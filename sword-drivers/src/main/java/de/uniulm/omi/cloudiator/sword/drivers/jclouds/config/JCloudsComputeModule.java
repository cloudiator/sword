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

import com.google.inject.TypeLiteral;
import de.uniulm.omi.cloudiator.sword.api.converters.OneWayConverter;
import de.uniulm.omi.cloudiator.sword.api.domain.*;
import de.uniulm.omi.cloudiator.sword.api.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.api.strategy.DeleteVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.api.supplier.Supplier;
import de.uniulm.omi.cloudiator.sword.core.config.AbstractComputeModule;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.converters.*;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.strategy.JCloudsCreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.strategy.JCloudsDeleteVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.suppliers.HardwareSupplier;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.suppliers.ImageSupplier;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.suppliers.LocationSupplier;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.suppliers.VirtualMachineSupplier;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.domain.LoginCredentials;

import java.util.Set;

/**
 * Created by daniel on 02.12.14.
 */
public abstract class JCloudsComputeModule extends AbstractComputeModule {

    @Override protected Class<? extends Supplier<Set<Image>>> imageSupplier() {
        return ImageSupplier.class;
    }

    @Override protected Class<? extends Supplier<Set<Location>>> locationSupplier() {
        return LocationSupplier.class;
    }

    @Override protected Class<? extends Supplier<Set<HardwareFlavor>>> hardwareFlavorSupplier() {
        return HardwareSupplier.class;
    }

    @Override protected Class<? extends Supplier<Set<VirtualMachine>>> virtualMachineSupplier() {
        return VirtualMachineSupplier.class;
    }

    @Override
    protected Class<? extends CreateVirtualMachineStrategy> createVirtualMachineStrategy() {
        return JCloudsCreateVirtualMachineStrategy.class;
    }

    @Override
    protected Class<? extends DeleteVirtualMachineStrategy> deleteVirtualMachineStrategy() {
        return JCloudsDeleteVirtualMachineStrategy.class;
    }

    protected abstract Class<? extends OneWayConverter<TemplateOptions, org.jclouds.compute.options.TemplateOptions>> templateOptionsConverter();

    @Override protected void configure() {
        super.configure();

        //bind the image converter
        bind(new TypeLiteral<OneWayConverter<org.jclouds.compute.domain.Image, Image>>() {
        }).to(JCloudsImageToImage.class);

        //bind the location converter
        bind(new TypeLiteral<OneWayConverter<org.jclouds.domain.Location, Location>>() {
        }).to(JCloudsLocationToLocation.class);

        //bind the hardware converter
        bind(new TypeLiteral<OneWayConverter<Hardware, HardwareFlavor>>() {
        }).to(JCloudsHardwareToHardwareFlavor.class);

        //bind the virtual machine converter
        bind(new TypeLiteral<OneWayConverter<ComputeMetadata, VirtualMachine>>() {
        }).to(JCloudsComputeMetadataToVirtualMachine.class);

        //bind the login credential converter
        bind(new TypeLiteral<OneWayConverter<LoginCredentials, LoginCredential>>() {
        }).to(JCloudsLoginCredentialsToLoginCredential.class);

        //bind the template options converter
        bind(
            new TypeLiteral<OneWayConverter<TemplateOptions, org.jclouds.compute.options.TemplateOptions>>() {
            }).to(templateOptionsConverter());
    }
}
