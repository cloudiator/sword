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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.suppliers;

import com.google.common.base.Supplier;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import de.uniulm.omi.cloudiator.domain.Location;
import de.uniulm.omi.cloudiator.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.util.NamingStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.domain.ServerInRegion;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.internal.RegionSupplier;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.Server;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 30.11.16.
 */
public class VirtualMachineSupplier implements Supplier<Set<VirtualMachine>> {

    private final OneWayConverter<ServerInRegion, VirtualMachine> virtualMachineConverter;
    private final OSClient osClient;
    private final RegionSupplier regionSupplier;
    private final NamingStrategy namingStrategy;

    @Inject public VirtualMachineSupplier(
        OneWayConverter<ServerInRegion, VirtualMachine> virtualMachineConverter, OSClient osClient,
        RegionSupplier regionSupplier, NamingStrategy namingStrategy) {



        checkNotNull(virtualMachineConverter, "virtualMachineConverter is null");
        checkNotNull(osClient, "osClient is null");
        checkNotNull(regionSupplier, "regionSupplier is null");
        checkNotNull(namingStrategy, "namingStrategy is null");

        this.virtualMachineConverter = virtualMachineConverter;
        this.osClient = osClient;
        this.regionSupplier = regionSupplier;
        this.namingStrategy = namingStrategy;
    }

    @Override public Set<VirtualMachine> get() {
        Set<VirtualMachine> virtualMachines = new HashSet<>();
        for (Location region : regionSupplier.get()) {
            virtualMachines.addAll(
                osClient.useRegion(region.id()).compute().servers().list().stream().filter(
                    (Predicate<Server>) server -> namingStrategy.belongsToNamingGroup()
                        .test(server.getName())).map(
                    (Function<Server, ServerInRegion>) server -> new ServerInRegion(server, server,
                        region)).map(virtualMachineConverter).collect(Collectors.toSet()));
        }
        return virtualMachines;
    }
}
