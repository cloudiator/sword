/*
 * Copyright (c) 2014-2018 University of Ulm
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

import com.microsoft.azure.management.Azure;
import de.uniulm.omi.cloudiator.sword.drivers.azure.internal.ResourceGroupNamingStrategy;
import de.uniulm.omi.cloudiator.sword.strategy.DeleteVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.util.IdScopeByLocations;
import de.uniulm.omi.cloudiator.sword.util.IdScopedByLocation;
import javax.inject.Inject;

/**
 * Created by daniel on 07.06.17.
 */
public class AzureDeleteVirtualMachineStrategy implements DeleteVirtualMachineStrategy {

  private final Azure azure;
  private final ResourceGroupNamingStrategy resourceGroupNamingStrategy;

  @Inject
  public AzureDeleteVirtualMachineStrategy(Azure azure,
      ResourceGroupNamingStrategy resourceGroupNamingStrategy) {

    checkNotNull(resourceGroupNamingStrategy, "resourceGroupNamingStrategy is null");
    this.resourceGroupNamingStrategy = resourceGroupNamingStrategy;

    checkNotNull(azure, "azure is null");
    this.azure = azure;
  }

  @Override
  public void apply(String id) {

    checkNotNull(id, "id is null");

    IdScopedByLocation idScopedByLocation = IdScopeByLocations.from(id);
    final String resourceGroup = resourceGroupNamingStrategy
        .apply(idScopedByLocation.getLocationId());

    azure.virtualMachines().powerOff(resourceGroup, idScopedByLocation.getId());

    azure.virtualMachines().deleteByResourceGroup(resourceGroup, idScopedByLocation.getId());
  }
}
