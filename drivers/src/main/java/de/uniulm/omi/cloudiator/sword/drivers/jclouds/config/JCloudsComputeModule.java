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

package de.uniulm.omi.cloudiator.sword.drivers.jclouds.config;

import com.google.common.base.Supplier;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.common.os.OperatingSystemFamily;
import de.uniulm.omi.cloudiator.sword.api.domain.*;
import de.uniulm.omi.cloudiator.sword.api.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.api.strategy.DeleteVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.core.config.AbstractComputeModule;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.BaseJCloudsViewFactory;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.JCloudsComputeClient;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.JCloudsComputeClientImpl;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.JCloudsViewFactory;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.converters.*;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.strategy.JCloudsCreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.strategy.JCloudsDeleteVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.suppliers.HardwareSupplier;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.suppliers.ImageSupplier;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.suppliers.LocationSupplier;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.suppliers.VirtualMachineSupplier;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.OperatingSystem;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.domain.LoginCredentials;

import java.util.Set;


/**
 * An abstract compute module for cloud providers that are supported using
 * jclouds.
 */
public abstract class JCloudsComputeModule extends AbstractComputeModule {

    @Provides private JCloudsComputeClient provideJCloudsComputeClient(Injector injector) {
        return overrideComputeClient(injector,
            injector.getInstance(JCloudsComputeClientImpl.class));
    }

    protected JCloudsComputeClient overrideComputeClient(Injector injector,
        JCloudsComputeClient originalComputeClient) {
        return originalComputeClient;
    }

    @Override protected final Supplier<Set<Image>> imageSupplier(Injector injector) {
        return overrideImageSupplier(injector, injector.getInstance(ImageSupplier.class));
    }

    /**
     * Allows jclouds submodules to override the image supplier
     */
    protected Supplier<Set<Image>> overrideImageSupplier(Injector injector,
        Supplier<Set<Image>> originalSupplier) {
        return originalSupplier;
    }

    @Override protected final Supplier<Set<Location>> locationSupplier(Injector injector) {
        return overrideLocationSupplier(injector, injector.getInstance(LocationSupplier.class));
    }

    /**
     * Allows jclouds submodules to override the location supplier
     */
    protected Supplier<Set<Location>> overrideLocationSupplier(Injector injector,
        Supplier<Set<Location>> originalSupplier) {
        return originalSupplier;
    }

    @Override
    protected final Supplier<Set<HardwareFlavor>> hardwareFlavorSupplier(Injector injector) {
        return overrideHardwareFlavorSupplier(injector,
            injector.getInstance(HardwareSupplier.class));
    }

    /**
     * Allows jclouds submodules to override the hardware supplier
     */
    protected Supplier<Set<HardwareFlavor>> overrideHardwareFlavorSupplier(Injector injector,
        Supplier<Set<HardwareFlavor>> originalSupplier) {
        return originalSupplier;
    }

    @Override
    protected final Supplier<Set<VirtualMachine>> virtualMachineSupplier(Injector injector) {
        return overrideVirtualMachineSupplier(injector,
            injector.getInstance(VirtualMachineSupplier.class));
    }

    /**
     * Allows jclouds submodules to override the VirtualMachine supplier
     */
    protected Supplier<Set<VirtualMachine>> overrideVirtualMachineSupplier(Injector injector,
        Supplier<Set<VirtualMachine>> originalSupplier) {
        return originalSupplier;
    }

    @Override
    protected final CreateVirtualMachineStrategy createVirtualMachineStrategy(Injector injector) {
        return overrideCreateVirtualMachineStrategy(injector,
            injector.getInstance(JCloudsCreateVirtualMachineStrategy.class));
    }

    /**
     * Allow jclouds submodules to ovveride the create virtual machine strategy
     */
    protected CreateVirtualMachineStrategy overrideCreateVirtualMachineStrategy(Injector injector,
        CreateVirtualMachineStrategy original) {
        return original;
    }


    @Override
    protected Class<? extends DeleteVirtualMachineStrategy> deleteVirtualMachineStrategy() {
        return JCloudsDeleteVirtualMachineStrategy.class;
    }

    /**
     * Extension point for the virtual machine converter.
     *
     * @return a converter for converting the jclouds compute metadata to virtual machines.
     */
    protected Class<? extends OneWayConverter<ComputeMetadata, VirtualMachine>> virtualMachineConverter() {
        return JCloudsComputeMetadataToVirtualMachine.class;
    }

    protected abstract Class<? extends OneWayConverter<TemplateOptions, org.jclouds.compute.options.TemplateOptions>> templateOptionsConverter();

    @Override protected void configure() {
        super.configure();

        //bind the image converter
        bind(new TypeLiteral<OneWayConverter<org.jclouds.compute.domain.Image, Image>>() {
        }).to(JCloudsImageToImage.class);

        //bind the operating system converter
        bind(
            new TypeLiteral<OneWayConverter<OperatingSystem, de.uniulm.omi.cloudiator.common.os.OperatingSystem>>() {
            }).to(JCloudsOperatingSystemConverter.class);

        //bind the operating system family converter
        bind(new TypeLiteral<OneWayConverter<OsFamily, OperatingSystemFamily>>() {
        }).to(JCloudsOperatingSystemFamilyConverter.class);

        //bind the location converter
        bind(new TypeLiteral<OneWayConverter<org.jclouds.domain.Location, Location>>() {
        }).to(JCloudsLocationToLocation.class);

        //bind the hardware converter
        bind(new TypeLiteral<OneWayConverter<Hardware, HardwareFlavor>>() {
        }).to(JCloudsHardwareToHardwareFlavor.class);

        //bind the virtual machine converter
        bind(new TypeLiteral<OneWayConverter<ComputeMetadata, VirtualMachine>>() {
        }).to(virtualMachineConverter());

        //bind the login credential converter
        bind(new TypeLiteral<OneWayConverter<LoginCredentials, LoginCredential>>() {
        }).to(JCloudsLoginCredentialsToLoginCredential.class);

        //bind the template options converter
        bind(
            new TypeLiteral<OneWayConverter<TemplateOptions, org.jclouds.compute.options.TemplateOptions>>() {
            }).to(templateOptionsConverter());

        //bind the view factory
        bind(JCloudsViewFactory.class).to(BaseJCloudsViewFactory.class);
    }
}
