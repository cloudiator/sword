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

package de.uniulm.omi.cloudiator.sword.drivers.oktawave.config;

import com.google.common.base.Supplier;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.oktawave.api.client.handler.*;
import com.oktawave.api.client.model.InstanceType;
import com.oktawave.api.client.model.Subregion;
import com.oktawave.api.client.model.Template;
import de.uniulm.omi.cloudiator.sword.config.AbstractComputeModule;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.Image;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.drivers.oktawave.converters.InstanceTypeToHardwareFlavor;
import de.uniulm.omi.cloudiator.sword.drivers.oktawave.converters.InstanceWithAccessDataToVirtualMachine;
import de.uniulm.omi.cloudiator.sword.drivers.oktawave.converters.SubregionToLocation;
import de.uniulm.omi.cloudiator.sword.drivers.oktawave.converters.TemplateToImage;
import de.uniulm.omi.cloudiator.sword.drivers.oktawave.domain.InstanceWithAccessData;
import de.uniulm.omi.cloudiator.sword.drivers.oktawave.internal.OktawaveProvider;
import de.uniulm.omi.cloudiator.sword.drivers.oktawave.strategies.OktawaveCreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.oktawave.strategies.OktawaveDeleteVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.oktawave.suppliers.*;
import de.uniulm.omi.cloudiator.sword.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.strategy.DeleteVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.util.OneWayConverter;

import java.util.Set;

/**
 * Created by pszkup on 08.05.2019
 */
public class OktawaveComputeModule extends AbstractComputeModule {

  @Override
  protected void configure() {
    super.configure();
    // azure api
    bind(ApiClient.class).toProvider(OktawaveProvider.class).in(Singleton.class);

    bind(DictionariesApi.class).toInstance(new DictionariesApi());
    bind(SubregionsApi.class).toInstance(new SubregionsApi());
    bind(OciTemplatesApi.class).toInstance(new OciTemplatesApi());
    bind(AccountApi.class).toInstance(new AccountApi());
    bind(OciApi.class).toInstance(new OciApi());

    // converters
    bind(new TypeLiteral<OneWayConverter<Subregion, Location>>() {}).to(SubregionToLocation.class);
    bind(new TypeLiteral<OneWayConverter<InstanceType, HardwareFlavor>>() {}).to(InstanceTypeToHardwareFlavor.class);
    bind(new TypeLiteral<OneWayConverter<Template, Image>>() {}).to(TemplateToImage.class);
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
