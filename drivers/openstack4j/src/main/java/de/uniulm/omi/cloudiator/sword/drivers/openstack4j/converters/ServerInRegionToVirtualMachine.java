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

import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.core.domain.VirtualMachineBuilder;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.domain.ServerInRegion;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.openstack4j.model.compute.Address;

/**
 * Created by daniel on 18.11.16.
 */
public class ServerInRegionToVirtualMachine
    implements OneWayConverter<ServerInRegion, VirtualMachine> {

    @Override public VirtualMachine apply(ServerInRegion serverInRegion) {
        //todo check which region to return. Always region or av or host?
        //todo add login credential and ip addresses

        VirtualMachineBuilder virtualMachineBuilder = VirtualMachineBuilder.newBuilder().name(serverInRegion.getName())
            .id(serverInRegion.getId()).providerId(serverInRegion.providerId())
            .location(serverInRegion.region());

        Set<Entry<String, List<? extends Address>>> addressSet = serverInRegion
            .getAddresses().getAddresses().entrySet();

        if(addressSet.size()>1){
            //TODO: server is configured with multiple network types, how to handle this?
            //LOGGER.warn(String.format("VM %s is configured with multiple network types!", serverInRegion.getId()));

        }

        //add all available addresses
        if(addressSet.iterator().hasNext()){
            Entry<String, List<? extends Address>> addressEntry = addressSet.iterator().next();
            //LOGGER.warn(String.format("Using addresses for network:  %s", addressEntry.getKey()));

            List<? extends Address> addressList = addressEntry.getValue();

            for (Address address : addressList){

                //TODO: currently only adding fixed IPs?
                if(address.getType().equals("fixed")){
                    //LOGGER.warn(String.format("Found private (fixed) IP: %s", address.getAddr()));
                    virtualMachineBuilder.addPrivateIpAddress(address.getAddr());

                }
            }

        }

        return virtualMachineBuilder.build();
    }
}
