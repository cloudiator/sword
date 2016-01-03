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
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.inject.*;
import com.google.inject.multibindings.Multibinder;
import de.uniulm.omi.cloudiator.sword.api.annotations.Memoized;
import de.uniulm.omi.cloudiator.sword.api.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.api.domain.Image;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.api.extensions.KeyPairService;
import de.uniulm.omi.cloudiator.sword.api.extensions.MigrationService;
import de.uniulm.omi.cloudiator.sword.api.extensions.PublicIpService;
import de.uniulm.omi.cloudiator.sword.api.service.DiscoveryService;
import de.uniulm.omi.cloudiator.sword.api.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.api.strategy.DeleteVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.api.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.sword.api.strategy.VirtualMachineMigrationStrategy;
import de.uniulm.omi.cloudiator.sword.core.base.BaseDiscoveryService;
import de.uniulm.omi.cloudiator.sword.core.base.BaseMigrationService;
import de.uniulm.omi.cloudiator.sword.core.strategy.DefaultGetStrategy;

import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * An abstract skeleton for compute modules.
 */
public abstract class AbstractComputeModule extends AbstractModule {

    /**
     * Can be extended to load own implementation of classes.
     */
    @Override protected void configure() {

        bind(new TypeLiteral<DiscoveryService<HardwareFlavor, Image, Location, VirtualMachine>>() {
        }).to(BaseDiscoveryService.class);

        bind(DeleteVirtualMachineStrategy.class).to(deleteVirtualMachineStrategy());

        bind(new TypeLiteral<GetStrategy<String, VirtualMachine>>() {
        }).to(getVirtualMachineStrategy());

        bind(new TypeLiteral<GetStrategy<String, Image>>() {
        }).to(getImageStrategy());

        bind(new TypeLiteral<GetStrategy<String, Location>>() {
        }).to(getLocationStrategy());

        bind(new TypeLiteral<GetStrategy<String, HardwareFlavor>>() {
        }).to(getHardwareFlavorStrategy());

        Multibinder<VirtualMachineMigrationStrategy> virtualMachineMigrationStrategyBinder =
            Multibinder.newSetBinder(binder(), VirtualMachineMigrationStrategy.class);

    }

    @Provides
    final CreateVirtualMachineStrategy provideCreateVirtualMachineStrategy(Injector injector) {
        return createVirtualMachineStrategy(injector);
    }

    @Provides final Optional<PublicIpService> provideFloatingIpService(Injector injector) {
        return publicIpService(injector);
    }

    @Provides final Optional<KeyPairService> provideKeyPairService(Injector injector) {
        return keyPairService(injector);
    }

    @Provides final Optional<MigrationService> provideMigrationService(Injector injector,
        Set<VirtualMachineMigrationStrategy> virtualMachineMigrationStrategies) {
        if (virtualMachineMigrationStrategies.isEmpty()) {
            return Optional.absent();
        }
        return Optional.of(injector.getInstance(BaseMigrationService.class));
    }

    @Provides @Singleton final Supplier<Set<Image>> provideImageSupplier(Injector injector) {
        return imageSupplier(injector);
    }

    @Provides @Singleton final Supplier<Set<Location>> provideLocationSupplier(Injector injector) {
        return locationSupplier(injector);
    }

    @Provides @Singleton
    final Supplier<Set<HardwareFlavor>> provideHardwareFlavorSupplier(Injector injector) {
        return hardwareFlavorSupplier(injector);
    }

    @Provides @Singleton
    final Supplier<Set<VirtualMachine>> provideVirtualMachineSupplier(Injector injector) {
        return virtualMachineSupplier(injector);
    }

    /*
     *   Providers for memoized suppliers
     *   These provides wrap the base injection
     *   with a memoized implementation.
     *   todo: make expiration configurable
     */

    @Provides @Memoized @Singleton
    final Supplier<Set<HardwareFlavor>> provideMemoizedHardwareFlavorSupplier(
        Supplier<Set<HardwareFlavor>> originalSupplier) {
        return Suppliers.memoizeWithExpiration(originalSupplier, 1L, TimeUnit.MINUTES);
    }

    @Provides @Memoized @Singleton final Supplier<Set<Location>> provideMemoizedLocationSupplier(
        Supplier<Set<Location>> originalSupplier) {
        return Suppliers.memoizeWithExpiration(originalSupplier, 1L, TimeUnit.MINUTES);
    }

    @Provides @Memoized @Singleton
    final Supplier<Set<Image>> provideMemoizedImageSupplier(Supplier<Set<Image>> originalSupplier) {
        return Suppliers.memoizeWithExpiration(originalSupplier, 1L, TimeUnit.MINUTES);
    }

    @Provides @Memoized @Singleton
    final Supplier<Set<VirtualMachine>> provideMemoizedVirtualMachineSupplier(
        Supplier<Set<VirtualMachine>> originalSupplier) {
        return Suppliers.memoizeWithExpiration(originalSupplier, 1L, TimeUnit.MINUTES);
    }

    /**
     * @return the {@link Image} {@link Supplier} to use.
     */
    protected abstract Supplier<Set<Image>> imageSupplier(Injector injector);

    /**
     * @return the {@link Location} {@link Supplier} to use.
     */
    protected abstract Supplier<Set<Location>> locationSupplier(Injector injector);

    /**
     * @return the {@link HardwareFlavor} {@link Supplier} to use.
     */
    protected abstract Supplier<Set<HardwareFlavor>> hardwareFlavorSupplier(Injector injector);

    /**
     * @return the {@link VirtualMachine} {@link Supplier} to use
     */
    protected abstract Supplier<Set<VirtualMachine>> virtualMachineSupplier(Injector injector);

    /**
     * @return the {@link CreateVirtualMachineStrategy} used for creating {@link VirtualMachine}s
     */
    protected abstract CreateVirtualMachineStrategy createVirtualMachineStrategy(Injector injector);

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

    /**
     * Extension point for registering new {@link VirtualMachineMigrationStrategy}.
     *
     * @param virtualMachineMigrationStrategyBinder the binder for registering new strategies.
     */
    protected void registerVirtualMachineMigrationStrategy(
        final Multibinder<VirtualMachineMigrationStrategy> virtualMachineMigrationStrategyBinder) {
        //intentionally left empty
    }


}
