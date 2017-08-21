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


import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.flexiant.client.domain.Server;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.domain.LoginCredentialBuilder;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachineBuilder;
import de.uniulm.omi.cloudiator.sword.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.util.OneWayConverter;

/**
 * Created by daniel on 10.12.14.
 */
public class FlexiantServerToVirtualMachine implements OneWayConverter<Server, VirtualMachine> {

  private final GetStrategy<String, Location> locationGetStrategy;

  @Inject
  public FlexiantServerToVirtualMachine(GetStrategy<String, Location> locationGetStrategy) {
    this.locationGetStrategy = locationGetStrategy;
  }

  @Override
  public VirtualMachine apply(final Server server) {

    final VirtualMachineBuilder virtualMachineBuilder = VirtualMachineBuilder.newBuilder();
    virtualMachineBuilder.id(server.getLocationUUID() + "/" + server.getId())
        .providerId(server.getId()).name(server.getName());
    if (server.getPublicIpAddress() != null) {
      virtualMachineBuilder.addIpString(server.getPublicIpAddress());
    }
    if (server.getPrivateIpAddress() != null) {
      virtualMachineBuilder.addIpString(server.getPrivateIpAddress());
    }
    if (server.getInitialPassword() != null && server.getInitialUser() != null) {
      virtualMachineBuilder.loginCredential(
          LoginCredentialBuilder.newBuilder().username(server.getInitialUser())
              .password(server.getInitialPassword()).build());
    }
    virtualMachineBuilder.location(locationGetStrategy.get(server.getLocationUUID()));
    return virtualMachineBuilder.build();
  }
}
