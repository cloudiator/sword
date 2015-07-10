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
 * Created by daniel on 19.06.15.
 */
public interface DiscoveryService<H extends HardwareFlavor, I extends Image, L extends Location, V extends VirtualMachine> {

    @Nullable H getHardwareFlavor(String id);

    @Nullable I getImage(String id);

    @Nullable L getLocation(String id);

    @Nullable V getVirtualMachine(String id);

    Iterable<H> listHardwareFlavors();

    Iterable<I> listImages();

    Iterable<L> listLocations();

    Iterable<V> listVirtualMachines();
}
