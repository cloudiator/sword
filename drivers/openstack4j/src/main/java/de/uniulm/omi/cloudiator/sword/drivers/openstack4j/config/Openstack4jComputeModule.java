/*
 * Copyright (c) 2014-2016 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.config;

import com.google.common.base.Supplier;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.sword.api.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.api.domain.Image;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.api.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.api.strategy.DeleteVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.core.config.AbstractComputeModule;
import de.uniulm.omi.cloudiator.sword.core.strategy.FakeDeleteVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.core.suppliers.EmptyVirtualMachineSupplier;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.converters.*;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.domain.AvailabilityZoneInRegion;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.domain.FlavorInRegion;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.domain.ImageInRegion;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.domain.ServerInRegion;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.internal.*;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.strategy.Openstack4jCreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.strategy.OpenstackConfiguredNetworkStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.strategy.OpenstackNetworkStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.suppliers.HardwareFlavorSupplier;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.suppliers.ImageSupplier;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.suppliers.LocationSupplier;
import org.openstack4j.api.OSClient;

import java.util.Set;


/**
 * Created by daniel on 02.12.14.
 */
public class Openstack4jComputeModule extends AbstractComputeModule {

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
        return injector.getInstance(HardwareFlavorSupplier.class);
    }

    @Override
    protected Supplier<Set<VirtualMachine>> virtualMachineSupplier(Injector injector) {
        return injector.getInstance(EmptyVirtualMachineSupplier.class);
    }

    @Override
    protected CreateVirtualMachineStrategy createVirtualMachineStrategy(Injector injector) {
        return injector.getInstance(Openstack4jCreateVirtualMachineStrategy.class);
    }

    @Override
    protected DeleteVirtualMachineStrategy deleteVirtualMachineStrategy(Injector injector) {
        return injector.getInstance(FakeDeleteVirtualMachineStrategy.class);
    }

    @Override
    protected void configure() {
        super.configure();
        bind(OSClient.class).toProvider(Openstack4jClientProvider.class);
        bind(KeyStoneVersion.class).toProvider(KeyStoneVersionProvider.class).in(Singleton.class);
        bind(OsClientFactory.class).toProvider(OsClientFactoryProvider.class).in(Singleton.class);
        bind(RegionSupplier.class).toProvider(RegionSupplierProvider.class).in(Singleton.class);
        bind(OpenstackNetworkStrategy.class).to(OpenstackConfiguredNetworkStrategy.class).in(Singleton.class);
        bind(new TypeLiteral<OneWayConverter<FlavorInRegion, HardwareFlavor>>() {
        }).to(FlavorInRegionToHardwareFlavor.class).in(Singleton.class);
        bind(new TypeLiteral<OneWayConverter<ImageInRegion, Image>>() {
        }).to(ImageInRegionToImage.class).in(Singleton.class);
        bind(new TypeLiteral<OneWayConverter<AvailabilityZoneInRegion, Location>>() {
        }).to(AvailabilityZoneInRegionToLocation.class).in(Singleton.class);
        bind(new TypeLiteral<OneWayConverter<String, Location>>() {
        }).to(RegionToLocation.class).in(Singleton.class);
        bind(new TypeLiteral<OneWayConverter<ServerInRegion, VirtualMachine>>() {
        }).to(ServerInRegionToVirtualMachine.class).in(Singleton.class);
    }
}
