/*
 * Copyright (c) 2014 University of Ulm
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

package de.uniulm.omi.executionware.drivers.jclouds.config;

import com.google.inject.TypeLiteral;
import de.uniulm.omi.executionware.api.converters.Converter;
import de.uniulm.omi.executionware.api.domain.HardwareFlavor;
import de.uniulm.omi.executionware.api.domain.Image;
import de.uniulm.omi.executionware.api.domain.Location;
import de.uniulm.omi.executionware.api.domain.VirtualMachine;
import de.uniulm.omi.executionware.api.supplier.Supplier;
import de.uniulm.omi.executionware.core.config.BaseComputeModule;
import de.uniulm.omi.executionware.drivers.jclouds.converters.JCloudsComputeMetadataToVirtualMachine;
import de.uniulm.omi.executionware.drivers.jclouds.converters.JCloudsHardwareToHardwareFlavor;
import de.uniulm.omi.executionware.drivers.jclouds.converters.JCloudsImageToImage;
import de.uniulm.omi.executionware.drivers.jclouds.converters.JCloudsLocationToLocation;
import de.uniulm.omi.executionware.drivers.jclouds.suppliers.HardwareSupplier;
import de.uniulm.omi.executionware.drivers.jclouds.suppliers.ImageSupplier;
import de.uniulm.omi.executionware.drivers.jclouds.suppliers.LocationSupplier;
import de.uniulm.omi.executionware.drivers.jclouds.suppliers.VirtualMachineSupplier;
import org.jclouds.compute.domain.ComputeMetadata;

import java.util.Set;

/**
 * Created by daniel on 02.12.14.
 */
public class JCloudsComputeModule extends BaseComputeModule {

    @Override
    public Class<? extends Supplier<Set<? extends Image>>> getImageSupplier() {
        return ImageSupplier.class;
    }

    @Override
    public Class<? extends Supplier<Set<? extends Location>>> getLocationSupplier() {
        return LocationSupplier.class;
    }

    @Override
    public Class<? extends Supplier<Set<? extends HardwareFlavor>>> getHardwareFlavorSupplier() {
        return HardwareSupplier.class;
    }

    @Override
    public Class<? extends Supplier<Set<? extends VirtualMachine>>> getVirtualMachineSupplier() {
        return VirtualMachineSupplier.class;
    }

    @Override
    protected void configure() {
        super.configure();

        //bind the image converter
        bind(new TypeLiteral<Converter<org.jclouds.compute.domain.Image, Image>>() {
        }).to(JCloudsImageToImage.class);

        //bind the location converter
        bind(new TypeLiteral<Converter<org.jclouds.domain.Location, Location>>() {
        }).to(JCloudsLocationToLocation.class);

        //bind the hardware converter
        bind(new TypeLiteral<Converter<org.jclouds.compute.domain.Hardware, HardwareFlavor>>() {
        }).to(JCloudsHardwareToHardwareFlavor.class);

        //bind the virtual machine converter
        bind(new TypeLiteral<Converter<ComputeMetadata, VirtualMachine>>() {
        }).to(JCloudsComputeMetadataToVirtualMachine.class);

    }
}
