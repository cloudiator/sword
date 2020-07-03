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

package de.uniulm.omi.cloudiator.sword.drivers.oktawave.suppliers;

import com.google.common.base.Supplier;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.oktawave.api.client.ApiException;
import com.oktawave.api.client.api.SubregionsApi;
import com.oktawave.api.client.model.Subregion;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.properties.Constants;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class LocationSupplier implements Supplier<Set<Location>> {

  private static Logger LOGGER = LoggerFactory.getLogger(LocationSupplier.class);

  private final SubregionsApi subregionsApi;
  private final OneWayConverter<Subregion, Location> converter;

  @Inject(optional = true)
  @Named(Constants.SWORD_REGIONS)
  private String regionFilter = null;
  private Set<String> allowedRegions = null;

  @Inject
  public LocationSupplier(SubregionsApi subregionsApi, OneWayConverter<Subregion, Location> converter) {
      this.converter = checkNotNull(converter, "converter is null");
    this.subregionsApi = checkNotNull(subregionsApi, "subregionsApi is null");
  }

  private Set<String> getAllowedRegions() {
    if (regionFilter != null && allowedRegions == null) {
      allowedRegions = Arrays.stream(regionFilter.split("[,;]")).collect(Collectors.toSet());
    }
    return allowedRegions;
  }

  @Override
  public Set<Location> get() {

    try {
      Set<String> filter = getAllowedRegions();
      return subregionsApi.subregionsGet(null)
              .getItems()
              .stream()
              .filter(Subregion::isIsActive)
              .filter(subregion -> filter == null || filter.contains(subregion.getName()))
              .map(converter)
              .collect(Collectors.toSet());
    } catch (ApiException e) {
      LOGGER.error("Could not get Subregion from Oktawave", e);
    }

    return new HashSet<>();
  }
}
