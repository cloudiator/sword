/*
 * Copyright (c) 2014-2018 University of Ulm
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

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Supplier;
import com.google.inject.Inject;
import com.google.inject.Provider;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.domain.AvailabilityZoneInRegion;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.internal.RegionSupplier;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.openstack4j.api.OSClient;

/**
 * Created by daniel on 18.11.16.
 */
public class LocationSupplier implements Supplier<Set<Location>> {

  private final Provider<OSClient> osClient;
  private final RegionSupplier regionSupplier;
  private final OneWayConverter<AvailabilityZoneInRegion, Location> avConverter;

  @Inject
  public LocationSupplier(Provider<OSClient> osClient, RegionSupplier regionSupplier,
      OneWayConverter<AvailabilityZoneInRegion, Location> avConverter) {
    checkNotNull(avConverter);
    this.avConverter = avConverter;
    checkNotNull(regionSupplier);
    this.regionSupplier = regionSupplier;
    checkNotNull(osClient);
    this.osClient = osClient;
  }

  @Override
  public Set<Location> get() {

    Set<Location> locations = new HashSet<>();
    //add regions
    locations.addAll(regionSupplier.get());
    //add availabilityZones
    for (Location region : regionSupplier.get()) {
      locations.addAll(osClient.get().useRegion(region.id()).compute().zones().list().stream().map(
          availabilityZone -> new AvailabilityZoneInRegion(
              availabilityZone, region)).map(avConverter).collect(Collectors.toSet()));
    }
    return locations;
  }
}
