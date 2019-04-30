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

package de.uniulm.cloudiator.sword.drivers.simulation.config;

import com.google.common.base.Supplier;
import com.google.inject.Injector;
import de.uniulm.cloudiator.sword.drivers.simulation.state.VirtualMachineStore;
import de.uniulm.cloudiator.sword.drivers.simulation.strategy.SimulationDeleteVirtualMachineStrategy;
import de.uniulm.cloudiator.sword.drivers.simulation.strategy.SimulationVirtualMachineStrategy;
import de.uniulm.cloudiator.sword.drivers.simulation.suppliers.HardwareGenerator;
import de.uniulm.cloudiator.sword.drivers.simulation.suppliers.ImageGenerator;
import de.uniulm.cloudiator.sword.drivers.simulation.suppliers.LocationGenerator;
import de.uniulm.omi.cloudiator.sword.config.AbstractComputeModule;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.Image;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.strategy.DeleteVirtualMachineStrategy;
import java.util.Set;

public class SimulationComputeModule extends AbstractComputeModule {

  @Override
  protected Supplier<Set<Image>> imageSupplier(Injector injector) {
    return injector.getInstance(ImageGenerator.class);
  }

  @Override
  protected Supplier<Set<Location>> locationSupplier(Injector injector) {
    return injector.getInstance(LocationGenerator.class);
  }

  @Override
  protected Supplier<Set<HardwareFlavor>> hardwareFlavorSupplier(Injector injector) {
    return injector.getInstance(HardwareGenerator.class);
  }

  @Override
  protected Supplier<Set<VirtualMachine>> virtualMachineSupplier(Injector injector) {
    return injector.getInstance(VirtualMachineStore.class);
  }

  @Override
  protected CreateVirtualMachineStrategy createVirtualMachineStrategy(Injector injector) {
    return injector.getInstance(SimulationVirtualMachineStrategy.class);
  }

  @Override
  protected DeleteVirtualMachineStrategy deleteVirtualMachineStrategy(Injector injector) {
    return injector.getInstance(SimulationDeleteVirtualMachineStrategy.class);
  }
}
