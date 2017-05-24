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

import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavorBuilder;
import de.uniulm.omi.cloudiator.sword.drivers.azure.domain.VirtualMachineSizeInRegion;
import de.uniulm.omi.cloudiator.util.OneWayConverter;

/**
 * Created by daniel on 22.05.17.
 */
public class VirtualMachineSizeInRegionToHardwareFlavor implements
    OneWayConverter<VirtualMachineSizeInRegion, HardwareFlavor> {

  @Override
  public HardwareFlavor apply(VirtualMachineSizeInRegion virtualMachineSizeInRegion) {
    return HardwareFlavorBuilder.newBuilder().id(virtualMachineSizeInRegion.name())
        .providerId(virtualMachineSizeInRegion.providerId())
        .name(virtualMachineSizeInRegion.providerId())
        .cores(virtualMachineSizeInRegion.numberOfCores())
        .gbDisk((float) virtualMachineSizeInRegion.osDiskSizeInMB() / 1000)
        .mbRam(virtualMachineSizeInRegion.memoryInMB())
        .location(virtualMachineSizeInRegion.region()).build();
  }
}
