/*
 * Copyright (c) 2014-2015 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.core.base;

import com.google.common.base.Supplier;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.api.annotations.Memoized;
import de.uniulm.omi.cloudiator.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.domain.Image;
import de.uniulm.omi.cloudiator.domain.Location;
import de.uniulm.omi.cloudiator.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.api.service.DiscoveryService;
import de.uniulm.omi.cloudiator.sword.api.strategy.GetStrategy;

import javax.annotation.Nullable;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 25.09.15.
 */
public class BaseDiscoveryService
    implements DiscoveryService {

    private final Supplier<Set<Image>> imageSupplier;
    private final Supplier<Set<Location>> locationSupplier;
    private final Supplier<Set<HardwareFlavor>> hardwareFlavorSupplier;
    private final Supplier<Set<VirtualMachine>> virtualMachineSupplier;
    private final GetStrategy<String, Image> imageGetStrategy;
    private final GetStrategy<String, Location> locationGetStrategy;
    private final GetStrategy<String, HardwareFlavor> hardwareFlavorGetStrategy;
    private final GetStrategy<String, VirtualMachine> virtualMachineGetStrategy;

    @Inject public BaseDiscoveryService(@Memoized Supplier<Set<Image>> imageSupplier,
        @Memoized Supplier<Set<Location>> locationSupplier,
        @Memoized Supplier<Set<HardwareFlavor>> hardwareFlavorSupplier,
        Supplier<Set<VirtualMachine>> virtualMachineSupplier,
        GetStrategy<String, Image> imageGetStrategy,
        GetStrategy<String, Location> locationGetStrategy,
        GetStrategy<String, HardwareFlavor> hardwareFlavorGetStrategy,
        GetStrategy<String, VirtualMachine> virtualMachineGetStrategy) {

        checkNotNull(imageSupplier);
        checkNotNull(locationSupplier);
        checkNotNull(hardwareFlavorSupplier);
        checkNotNull(virtualMachineSupplier);
        checkNotNull(imageGetStrategy);
        checkNotNull(locationGetStrategy);
        checkNotNull(hardwareFlavorGetStrategy);
        checkNotNull(virtualMachineGetStrategy);


        this.imageSupplier = imageSupplier;
        this.locationSupplier = locationSupplier;
        this.hardwareFlavorSupplier = hardwareFlavorSupplier;
        this.virtualMachineSupplier = virtualMachineSupplier;
        this.imageGetStrategy = imageGetStrategy;
        this.locationGetStrategy = locationGetStrategy;
        this.hardwareFlavorGetStrategy = hardwareFlavorGetStrategy;
        this.virtualMachineGetStrategy = virtualMachineGetStrategy;
    }

    @Override @Nullable public Image getImage(String id) {
        checkNotNull(id);
        checkArgument(!id.isEmpty());
        return this.imageGetStrategy.get(id);
    }

    @Override @Nullable public VirtualMachine getVirtualMachine(String id) {
        checkNotNull(id);
        checkArgument(!id.isEmpty());
        return this.virtualMachineGetStrategy.get(id);
    }

    @Override @Nullable public Location getLocation(String id) {
        checkNotNull(id);
        checkArgument(!id.isEmpty());
        return locationGetStrategy.get(id);
    }

    @Override @Nullable public HardwareFlavor getHardwareFlavor(String id) {
        checkNotNull(id);
        checkArgument(!id.isEmpty());
        return hardwareFlavorGetStrategy.get(id);
    }

    @Override public Iterable<HardwareFlavor> listHardwareFlavors() {
        return hardwareFlavorSupplier.get();
    }

    @Override public Iterable<Image> listImages() {
        return imageSupplier.get();
    }

    @Override public Iterable<Location> listLocations() {
        return locationSupplier.get();
    }

    @Override public Iterable<VirtualMachine> listVirtualMachines() {
        return virtualMachineSupplier.get();
    }
}
