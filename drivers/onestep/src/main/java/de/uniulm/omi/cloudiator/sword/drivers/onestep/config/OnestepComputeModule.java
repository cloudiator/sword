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

package de.uniulm.omi.cloudiator.sword.drivers.onestep.config;

import client.api.ApiClient;
import client.api.RegionsApi;
import client.api.TemplatesApi;
import client.model.Region;
import com.google.common.base.Supplier;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import de.uniulm.omi.cloudiator.sword.config.AbstractComputeModule;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.Image;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.converters.ImageTemplateToImage;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.domain.ImageTemplate;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.strategies.OktawaveCreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.strategies.OktawaveDeleteVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.suppliers.HardwareSupplier;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.suppliers.ImageSupplier;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.suppliers.LocationSupplier;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.converters.RegionToLocation;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.domain.ActiveRegionsSet;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.domain.ImageTemplatesSet;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.internal.ActiveRegionsProvider;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.internal.OnestepProvider;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.suppliers.*;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.internal.ImageTemplatesProvider;
import de.uniulm.omi.cloudiator.sword.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.strategy.DeleteVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.util.OneWayConverter;

import java.util.Set;

/**
 * Created by mriedl 07.2020
 */
public class OnestepComputeModule extends AbstractComputeModule {

  @Override
  protected void configure() {
    super.configure();
    // azure api
    bind(ApiClient.class).toProvider(OnestepProvider.class).in(Singleton.class);

    bind(RegionsApi.class).toInstance(new RegionsApi());
    bind(TemplatesApi.class).toInstance(new TemplatesApi());

    //Note that those two classes are needed as:
    //HardwareSupplier and ImageSupplier both needs same regions and operatingSystemsLists
    bind(ActiveRegionsSet.class).toProvider(ActiveRegionsProvider.class).in(Singleton.class);
    bind(ImageTemplatesSet.class).toProvider(ImageTemplatesProvider.class).in(Singleton.class);

    //bind(DictionariesApi.class).toInstance(new DictionariesApi());

    //bind(AccountApi.class).toInstance(new AccountApi());
    //bind(OciApi.class).toInstance(new OciApi());

    // converters
    bind(new TypeLiteral<OneWayConverter<Region, Location>>() {}).to(RegionToLocation.class);
    bind(new TypeLiteral<OneWayConverter<ImageTemplate, Image>>() {}).to(ImageTemplateToImage.class);
    //bind(new TypeLiteral<OneWayConverter<InstanceWithAccessData, VirtualMachine>>() {}).to(InstanceWithAccessDataToVirtualMachine.class);

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
    return injector.getInstance(OktawaveCreateVirtualMachineStrategy.class);
  }

  @Override
  protected DeleteVirtualMachineStrategy deleteVirtualMachineStrategy(Injector injector) {
    return injector.getInstance(OktawaveDeleteVirtualMachineStrategy.class);
  }

}
