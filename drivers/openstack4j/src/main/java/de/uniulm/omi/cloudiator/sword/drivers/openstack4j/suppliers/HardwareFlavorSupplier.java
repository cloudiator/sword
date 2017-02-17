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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.suppliers;

import com.google.common.base.Supplier;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import de.uniulm.omi.cloudiator.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.domain.Location;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.domain.FlavorInRegion;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.internal.RegionSupplier;
import org.openstack4j.api.OSClient;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 14.11.16.
 */
public class HardwareFlavorSupplier implements Supplier<Set<HardwareFlavor>> {

    private final OSClient osClient;
    private final OneWayConverter<FlavorInRegion, HardwareFlavor> converter;
    private final RegionSupplier regionSupplier;

    @Inject
    public HardwareFlavorSupplier(OSClient osClient,
                                  OneWayConverter<FlavorInRegion, HardwareFlavor> converter, RegionSupplier regionSupplier) {

        checkNotNull(osClient, "osClient is null");
        checkNotNull(converter, "converter is null");
        checkNotNull(regionSupplier, "regionSupplier is null");

        this.osClient = osClient;
        this.converter = converter;
        this.regionSupplier = regionSupplier;
    }

    @Override
    public Set<HardwareFlavor> get() {

        Set<HardwareFlavor> hardwareFlavors = new HashSet<>();

        for (Location region : regionSupplier.get()) {
            hardwareFlavors.addAll(
                    osClient.useRegion(region.id()).compute().flavors().list().stream().map(
                            flavor -> new FlavorInRegion(flavor, region))
                            .map(converter).collect(Collectors.toSet()));
        }

        return hardwareFlavors;
    }
}
