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

package de.uniulm.omi.executionware.drivers.flexiant.config;

import com.google.inject.TypeLiteral;
import de.uniulm.omi.executionware.api.converters.Converter;
import de.uniulm.omi.executionware.api.domain.HardwareFlavor;
import de.uniulm.omi.executionware.api.domain.Image;
import de.uniulm.omi.executionware.api.domain.Location;
import de.uniulm.omi.executionware.api.domain.VirtualMachine;
import de.uniulm.omi.executionware.api.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.executionware.api.strategy.DeleteVirtualMachineStrategy;
import de.uniulm.omi.executionware.api.supplier.Supplier;
import de.uniulm.omi.executionware.core.config.BaseComputeModule;
import de.uniulm.omi.executionware.drivers.flexiant.converters.FlexiantHardwareToHardwareFlavor;
import de.uniulm.omi.executionware.drivers.flexiant.converters.FlexiantImageToImage;
import de.uniulm.omi.executionware.drivers.flexiant.converters.FlexiantLocationToLocation;
import de.uniulm.omi.executionware.drivers.flexiant.converters.FlexiantServerToVirtualMachine;
import de.uniulm.omi.executionware.drivers.flexiant.strategy.FlexiantCreateVirtualMachineStrategy;
import de.uniulm.omi.executionware.drivers.flexiant.strategy.FlexiantDeleteVirtualMachineStrategy;
import de.uniulm.omi.executionware.drivers.flexiant.suppliers.HardwareSupplier;
import de.uniulm.omi.executionware.drivers.flexiant.suppliers.ImageSupplier;
import de.uniulm.omi.executionware.drivers.flexiant.suppliers.LocationSupplier;
import de.uniulm.omi.executionware.drivers.flexiant.suppliers.VirtualMachineSupplier;
import de.uniulm.omi.flexiant.domain.impl.Hardware;
import de.uniulm.omi.flexiant.domain.impl.Server;

import java.util.Set;

/**
 * Created by daniel on 02.12.14.
 */
public class FlexiantComputeModule extends BaseComputeModule {

    @Override
    protected Class<? extends Supplier<Set<? extends Image>>> getImageSupplier() {
        return ImageSupplier.class;
    }

    @Override
    protected Class<? extends Supplier<Set<? extends Location>>> getLocationSupplier() {
        return LocationSupplier.class;
    }

    @Override
    protected Class<? extends Supplier<Set<? extends HardwareFlavor>>> getHardwareFlavorSupplier() {
        return HardwareSupplier.class;
    }

    @Override
    protected Class<? extends Supplier<Set<? extends VirtualMachine>>> getVirtualMachineSupplier() {
        return VirtualMachineSupplier.class;
    }

    @Override
    protected Class<? extends CreateVirtualMachineStrategy> getCreateVirtualMachineStrategy() {
        return FlexiantCreateVirtualMachineStrategy.class;
    }

    @Override
    protected Class<? extends DeleteVirtualMachineStrategy> getDeleteVirtualMachineStrategy() {
        return FlexiantDeleteVirtualMachineStrategy.class;
    }

    @Override
    protected void configure() {
        super.configure();

        //bind the image converter
        bind(new TypeLiteral<Converter<de.uniulm.omi.flexiant.domain.impl.Image, Image>>() {
        }).to(FlexiantImageToImage.class);

        //bind the location converter
        bind(new TypeLiteral<Converter<de.uniulm.omi.flexiant.domain.impl.Location, Location>>() {
        }).to(FlexiantLocationToLocation.class);

        //bind the hardware converter
        bind(new TypeLiteral<Converter<Hardware, HardwareFlavor>>() {
        }).to(FlexiantHardwareToHardwareFlavor.class);

        //bind the virtual machine converter
        bind(new TypeLiteral<Converter<Server, VirtualMachine>>() {
        }).to(FlexiantServerToVirtualMachine.class);
    }
}
