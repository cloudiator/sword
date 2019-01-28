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

package de.uniulm.omi.cloudiator.sword.drivers.azure.suppliers;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Supplier;
import com.google.inject.Inject;
import com.microsoft.azure.CloudException;
import com.microsoft.azure.management.Azure;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.drivers.azure.domain.VirtualMachineSizeInRegion;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by daniel on 22.05.17.
 */
public class HardwareSupplier implements Supplier<Set<HardwareFlavor>> {

  private final Azure azure;
  private final Supplier<Set<Location>> regionSupplier;
  private final OneWayConverter<VirtualMachineSizeInRegion, HardwareFlavor> hardwareConverter;

  @Inject
  public HardwareSupplier(Azure azure,
      Supplier<Set<Location>> regionSupplier,
      OneWayConverter<VirtualMachineSizeInRegion, HardwareFlavor> hardwareConverter) {
    checkNotNull(hardwareConverter, "hardwareConverter is null");
    this.hardwareConverter = hardwareConverter;
    checkNotNull(regionSupplier, "regionSupplier is null");
    this.regionSupplier = regionSupplier;
    checkNotNull(azure, "azure is null");
    this.azure = azure;
  }

  @Override
  public Set<HardwareFlavor> get() {
    Set<VirtualMachineSizeInRegion> virtualMachineSizeInRegions = new HashSet<>();
    for (Location region : regionSupplier.get()) {
      try {
        azure.virtualMachines().sizes().listByRegion(region.id()).stream().map(
            virtualMachineSize -> new VirtualMachineSizeInRegion(virtualMachineSize, region))
            .forEach(virtualMachineSizeInRegions::add);
      } catch (CloudException e) {
        // swallow
        // some regions are already visible, but not available yet
      }
    }
    return virtualMachineSizeInRegions.stream()
        .map(hardwareConverter::apply)
        .collect(Collectors.toSet());
  }
}
