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

package de.uniulm.omi.cloudiator.sword.drivers.jclouds.converters;


import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavorBuilder;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import java.util.Optional;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.Processor;
import org.jclouds.compute.domain.Volume;

/**
 * Created by daniel on 03.12.14.
 */
public class JCloudsHardwareToHardwareFlavor implements OneWayConverter<Hardware, HardwareFlavor> {

  private final OneWayConverter<org.jclouds.domain.Location, Location> locationConverter;

  @Inject
  public JCloudsHardwareToHardwareFlavor(
      OneWayConverter<org.jclouds.domain.Location, Location> locationConverter) {
    this.locationConverter = locationConverter;
  }

  @Override
  public HardwareFlavor apply(Hardware hardware) {
    if (hardware == null) {
      return null;
    }
    int cores = 0;
    for (Processor processor : hardware.getProcessors()) {
      cores += processor.getCores();
    }
    Float gbDisk = null;
    final Optional<? extends Volume> bootVolume =
        hardware.getVolumes().stream().filter(Volume::isBootDevice).findFirst();
    if (bootVolume.isPresent()) {
      gbDisk = bootVolume.get().getSize();
      if (gbDisk == 0) {
        gbDisk = null;
      }
    }

    return HardwareFlavorBuilder.newBuilder().id(hardware.getId())
        .providerId(hardware.getProviderId()).name(forceName(hardware)).cores(cores)
        .mbRam(hardware.getRam()).gbDisk(gbDisk)
        .location(locationConverter.apply(hardware.getLocation())).build();
  }

  private String forceName(Hardware hardware) {
    if (hardware.getName() == null) {
      return hardware.getId();
    }
    return hardware.getName();
  }
}
