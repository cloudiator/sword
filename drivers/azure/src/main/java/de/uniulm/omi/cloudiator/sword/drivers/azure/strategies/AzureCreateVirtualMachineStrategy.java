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

package de.uniulm.omi.cloudiator.sword.drivers.azure.strategies;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.inject.Inject;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.compute.KnownLinuxVirtualMachineImage;
import de.uniulm.omi.cloudiator.sword.domain.Cloud;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachineTemplate;
import de.uniulm.omi.cloudiator.sword.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.util.OneWayConverter;

/**
 * Created by daniel on 22.05.17.
 */
public class AzureCreateVirtualMachineStrategy implements
    CreateVirtualMachineStrategy {

  private final Azure azure;
  private final GetStrategy<String, HardwareFlavor> hardwareGetStrategy;
  private final String nodeGroup;
  private final OneWayConverter<com.microsoft.azure.management.compute.VirtualMachine, VirtualMachine> virtualMachineConverter;

  @Inject
  public AzureCreateVirtualMachineStrategy(Azure azure,
      GetStrategy<String, HardwareFlavor> hardwareGetStrategy, Cloud cloud,
      OneWayConverter<com.microsoft.azure.management.compute.VirtualMachine, VirtualMachine> virtualMachineConverter) {
    checkNotNull(virtualMachineConverter, "virtualMachineConverter is null");
    this.virtualMachineConverter = virtualMachineConverter;
    checkNotNull(hardwareGetStrategy, "hardwareGetStrategy is null");
    this.hardwareGetStrategy = hardwareGetStrategy;
    checkNotNull(azure, "azure is null");
    this.azure = azure;
    nodeGroup = cloud.configuration().nodeGroup();
  }

  private String resourceGroupName(String region) {
    return region + nodeGroup;
  }

  @Override
  public VirtualMachine apply(VirtualMachineTemplate virtualMachineTemplate) {

    HardwareFlavor hardwareFlavor = hardwareGetStrategy
        .get(virtualMachineTemplate.hardwareFlavorId());
    checkState(hardwareFlavor != null, String.format("hardwareFlavor with id %s does not exist",
        virtualMachineTemplate.hardwareFlavorId()));

    final boolean nodeGroupResourceGroupExists = azure.resourceGroups()
        .checkExistence(resourceGroupName(virtualMachineTemplate.locationId()));
    if (!nodeGroupResourceGroupExists) {
      azure.resourceGroups().define(resourceGroupName(virtualMachineTemplate.locationId()))
          .withRegion(virtualMachineTemplate.locationId())
          .create();
    }

    final com.microsoft.azure.management.compute.VirtualMachine virtualMachine = azure
        .virtualMachines().define(virtualMachineTemplate.name())
        .withRegion(virtualMachineTemplate.locationId())
        .withExistingResourceGroup(resourceGroupName(virtualMachineTemplate.locationId()))
        .withNewPrimaryNetwork("10.0.0.0/28").withPrimaryPrivateIPAddressDynamic()
        .withNewPrimaryPublicIPAddress(virtualMachineTemplate.name().toLowerCase())
        .withPopularLinuxImage(
            KnownLinuxVirtualMachineImage.valueOf(virtualMachineTemplate.imageId()))
        .withRootUsername("ubuntu").withRootPassword("oop!aiYooP9e")
        .withSize(hardwareFlavor.providerId()).create();

    return virtualMachineConverter.apply(virtualMachine);
  }
}
