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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.converters;

import de.uniulm.omi.cloudiator.util.OneWayConverter;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.Image;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachineBuilder;
import de.uniulm.omi.cloudiator.sword.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.domain.ServerInRegion;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 18.11.16.
 */
public class ServerInRegionToVirtualMachine
    implements OneWayConverter<ServerInRegion, VirtualMachine> {

    private final GetStrategy<String, Image> imageGetStrategy;
    private final GetStrategy<String, HardwareFlavor> hardwareFlavorGetStrategy;

    public ServerInRegionToVirtualMachine(GetStrategy<String, Image> imageGetStrategy,
        GetStrategy<String, HardwareFlavor> hardwareFlavorGetStrategy) {

        checkNotNull(imageGetStrategy, "imageGetStrategy is null");
        checkNotNull(hardwareFlavorGetStrategy, "hardwareFlavorGetStrategy is null");

        this.imageGetStrategy = imageGetStrategy;
        this.hardwareFlavorGetStrategy = hardwareFlavorGetStrategy;
    }

    @Override public VirtualMachine apply(ServerInRegion serverInRegion) {
        //todo check which region to return. Always region or av or host?
        //todo add login credential and ip addresses

        return VirtualMachineBuilder.newBuilder().name(serverInRegion.getName())
            .image(imageGetStrategy.get(serverInRegion.getImageId()))
            .hardware(hardwareFlavorGetStrategy.get(serverInRegion.getFlavorId()))
            .id(serverInRegion.getId()).providerId(serverInRegion.providerId())
            .location(serverInRegion.region()).build();
    }
}
