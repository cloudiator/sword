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
import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.internal.RegionSupplier;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.domain.AvailabilityZoneInRegion;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.ext.AvailabilityZone;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 18.11.16.
 */
public class LocationSupplier implements Supplier<Set<Location>> {

    private final OSClient osClient;
    private final RegionSupplier regionSupplier;
    private final OneWayConverter<AvailabilityZoneInRegion, Location> avConverter;

    @Inject public LocationSupplier(OSClient osClient, RegionSupplier regionSupplier,
        OneWayConverter<AvailabilityZoneInRegion, Location> avConverter) {
        checkNotNull(avConverter);
        this.avConverter = avConverter;
        checkNotNull(regionSupplier);
        this.regionSupplier = regionSupplier;
        checkNotNull(osClient);
        this.osClient = osClient;
    }

    @Override public Set<Location> get() {

        Set<Location> locations = new HashSet<>();
        //add regions
        locations.addAll(regionSupplier.get());
        //add availabilityZones
        for (Location region : regionSupplier.get()) {
            locations.addAll(osClient.useRegion(region.id()).compute().zones().list().stream().map(
                (Function<AvailabilityZone, AvailabilityZoneInRegion>) availabilityZone -> new AvailabilityZoneInRegion(
                    availabilityZone, region)).map(avConverter).collect(Collectors.toSet()));
        }
        return locations;
    }
}
