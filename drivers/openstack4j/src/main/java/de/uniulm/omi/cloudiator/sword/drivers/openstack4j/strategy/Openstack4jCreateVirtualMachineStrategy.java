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
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import de.uniulm.omi.cloudiator.domain.Location;
import de.uniulm.omi.cloudiator.domain.LocationScope;
import de.uniulm.omi.cloudiator.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.domain.VirtualMachineTemplate;
import de.uniulm.omi.cloudiator.sword.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.sword.util.NamingStrategy;
import de.uniulm.omi.cloudiator.sword.util.IdScopeByLocations;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.domain.ServerInRegion;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.compute.builder.ServerCreateBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Created by daniel on 18.11.16.
 */
public class Openstack4jCreateVirtualMachineStrategy implements CreateVirtualMachineStrategy {

    private final OSClient osClient;
    private final OneWayConverter<ServerInRegion, VirtualMachine> virtualMachineConverter;
    private final GetStrategy<String, Location> locationGetStrategy;
    private final OpenstackNetworkStrategy networkStrategy;
    private final NamingStrategy namingStrategy;
    private final CreateSecurityGroupFromTemplateOption createSecurityGroupFromTemplateOption;

    @Inject public Openstack4jCreateVirtualMachineStrategy(OSClient osClient,
        OneWayConverter<ServerInRegion, VirtualMachine> virtualMachineConverter,
        GetStrategy<String, Location> locationGetStrategy, OpenstackNetworkStrategy networkStrategy,
        NamingStrategy namingStrategy,
        CreateSecurityGroupFromTemplateOption createSecurityGroupFromTemplateOption) {
        checkNotNull(createSecurityGroupFromTemplateOption,
            "createSecurityGroupFromTemplateOption is null");
        this.createSecurityGroupFromTemplateOption = createSecurityGroupFromTemplateOption;
        checkNotNull(namingStrategy, "namingStrategy is null");
        this.namingStrategy = namingStrategy;
        checkNotNull(networkStrategy, "networkStrategy is null");
        this.networkStrategy = networkStrategy;
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

        List<String> networks = new ArrayList<>(1);
        if (networkStrategy.get() != null) {
            networks.add(networkStrategy.get());
        }

        //keypair
        String keyPair = null;
        if (virtualMachineTemplate.templateOptions().isPresent()) {
            keyPair = virtualMachineTemplate.templateOptions().get().keyPairName();
        }

        List<String> secGroups = new ArrayList<>(1);
        if (virtualMachineTemplate.templateOptions().isPresent()) {
            secGroups.add(createSecurityGroupFromTemplateOption
                .create(virtualMachineTemplate.templateOptions().get(),
                    virtualMachineTemplate.locationId()));
        }

        //todo this code also assumes that location is always the availability zone
        final ServerCreateBuilder serverCreateBuilder = Builders.server()
            .name(namingStrategy.generateUniqueNameBasedOnName(virtualMachineTemplate.name()))
            .flavor(IdScopeByLocations.from(virtualMachineTemplate.hardwareFlavorId()).getId())
            .image(IdScopeByLocations.from(virtualMachineTemplate.imageId()).getId())
            .availabilityZone(IdScopeByLocations.from(virtualMachineTemplate.locationId()).getId())
            .networks(networks).keypairName(keyPair);
        for (String secGroup : secGroups) {
            serverCreateBuilder.addSecurityGroup(secGroup);
        }
        final ServerCreate serverCreate = serverCreateBuilder.build();
        //todo make timeout configurable
        final Server createdServer = osClient.useRegion(region.id()).compute().servers()
            .bootAndWaitActive(serverCreate, 120000);
        // we retrieve the newly created server to get additional details the creation request does
        // not contain
        final Server retrievedServer = osClient.compute().servers().get(createdServer.getId());
        checkState(retrievedServer != null,
            "Could not retrieve newly created server with id " + createdServer.getId());
        return virtualMachineConverter
            .apply(new ServerInRegion(createdServer, retrievedServer, region));
    }
}
