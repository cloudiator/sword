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

package de.uniulm.omi.cloudiator.sword.api.service;

import de.uniulm.omi.cloudiator.sword.api.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.api.domain.Image;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;

import javax.annotation.Nullable;

/**
 * Provides methods to either directly fetch
 * resources (HardwareFlavor, Image, Location and Virtual Machine)
 * stored at the cloud provider or
 * to fetch all resources of a specific type.
 *
 * @param <H> the type of the {@link HardwareFlavor}
 * @param <I> the type of the {@link Image}
 * @param <L> the type of the {@link Location}
 * @param <V> the type of the {@link VirtualMachine}
 */
public interface DiscoveryService<H extends HardwareFlavor, I extends Image, L extends Location, V extends VirtualMachine> {

    /**
     * Retrieves the hardware flavor with the specified id.
     *
     * @param id mandatory id for the hardware flavor.
     * @return the hardware flavor or null if not found.
     * @throws NullPointerException     if the id is null.
     * @throws IllegalArgumentException if the id is empty.
     */
    @Nullable H getHardwareFlavor(String id);

    /**
     * Retrieves the image with the specified id.
     *
     * @param id mandatory id for the image.
     * @return the image or null if not found.
     * @throws NullPointerException     if the id is null.
     * @throws IllegalArgumentException if the id is empty.
     */
    @Nullable I getImage(String id);

    /**
     * Retrieves the location with the specified id.
     *
     * @param id mandatory id for the location.
     * @return the location or null if not found.
     * @throws NullPointerException     if the id is null.
     * @throws IllegalArgumentException if the id is empty.
     */
    @Nullable L getLocation(String id);

    /**
     * Retrieves the virtual machine with the specified id.
     *
     * @param id mandatory id for the virtual machine.
     * @return the virtual machine or null if not found.
     * @throws NullPointerException     if the id is null.
     * @throws IllegalArgumentException if the id is empty.
     */
    @Nullable V getVirtualMachine(String id);

    /**
     * Retrieves an {@link Iterable} of the hardware flavors offered by the cloud provider.
     *
     * @return the hardware flavors offered.
     */
    Iterable<H> listHardwareFlavors();

    /**
     * Retrieves an {@link Iterable} of the images offered by the cloud provider.
     *
     * @return the images offered.
     */
    Iterable<I> listImages();

    /**
     * Retrieves an {@link Iterable} of the locations offered by the cloud provider.
     *
     * @return the locations offered.
     */
    Iterable<L> listLocations();

    /**
     * Retrieves an {@link Iterable} of the virtual machines offered by the cloud provider.
     *
     * @return the virtual machines offered.
     */
    Iterable<V> listVirtualMachines();
}
