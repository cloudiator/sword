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

package de.uniulm.omi.cloudiator.sword.drivers.azure.config;

import com.google.common.base.Supplier;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.microsoft.azure.management.Azure;
import de.uniulm.omi.cloudiator.sword.config.AbstractComputeModule;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.Image;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.drivers.azure.converters.AzureLocationToLocation;
import de.uniulm.omi.cloudiator.sword.drivers.azure.converters.AzureVirtualMachineToVirtualMachine;
import de.uniulm.omi.cloudiator.sword.drivers.azure.converters.VirtualMachineSizeInRegionToHardwareFlavor;
import de.uniulm.omi.cloudiator.sword.drivers.azure.domain.VirtualMachineSizeInRegion;
import de.uniulm.omi.cloudiator.sword.drivers.azure.internal.AzureProvider;
import de.uniulm.omi.cloudiator.sword.drivers.azure.strategies.AzureCreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.azure.strategies.AzureDeleteVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.azure.suppliers.HardwareSupplier;
import de.uniulm.omi.cloudiator.sword.drivers.azure.suppliers.ImageSupplier;
import de.uniulm.omi.cloudiator.sword.drivers.azure.suppliers.LocationSupplier;
import de.uniulm.omi.cloudiator.sword.drivers.azure.suppliers.VirtualMachineSupplier;
import de.uniulm.omi.cloudiator.sword.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.strategy.DeleteVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import java.util.Collections;
import java.util.Set;

/**
 * Created by daniel on 16.05.17.
 */
public class AzureComputeModule extends AbstractComputeModule {

  @Override
  protected void configure() {
    super.configure();
    bind(Azure.class).toProvider(AzureProvider.class).in(Singleton.class);
    bind(
        new TypeLiteral<OneWayConverter<com.microsoft.azure.management.resources.Location, de.uniulm.omi.cloudiator.sword.domain.Location>>() {
        }).to(
        AzureLocationToLocation.class);
    bind(new TypeLiteral<OneWayConverter<VirtualMachineSizeInRegion, HardwareFlavor>>() {
    }).to(
        VirtualMachineSizeInRegionToHardwareFlavor.class);
    bind(
        new TypeLiteral<OneWayConverter<com.microsoft.azure.management.compute.VirtualMachine, VirtualMachine>>() {
        }).to(
        AzureVirtualMachineToVirtualMachine.class);
  }

  @Override
  protected Supplier<Set<Image>> imageSupplier(Injector injector) {
    return injector.getInstance(ImageSupplier.class);
  }

  @Override
  protected Supplier<Set<Location>> locationSupplier(Injector injector) {
    return injector.getInstance(LocationSupplier.class);
  }

  @Override
  protected Supplier<Set<HardwareFlavor>> hardwareFlavorSupplier(Injector injector) {
    return injector.getInstance(HardwareSupplier.class);
  }

  @Override
  protected Supplier<Set<VirtualMachine>> virtualMachineSupplier(Injector injector) {
    return injector.getInstance(VirtualMachineSupplier.class);
  }

  @Override
  protected CreateVirtualMachineStrategy createVirtualMachineStrategy(Injector injector) {
    return injector.getInstance(AzureCreateVirtualMachineStrategy.class);
  }

  @Override
  protected DeleteVirtualMachineStrategy deleteVirtualMachineStrategy(Injector injector) {
    return injector.getInstance(AzureDeleteVirtualMachineStrategy.class);
  }
}
