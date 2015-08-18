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

package de.uniulm.omi.cloudiator.sword.core.config;

import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import de.uniulm.omi.cloudiator.sword.api.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.api.domain.Image;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.api.extensions.KeyPairService;
import de.uniulm.omi.cloudiator.sword.api.extensions.PublicIpService;
import de.uniulm.omi.cloudiator.sword.api.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.api.strategy.DeleteVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.api.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.sword.api.supplier.ResourceSupplier;
import de.uniulm.omi.cloudiator.sword.core.strategy.DefaultGetStrategy;

import java.util.Set;

/**
 * Created by daniel on 02.12.14.
 */
public abstract class AbstractComputeModule extends AbstractModule {

    @Override protected void configure() {

        bind(new TypeLiteral<ResourceSupplier<Set<Image>>>() {
        }).to(imageSupplier());

        bind(new TypeLiteral<ResourceSupplier<Set<Location>>>() {
        }).to(locationSupplier());

        bind(new TypeLiteral<ResourceSupplier<Set<HardwareFlavor>>>() {
        }).to(hardwareFlavorSupplier());

        bind(new TypeLiteral<ResourceSupplier<Set<VirtualMachine>>>() {
        }).to(virtualMachineSupplier());

        bind(CreateVirtualMachineStrategy.class).to(createVirtualMachineStrategy());

        bind(DeleteVirtualMachineStrategy.class).to(deleteVirtualMachineStrategy());

        bind(new TypeLiteral<GetStrategy<String, VirtualMachine>>() {
        }).to(getVirtualMachineStrategy());

        bind(new TypeLiteral<GetStrategy<String, Image>>() {
        }).to(getImageStrategy());

        bind(new TypeLiteral<GetStrategy<String, Location>>() {
        }).to(getLocationStrategy());

        bind(new TypeLiteral<GetStrategy<String, HardwareFlavor>>() {
        }).to(getHardwareFlavorStrategy());
    }

    @Provides final Optional<PublicIpService> provideFloatingIpService(Injector injector) {
        return publicIpService(injector);
    }

    @Provides final Optional<KeyPairService> provideKeyPairService(Injector injector) {
        return keyPairService(injector);
    }

    protected abstract Class<? extends ResourceSupplier<Set<Image>>> imageSupplier();

    protected abstract Class<? extends ResourceSupplier<Set<Location>>> locationSupplier();

    protected abstract Class<? extends ResourceSupplier<Set<HardwareFlavor>>> hardwareFlavorSupplier();

    protected abstract Class<? extends ResourceSupplier<Set<VirtualMachine>>> virtualMachineSupplier();

    protected abstract Class<? extends CreateVirtualMachineStrategy> createVirtualMachineStrategy();

    protected abstract Class<? extends DeleteVirtualMachineStrategy> deleteVirtualMachineStrategy();

    protected Class<? extends GetStrategy<String, VirtualMachine>> getVirtualMachineStrategy() {
        return DefaultGetStrategy.DefaultVirtualMachineGetStrategy.class;
    }

    protected Class<? extends GetStrategy<String, Image>> getImageStrategy() {
        return DefaultGetStrategy.DefaultImageGetStrategy.class;
    }

    protected Class<? extends GetStrategy<String, Location>> getLocationStrategy() {
        return DefaultGetStrategy.DefaultLocationGetStrategy.class;
    }

    protected Class<? extends GetStrategy<String, HardwareFlavor>> getHardwareFlavorStrategy() {
        return DefaultGetStrategy.DefaultHardwareFlavorGetStrategy.class;
    }

    protected Optional<PublicIpService> publicIpService(Injector injector) {
        return Optional.absent();
    }

    protected Optional<KeyPairService> keyPairService(Injector injector) {
        return Optional.absent();
    }


}
