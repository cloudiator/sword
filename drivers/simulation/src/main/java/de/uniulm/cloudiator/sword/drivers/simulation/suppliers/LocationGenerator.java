/*
 * Copyright (c) 2014-2019 University of Ulm
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

package de.uniulm.cloudiator.sword.drivers.simulation.suppliers;

import com.google.common.base.Supplier;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import de.uniulm.cloudiator.sword.drivers.simulation.config.SimulationConstants.Properties;
import de.uniulm.omi.cloudiator.domain.LocationScope;
import de.uniulm.omi.cloudiator.sword.domain.Cloud;
import de.uniulm.omi.cloudiator.sword.domain.GeoLocationBuilder;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.domain.LocationBuilder;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocationGenerator implements Supplier<Set<Location>> {

  @Named(Properties.LOCATION_SIZE)
  @Inject(optional = true)
  int locationSize = 10;

  private static final Logger LOGGER = LoggerFactory.getLogger(LocationGenerator.class);

  private final Cloud cloud;

  @Inject
  public LocationGenerator(Cloud cloud) {
    this.cloud = cloud;
  }

  private static final Set<LocationTemplate> TEMPLATES = new LinkedHashSet<LocationTemplate>() {{
    add(LocationTemplate.of("Dublin", "Ireland", 53.349804, 53.349804));
    add(LocationTemplate.of("Frankfurt", "Germany", 50.110924, 8.682127));
    add(LocationTemplate.of("Sydney", "Australia", -33.868820, 151.209290));
    add(LocationTemplate.of("Mumbai", "India", 19.075983, 72.877655));
    add(LocationTemplate.of("Portland", "US", 45.512230, -122.658722));
    add(LocationTemplate.of("Montreal", "US", 45.501690, -73.567253));
    add(LocationTemplate.of("Sao Paulo", "Brazil", -23.550520, -46.633308));
    add(LocationTemplate.of("Cape Town", "South Africa", -33.924870, 18.424055));
    add(LocationTemplate.of("Beijing", "US", 39.904202, 116.407394));
    add(LocationTemplate.of("Milan", "Italy", 45.464203, 9.189982));
  }};

  private static class LocationTemplate {

    private final String city;
    private final String country;
    private final double latitude;
    private final double longitude;

    private LocationTemplate(String city, String country, double latitude, double longitude) {
      this.city = city;
      this.country = country;
      this.latitude = latitude;
      this.longitude = longitude;
    }

    private static LocationTemplate of(String city, String country, double latitude,
        double longitude) {
      return new LocationTemplate(city, country, latitude, longitude);
    }

    public String generateId() {
      return city + "-" + country;
    }

    public String getCity() {
      return city;
    }

    public String getCountry() {
      return country;
    }

    public double getLatitude() {
      return latitude;
    }

    public double getLongitude() {
      return longitude;
    }
  }


  @Override
  public Set<Location> get() {

    if (locationSize <= 0) {
      throw new IllegalArgumentException(String
          .format("Location size was set to value smaller or equal 0. Check your %s setting.",
              Properties.LOCATION_SIZE));
    }

    if (locationSize > 10) {
      LOGGER.warn(
          "Location size is larger then 10 which is the maximum location size. Will default to 10.");
      locationSize = 10;
    }

    Set<Location> locations = Sets.newHashSetWithExpectedSize(locationSize);

    final Iterator<LocationTemplate> iterator = TEMPLATES.iterator();
    for (int i = 1; i <= locationSize; i++) {
      final LocationTemplate next = iterator.next();
      final Location location = LocationBuilder.newBuilder().id(next.generateId())
          .name(next.generateId()).geoLocation(
              GeoLocationBuilder.newBuilder().city(next.getCity()).country(next.getCountry())
                  .latitude(BigDecimal.valueOf(next.getLatitude()))
                  .longitude(BigDecimal.valueOf(next.getLongitude()))
                  .build())
          .providerId(next.generateId()).scope(LocationScope.REGION).assignable(true).build();
      locations.add(location);
    }

    return locations;
  }
}
