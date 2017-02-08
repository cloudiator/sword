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

package de.uniulm.omi.cloudiator.sword.drivers.flexiant.suppliers;

import com.google.common.base.Supplier;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.domain.Location;
import de.uniulm.omi.cloudiator.sword.drivers.flexiant.FlexiantComputeClient;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by daniel on 05.12.14.
 */
public class LocationSupplier implements Supplier<Set<Location>> {

    private final FlexiantComputeClient flexiantComputeClient;
    private final OneWayConverter<de.uniulm.omi.cloudiator.flexiant.client.domain.Location, Location>
        locationConverter;

    @Inject public LocationSupplier(FlexiantComputeClient flexiantComputeClient,
        OneWayConverter<de.uniulm.omi.cloudiator.flexiant.client.domain.Location, Location> locationConverter) {
        this.flexiantComputeClient = flexiantComputeClient;
        this.locationConverter = locationConverter;
    }

    @Override public Set<Location> get() {
        return flexiantComputeClient.listLocations().stream().map(locationConverter::apply)
            .collect(Collectors.toSet());
    }
}
