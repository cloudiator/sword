/*
 * Copyright (c) 2014-2016 University of Ulm
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.strategy;

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.LocationScope;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachineTemplate;
import de.uniulm.omi.cloudiator.sword.api.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.api.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.sword.core.util.IdScopeByLocations;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.domain.ServerInRegion;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Created by daniel on 18.11.16.
 */
public class Openstack4jCreateVirtualMachineStrategy implements CreateVirtualMachineStrategy {

    private final OSClient osClient;
    private final OneWayConverter<ServerInRegion, VirtualMachine> virtualMachineConverter;
    private final GetStrategy<String, Location> locationGetStrategy;

    @Inject public Openstack4jCreateVirtualMachineStrategy(OSClient osClient,
        OneWayConverter<ServerInRegion, VirtualMachine> virtualMachineConverter,
        GetStrategy<String, Location> locationGetStrategy) {
        checkNotNull(locationGetStrategy, "locationGetStrategy is null");
        this.locationGetStrategy = locationGetStrategy;
        checkNotNull(virtualMachineConverter, "virtualMachineConverter is null");
        this.virtualMachineConverter = virtualMachineConverter;
        checkNotNull(osClient, "osClient is null");
        this.osClient = osClient;
    }

    @Override public VirtualMachine apply(VirtualMachineTemplate virtualMachineTemplate) {

        //todo we currently simply assume that this is a zone (hosts may follow)
        Location zone = locationGetStrategy.get(virtualMachineTemplate.locationId());
        checkNotNull(zone, "zone is null");
        checkState(LocationScope.ZONE.equals(zone.locationScope()), "zone is no zone");
        checkState(zone.parent().isPresent(), "zone has no parent region");
        Location region = zone.parent().get();

        //todo this code also assumes that location is always the availability zone
        final ServerCreate serverCreate = Builders.server().name(virtualMachineTemplate.name())
            .flavor(IdScopeByLocations.from(virtualMachineTemplate.hardwareFlavorId()).getId())
            .image(IdScopeByLocations.from(virtualMachineTemplate.imageId()).getId())
            .availabilityZone(IdScopeByLocations.from(virtualMachineTemplate.locationId()).getId())
            .build();
        final Server server =
            osClient.useRegion(region.id()).compute().servers().boot(serverCreate);
        return virtualMachineConverter.apply(new ServerInRegion(server, region));
    }
}
