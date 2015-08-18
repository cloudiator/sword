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
 * An abstract skeleton for compute modules.
 */
public abstract class AbstractComputeModule extends AbstractModule {

    /**
     * Can be extended to load own implementation of classes.
     */
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

    /**
     * @return the {@link Image} {@link ResourceSupplier} to use.
     */
    protected abstract Class<? extends ResourceSupplier<Set<Image>>> imageSupplier();

    /**
     * @return the {@link Location} {@link ResourceSupplier} to use.
     */
    protected abstract Class<? extends ResourceSupplier<Set<Location>>> locationSupplier();

    /**
     * @return the {@link HardwareFlavor} {@link ResourceSupplier} to use.
     */
    protected abstract Class<? extends ResourceSupplier<Set<HardwareFlavor>>> hardwareFlavorSupplier();

    /**
     * @return the {@link VirtualMachine} {@link ResourceSupplier} to use
     */
    protected abstract Class<? extends ResourceSupplier<Set<VirtualMachine>>> virtualMachineSupplier();

    /**
     * @return the {@link CreateVirtualMachineStrategy} used for creating {@link VirtualMachine}s
     */
    protected abstract Class<? extends CreateVirtualMachineStrategy> createVirtualMachineStrategy();

    /**
     * @return the {@link DeleteVirtualMachineStrategy} used for deleting {@link VirtualMachine}s
     */
    protected abstract Class<? extends DeleteVirtualMachineStrategy> deleteVirtualMachineStrategy();

    /**
     * Extension point for the {@link GetStrategy} for {@link VirtualMachine}s
     * Defaults to {@link de.uniulm.omi.cloudiator.sword.core.strategy.DefaultGetStrategy.DefaultVirtualMachineGetStrategy}
     *
     * @return the strategy class to use.
     */
    protected Class<? extends GetStrategy<String, VirtualMachine>> getVirtualMachineStrategy() {
        return DefaultGetStrategy.DefaultVirtualMachineGetStrategy.class;
    }

    /**
     * Extension point for the {@link GetStrategy} for {@link Image}s
     * Defaults to {@link de.uniulm.omi.cloudiator.sword.core.strategy.DefaultGetStrategy.DefaultImageGetStrategy}
     *
     * @return the strategy class to use.
     */
    protected Class<? extends GetStrategy<String, Image>> getImageStrategy() {
        return DefaultGetStrategy.DefaultImageGetStrategy.class;
    }

    /**
     * Extension point for the {@link GetStrategy} for {@link Location}s.
     * Defaults to {@link de.uniulm.omi.cloudiator.sword.core.strategy.DefaultGetStrategy.DefaultLocationGetStrategy}
     *
     * @return the strategy class to use.
     */
    protected Class<? extends GetStrategy<String, Location>> getLocationStrategy() {
        return DefaultGetStrategy.DefaultLocationGetStrategy.class;
    }

    /**
     * Extension point for the {@link GetStrategy} for {@link HardwareFlavor}s.
     * Defaults to {@link de.uniulm.omi.cloudiator.sword.core.strategy.DefaultGetStrategy.DefaultHardwareFlavorGetStrategy}
     *
     * @return the strategy class to use.
     */
    protected Class<? extends GetStrategy<String, HardwareFlavor>> getHardwareFlavorStrategy() {
        return DefaultGetStrategy.DefaultHardwareFlavorGetStrategy.class;
    }

    /**
     * Extension point for adding a {@link PublicIpService} extension.
     * <p>
     * Defaults to {@link com.google.common.base.Absent}
     *
     * @param injector injector for instantiating new classes.
     * @return an optional public ip service.
     */
    protected Optional<PublicIpService> publicIpService(Injector injector) {
        return Optional.absent();
    }

    /**
     * Extension point for adding a {@link KeyPairService} extension.
     * <p>
     * Defaults to {@link com.google.common.base.Absent}
     *
     * @param injector injector for instantiating new classes.
     * @return an optional key pair service.
     */
    protected Optional<KeyPairService> keyPairService(Injector injector) {
        return Optional.absent();
    }


}
