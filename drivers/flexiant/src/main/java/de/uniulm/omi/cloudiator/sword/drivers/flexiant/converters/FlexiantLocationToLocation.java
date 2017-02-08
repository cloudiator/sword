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

package de.uniulm.omi.cloudiator.sword.drivers.flexiant.converters;


import com.google.common.collect.ImmutableMap;
import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.flexiant.client.domain.LocationScope;
import de.uniulm.omi.cloudiator.domain.Location;
import de.uniulm.omi.cloudiator.domain.LocationBuilder;

import java.util.Map;

/**
 * Created by daniel on 05.12.14.
 */
public class FlexiantLocationToLocation
    implements OneWayConverter<de.uniulm.omi.cloudiator.flexiant.client.domain.Location, Location> {

    private final OneWayConverter<LocationScope, de.uniulm.omi.cloudiator.domain.LocationScope>
        locationScopeConverter;

    public FlexiantLocationToLocation() {
        locationScopeConverter = new FlexiantLocationScopeToLocationScope();
    }

    @Override
    public Location apply(de.uniulm.omi.cloudiator.flexiant.client.domain.Location location) {

        final boolean assignable = location.getLocationScope().equals(LocationScope.VDC);

        final LocationBuilder builder =
            LocationBuilder.newBuilder().id(location.getId()).assignable(assignable)
                .name(location.getName());
        if (location.getParent() != null) {
            builder.parent(apply(location.getParent()));
        }
        builder.scope(locationScopeConverter.apply(location.getLocationScope()));
        return builder.build();
    }

    private static class FlexiantLocationScopeToLocationScope implements
        OneWayConverter<LocationScope, de.uniulm.omi.cloudiator.domain.LocationScope> {

        private static final Map<LocationScope, de.uniulm.omi.cloudiator.domain.LocationScope>
            CONVERSION_MAP;

        static {
            final ImmutableMap.Builder<LocationScope, de.uniulm.omi.cloudiator.domain.LocationScope>
                builder = ImmutableMap.builder();
            builder.put(LocationScope.CLUSTER,
                de.uniulm.omi.cloudiator.domain.LocationScope.REGION);
            builder.put(LocationScope.VDC,
                de.uniulm.omi.cloudiator.domain.LocationScope.ZONE);
            CONVERSION_MAP = builder.build();
        }

        @Override public de.uniulm.omi.cloudiator.domain.LocationScope apply(
            LocationScope locationScope) {

            if (CONVERSION_MAP.containsKey(locationScope)) {
                return CONVERSION_MAP.get(locationScope);
            }
            throw new AssertionError(
                String.format("Location scope %s is currently not supported", locationScope));
        }
    }

}
