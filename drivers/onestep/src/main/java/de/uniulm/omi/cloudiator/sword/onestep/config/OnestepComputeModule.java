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

package de.uniulm.omi.cloudiator.sword.onestep.config;

import com.google.common.base.Supplier;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.oktawave.api.client.ApiClient;
import com.oktawave.api.client.api.*;
import com.oktawave.api.client.model.InstanceType;
import com.oktawave.api.client.model.Subregion;
import com.oktawave.api.client.model.Template;
import de.uniulm.omi.cloudiator.sword.config.AbstractComputeModule;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.Image;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.onestep.converters.InstanceTypeToHardwareFlavor;
import de.uniulm.omi.cloudiator.sword.onestep.converters.InstanceWithAccessDataToVirtualMachine;
import de.uniulm.omi.cloudiator.sword.onestep.converters.RegionToLocation;
import de.uniulm.omi.cloudiator.sword.onestep.converters.ImageTemplateToImage;
import de.uniulm.omi.cloudiator.sword.onestep.domain.ActiveRegionsSet;
import de.uniulm.omi.cloudiator.sword.onestep.domain.ImageTemplate;
import de.uniulm.omi.cloudiator.sword.onestep.domain.InstanceWithAccessData;
import de.uniulm.omi.cloudiator.sword.onestep.internal.ActiveRegionsProvider;
import de.uniulm.omi.cloudiator.sword.onestep.internal.OnestepProvider;
import de.uniulm.omi.cloudiator.sword.onestep.strategies.OktawaveCreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.onestep.strategies.OktawaveDeleteVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.onestep.suppliers.*;
import de.uniulm.omi.cloudiator.sword.onestep.suppliers.ImageTemplatesProvider;
import de.uniulm.omi.cloudiator.sword.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.strategy.DeleteVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.util.OneWayConverter;

import java.util.Set;

/**
 * Created by pszkup on 08.05.2019
 */
public class OnestepComputeModule extends AbstractComputeModule {

  @Override
  protected void configure() {
    super.configure();
    // azure api
    bind(ApiClient.class).toProvider(OnestepProvider.class).in(Singleton.class);


    //Note that those two classes are needed as:
    //HardwareSupplier and ImageSupplier both needs same regions and operatingSystemsLists
    bind(ActiveRegionsSet.class).toProvider(ActiveRegionsProvider.class).in(Singleton.class);
    bind(ImageTemplate.class).toProvider(ImageTemplatesProvider.class).in(Singleton.class);

    bind(DictionariesApi.class).toInstance(new DictionariesApi());
    bind(SubregionsApi.class).toInstance(new SubregionsApi());
    bind(OciTemplatesApi.class).toInstance(new OciTemplatesApi());
    bind(AccountApi.class).toInstance(new AccountApi());
    bind(OciApi.class).toInstance(new OciApi());

    // converters
    bind(new TypeLiteral<OneWayConverter<Subregion, Location>>() {}).to(RegionToLocation.class);
    bind(new TypeLiteral<OneWayConverter<InstanceType, HardwareFlavor>>() {}).to(InstanceTypeToHardwareFlavor.class);
    bind(new TypeLiteral<OneWayConverter<Template, Image>>() {}).to(ImageTemplateToImage.class);
    bind(new TypeLiteral<OneWayConverter<InstanceWithAccessData, VirtualMachine>>() {}).to(InstanceWithAccessDataToVirtualMachine.class);

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
