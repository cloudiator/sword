/*
 * Copyright (c) 2014-2017 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.drivers.azure.suppliers;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Supplier;
import com.google.inject.Inject;
import com.microsoft.azure.management.Azure;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by daniel on 16.05.17.
 */
public class LocationSupplier implements Supplier<Set<Location>> {

  private final Azure azure;
  private final OneWayConverter<com.microsoft.azure.management.resources.Location, Location> converter;

  @Inject
  public LocationSupplier(Azure azure,
      OneWayConverter<com.microsoft.azure.management.resources.Location, Location> converter) {
    checkNotNull(converter, "converter is null");
    this.converter = converter;
    checkNotNull(azure, "azure is null");
    this.azure = azure;
  }

  @Override
  public Set<Location> get() {
    return azure.getCurrentSubscription().listLocations().stream().map(
        converter::apply).collect(Collectors.toSet());
  }
}
