/*
 * Copyright (c) 2014-2015 University of Ulm
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.uniulm.omi.cloudiator.sword.drivers.flexiant.converters;


import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.flexiant.client.domain.Server;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.core.domain.VirtualMachineBuilder;

/**
 * Created by daniel on 10.12.14.
 */
public class FlexiantServerToVirtualMachine implements OneWayConverter<Server, VirtualMachine> {

    @Override public VirtualMachine apply(final Server server) {

        final VirtualMachineBuilder virtualMachineBuilder = VirtualMachineBuilder.newBuilder();
        virtualMachineBuilder.id(server.getLocationUUID() + "/" + server.getId())
            .name(server.getName());
        if (server.getPublicIpAddress() != null) {
            virtualMachineBuilder.addPublicIpAddress(server.getPublicIpAddress());
        }
        if (server.getPrivateIpAddress() != null) {
            virtualMachineBuilder.addPrivateIpAddress(server.getPrivateIpAddress());
        }
        return virtualMachineBuilder.build();
    }
}
