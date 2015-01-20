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

package de.uniulm.omi.executionware.drivers.flexiant.converters;

import de.uniulm.omi.executionware.api.converters.Converter;
import de.uniulm.omi.executionware.api.domain.Location;
import de.uniulm.omi.executionware.core.domain.builders.LocationBuilder;
import de.uniulm.omi.flexiant.domain.impl.LocationScope;

/**
 * Created by daniel on 05.12.14.
 */
public class FlexiantLocationToLocation implements Converter<de.uniulm.omi.flexiant.domain.impl.Location, Location> {

    @Override
    public Location apply(de.uniulm.omi.flexiant.domain.impl.Location location) {

        final boolean assignable = location.getLocationScope().equals(LocationScope.VDC);

        return new LocationBuilder().id(location.getId()).assignable(assignable).description(location.getName()).build();
    }
}
