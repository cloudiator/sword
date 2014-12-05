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

package de.uniulm.omi.executionware.core.base;

import de.uniulm.omi.executionware.api.domain.HardwareFlavor;
import de.uniulm.omi.executionware.api.domain.Image;
import de.uniulm.omi.executionware.api.domain.Location;
import de.uniulm.omi.executionware.api.domain.VirtualMachine;
import de.uniulm.omi.executionware.api.service.ComputeService;
import de.uniulm.omi.executionware.api.supplier.Supplier;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 02.12.14.
 */
public class BaseComputeService implements ComputeService {

    private final Supplier<Set<? extends Image>> imageSupplier;
    private final Supplier<Set<? extends Location>> locationSupplier;
    private final Supplier<Set<? extends HardwareFlavor>> hardwareFlavorSupplier;

    public BaseComputeService(Supplier<Set<? extends Image>> imageSupplier, Supplier<Set<? extends Location>> locationSupplier, Supplier<Set<? extends HardwareFlavor>> hardwareFlavorSupplier) {
        checkNotNull(imageSupplier);
        checkNotNull(locationSupplier);
        checkNotNull(hardwareFlavorSupplier);
        this.imageSupplier = imageSupplier;
        this.locationSupplier = locationSupplier;
        this.hardwareFlavorSupplier = hardwareFlavorSupplier;
    }

    @Override
    public Image getImage() {
        return null;
    }

    @Override
    public VirtualMachine getVirtualMachine() {
        return null;
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public HardwareFlavor getFlavor() {
        return null;
    }

    @Override
    public Iterable<? extends HardwareFlavor> listHardwareFlavors() {
        return this.hardwareFlavorSupplier.get();
    }

    @Override
    public Iterable<? extends Image> listImages() {
        return this.imageSupplier.get();
    }

    @Override
    public Iterable<? extends Location> listLocations() {
        return this.locationSupplier.get();
    }

    @Override
    public Iterable<? extends VirtualMachine> listVirtualMachines() {
        return null;
    }

    @Override
    public void destroyVirtualMachine() {

    }
}
