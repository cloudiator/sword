/*
 * Copyright (c) 2014-2017 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.drivers.azure.converters;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Inject;
import com.microsoft.azure.management.compute.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachineBuilder;
import de.uniulm.omi.cloudiator.sword.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.sword.util.IdScopeByLocations;
import de.uniulm.omi.cloudiator.util.OneWayConverter;

/**
 * Created by daniel on 22.05.17.
 */
public class AzureVirtualMachineToVirtualMachine implements
    OneWayConverter<VirtualMachine, de.uniulm.omi.cloudiator.sword.domain.VirtualMachine> {

  private final GetStrategy<String, Location> locationGetStrategy;
  private final GetStrategy<String, HardwareFlavor> hardwareGetStrategy;

  @Inject
  public AzureVirtualMachineToVirtualMachine(
      GetStrategy<String, Location> locationGetStrategy,
      GetStrategy<String, HardwareFlavor> hardwareGetStrategy) {
    
    checkNotNull(hardwareGetStrategy, "hardwareGetStrategy is null");
    this.hardwareGetStrategy = hardwareGetStrategy;

    checkNotNull(locationGetStrategy, "locationGetStrategy is null");
    this.locationGetStrategy = locationGetStrategy;
  }

  @Override
  public de.uniulm.omi.cloudiator.sword.domain.VirtualMachine apply(
      VirtualMachine virtualMachine) {

    final String id = IdScopeByLocations
        .from(virtualMachine.regionName(), virtualMachine.name()).getIdWithLocation();

    return VirtualMachineBuilder.newBuilder()
        .addIpString(virtualMachine.getPrimaryNetworkInterface().primaryPrivateIP())
        .addIpString(virtualMachine.getPrimaryPublicIPAddress().ipAddress())
        .name(virtualMachine.name()).id(id)
        .providerId(virtualMachine.vmId())
        .location(locationGetStrategy.get(virtualMachine.regionName()))
        .hardware(hardwareGetStrategy.get(
            IdScopeByLocations.from(virtualMachine.regionName(), virtualMachine.size().toString())
                .getIdWithLocation())).build();
  }
}
