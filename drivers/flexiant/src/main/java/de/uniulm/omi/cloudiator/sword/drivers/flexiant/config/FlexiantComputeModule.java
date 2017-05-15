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

package de.uniulm.omi.cloudiator.sword.drivers.flexiant.config;

import com.google.common.base.Supplier;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import de.uniulm.omi.cloudiator.flexiant.client.domain.Hardware;
import de.uniulm.omi.cloudiator.flexiant.client.domain.Server;
import de.uniulm.omi.cloudiator.sword.config.AbstractComputeModule;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.Image;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.drivers.flexiant.converters.FlexiantHardwareToHardwareFlavor;
import de.uniulm.omi.cloudiator.sword.drivers.flexiant.converters.FlexiantImageToImage;
import de.uniulm.omi.cloudiator.sword.drivers.flexiant.converters.FlexiantLocationToLocation;
import de.uniulm.omi.cloudiator.sword.drivers.flexiant.converters.FlexiantServerToVirtualMachine;
import de.uniulm.omi.cloudiator.sword.drivers.flexiant.strategy.FlexiantCreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.flexiant.strategy.FlexiantDeleteVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.flexiant.suppliers.HardwareSupplier;
import de.uniulm.omi.cloudiator.sword.drivers.flexiant.suppliers.ImageSupplier;
import de.uniulm.omi.cloudiator.sword.drivers.flexiant.suppliers.LocationSupplier;
import de.uniulm.omi.cloudiator.sword.drivers.flexiant.suppliers.VirtualMachineSupplier;
import de.uniulm.omi.cloudiator.sword.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.strategy.DeleteVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import java.util.Set;


/**
 * Created by daniel on 02.12.14.
 */
public class FlexiantComputeModule extends AbstractComputeModule {

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
    return injector.getInstance(FlexiantCreateVirtualMachineStrategy.class);
  }

  @Override
  protected DeleteVirtualMachineStrategy deleteVirtualMachineStrategy(Injector injector) {
    return injector.getInstance(FlexiantDeleteVirtualMachineStrategy.class);
  }

  @Override
  protected void configure() {
    super.configure();

    //bind the image converter
    bind(
        new TypeLiteral<OneWayConverter<de.uniulm.omi.cloudiator.flexiant.client.domain.Image, Image>>() {
        }).to(FlexiantImageToImage.class);

    //bind the location converter
    bind(
        new TypeLiteral<OneWayConverter<de.uniulm.omi.cloudiator.flexiant.client.domain.Location, Location>>() {
        }).to(FlexiantLocationToLocation.class);

    //bind the hardware converter
    bind(new TypeLiteral<OneWayConverter<Hardware, HardwareFlavor>>() {
    }).to(FlexiantHardwareToHardwareFlavor.class);

    //bind the virtual machine converter
    bind(new TypeLiteral<OneWayConverter<Server, VirtualMachine>>() {
    }).to(FlexiantServerToVirtualMachine.class);
  }
}
