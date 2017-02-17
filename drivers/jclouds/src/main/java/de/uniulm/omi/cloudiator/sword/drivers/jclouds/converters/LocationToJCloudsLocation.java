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

package de.uniulm.omi.cloudiator.sword.drivers.jclouds.converters;

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import de.uniulm.omi.cloudiator.domain.Location;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.JCloudsComputeClient;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.domain.AssignableLocation;

import java.util.function.Predicate;

/**
 * Created by daniel on 07.07.16.
 */
public class LocationToJCloudsLocation
    implements OneWayConverter<Location, org.jclouds.domain.Location> {

    private final JCloudsComputeClient jCloudsComputeClient;

    @Inject public LocationToJCloudsLocation(JCloudsComputeClient jCloudsComputeClient) {
        this.jCloudsComputeClient = jCloudsComputeClient;
    }

    @Override public org.jclouds.domain.Location apply(Location location) {
        return jCloudsComputeClient.listLocations().stream()
            .filter(new Predicate<AssignableLocation>() {
                @Override public boolean test(AssignableLocation assignableLocation) {
                    return assignableLocation.getId().equals(location.id());
                }
            }).findFirst().orElseThrow(() -> new IllegalStateException(
                "Could not find jclouds location with id " + location.id()));
    }
}
