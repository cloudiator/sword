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

package de.uniulm.omi.cloudiator.sword.drivers.azure.converters;

import com.microsoft.azure.management.resources.Location;
import de.uniulm.omi.cloudiator.domain.LocationScope;
import de.uniulm.omi.cloudiator.sword.domain.LocationBuilder;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import javax.annotation.Nullable;

/**
 * Created by daniel on 16.05.17.
 */
public class AzureLocationToLocation implements
    OneWayConverter<Location, de.uniulm.omi.cloudiator.sword.domain.Location> {

  @Override
  public de.uniulm.omi.cloudiator.sword.domain.Location apply(@Nullable Location location) {
    return LocationBuilder.newBuilder().id(location.name()).providerId(location.name())
        .assignable(true)
        .name(location.displayName()).scope(LocationScope.REGION).build();
  }
}
