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

package de.uniulm.omi.cloudiator.sword.drivers.jclouds.converters;



import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.core.domain.LocationBuilder;

/**
 * Created by daniel on 03.12.14.
 */
public class JCloudsLocationToLocation
    implements OneWayConverter<org.jclouds.domain.Location, Location> {

    @Override public Location apply(org.jclouds.domain.Location location) {

        if (location == null) {
            return null;
        }

        final LocationBuilder builder =
            LocationBuilder.newBuilder().id(location.getId()).name(location.getDescription());

        builder.assignable(isAssignable(location));

        if (location.getParent() != null) {
            final Location parent = apply(location.getParent());
            // we only set assignable locations as parent
            // as otherwise the parents are not included in the
            // LocationsSupplier, what may cause problems on client side
            // todo: maybe include non assignable locations into list method?
            if (parent.isAssignable()) {
                builder.parent(parent);
            }
        }
        return builder.build();
    }

    private boolean isAssignable(org.jclouds.domain.Location location) {
        switch (location.getScope()) {
            case PROVIDER:
                return false;
            case REGION:
                return true;
            case ZONE:
                return true;
            default:
                throw new AssertionError(
                    "Openstack jclouds driver is suspected to only return locations with scope provider, region or zone");
        }
    }

}
