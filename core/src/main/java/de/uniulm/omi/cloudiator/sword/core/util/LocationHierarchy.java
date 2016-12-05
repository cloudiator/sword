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

package de.uniulm.omi.cloudiator.sword.core.util;

import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.LocationScope;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 05.12.16.
 */
public class LocationHierarchy {

    private final Location location;

    private LocationHierarchy(Location location) {
        checkNotNull(location, "location is null");
        this.location = location;
    }

    public static LocationHierarchy of(Location location) {
        return new LocationHierarchy(location);
    }

    public Location topmostLocation() {
        Location locationForIteration = location;
        while (locationForIteration.parent().isPresent()) {
            locationForIteration = locationForIteration.parent().get();
        }
        return locationForIteration;
    }

    public Optional<Location> firstParentLocationWithScope(LocationScope locationScope) {
        checkNotNull(locationScope, "locationScope is null");

        //return fast if location is already in searched scope
        if (locationScope.equals(location.locationScope())) {
            return Optional.of(location);
        }

        Location locationForIteration = location;
        while (locationForIteration.parent().isPresent()) {
            locationForIteration = location.parent().get();
            if (locationScope.equals(locationForIteration.locationScope())) {
                return Optional.of(locationForIteration);
            }
        }
        return Optional.empty();
    }

}
