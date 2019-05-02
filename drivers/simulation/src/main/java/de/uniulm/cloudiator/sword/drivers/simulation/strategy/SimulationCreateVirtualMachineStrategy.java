/*
 * Copyright (c) 2014-2019 University of Ulm
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

package de.uniulm.cloudiator.sword.drivers.simulation.strategy;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Supplier;
import com.google.inject.Inject;
import de.uniulm.cloudiator.sword.drivers.simulation.config.SimulationModel;
import de.uniulm.cloudiator.sword.drivers.simulation.state.VirtualMachineStore;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.Image;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachineBuilder;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachineTemplate;
import de.uniulm.omi.cloudiator.sword.strategy.CreateVirtualMachineStrategy;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;

public class SimulationCreateVirtualMachineStrategy implements CreateVirtualMachineStrategy {

  private final SimulationModel simulationModel;
  private final Supplier<Set<Location>> locationSupplier;
  private final Supplier<Set<Image>> imageSupplier;
  private final Supplier<Set<HardwareFlavor>> hardwareSupplier;
  private final VirtualMachineStore virtualMachineStore;

  @Inject
  public SimulationCreateVirtualMachineStrategy(
      SimulationModel simulationModel,
      Supplier<Set<Location>> locationSupplier,
      Supplier<Set<Image>> imageSupplier,
      Supplier<Set<HardwareFlavor>> hardwareSupplier,
      VirtualMachineStore virtualMachineStore) {
    this.simulationModel = simulationModel;
    this.locationSupplier = locationSupplier;
    this.imageSupplier = imageSupplier;
    this.hardwareSupplier = hardwareSupplier;
    this.virtualMachineStore = virtualMachineStore;
  }

  @Override
  public VirtualMachine apply(VirtualMachineTemplate virtualMachineTemplate) {

    validateExists(virtualMachineTemplate);
    validateMatches(virtualMachineTemplate);

    System.out.println(simulationModel.vmStartupTime());

    final String id = UUID.randomUUID().toString();

    final VirtualMachine virtualMachine = VirtualMachineBuilder.newBuilder().id(id).providerId(id)
        .name(virtualMachineTemplate.name())
        .location(getLocation(virtualMachineTemplate.locationId()))
        .image(getImage(virtualMachineTemplate.imageId()))
        .hardware(getHardware(virtualMachineTemplate.hardwareFlavorId())).build();

    return virtualMachineStore.store(virtualMachine);
  }

  private boolean validateExists(VirtualMachineTemplate virtualMachineTemplate) {

    checkArgument(getLocation(virtualMachineTemplate.locationId()) != null,
        String.format("Location with id %s does not exist.", virtualMachineTemplate.locationId()));
    checkArgument(getImage(virtualMachineTemplate.imageId()) != null,
        String.format("Image with id %s does not exist.", virtualMachineTemplate.imageId()));
    checkArgument(getHardware(virtualMachineTemplate.hardwareFlavorId()) != null, String
        .format("Hardware with id %s does not exist.", virtualMachineTemplate.hardwareFlavorId()));

    return true;
  }

  private boolean validateMatches(VirtualMachineTemplate virtualMachineTemplate) {

    Location location = getLocation(virtualMachineTemplate.locationId());
    Image image = getImage(virtualMachineTemplate.imageId());
    HardwareFlavor hardwareFlavor = getHardware(virtualMachineTemplate.hardwareFlavorId());

    //noinspection ConstantConditions
    checkArgument(image.location().isPresent() && image.location().get().equals(location),
        String
            .format("Location of image %s does not match selected location %s.", image, location));
    //noinspection ConstantConditions
    checkArgument(
        hardwareFlavor.location().isPresent() && hardwareFlavor.location().get().equals(location),
        String
            .format("Location of hardware %s does not match selected location %s.", hardwareFlavor,
                location));

    return true;
  }

  @Nullable
  private Location getLocation(String id) {
    return locationSupplier.get().stream().filter(location -> location.id().equals(id)).findFirst()
        .orElse(null);
  }

  @Nullable
  private Image getImage(String id) {
    return imageSupplier.get().stream().filter(image -> image.id().equals(id)).findFirst()
        .orElse(null);
  }

  @Nullable
  private HardwareFlavor getHardware(String id) {
    return hardwareSupplier.get().stream().filter(hardware -> hardware.id().equals(id)).findFirst()
        .orElse(null);
  }


}
