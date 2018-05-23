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

package de.uniulm.omi.cloudiator.sword.domain;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.MoreObjects;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;

/**
 * Created by daniel on 09.03.17.
 */
public class GeoLocationImpl implements GeoLocation {

  @Nullable
  private final String country;
  @Nullable
  private final BigDecimal latitude;
  @Nullable
  private final BigDecimal longitude;
  @Nullable
  private final String city;

  GeoLocationImpl(@Nullable String country, @Nullable BigDecimal latitude,
      @Nullable BigDecimal longitude, @Nullable String city) {

    if (country != null) {
      checkArgument(!country.isEmpty(), "country is empty");
    }

    if (city != null) {
      checkArgument(!city.isEmpty(), "city is empty");
    }

    this.country = country;
    this.latitude = latitude;
    this.longitude = longitude;
    this.city = city;
  }

  @Override
  public Optional<String> country() {
    return Optional.ofNullable(country);
  }

  @Override
  public Optional<BigDecimal> latitude() {
    return Optional.ofNullable(latitude);
  }

  @Override
  public Optional<BigDecimal> longitude() {
    return Optional.ofNullable(longitude);
  }

  @Override
  public Optional<String> city() {
    return Optional.ofNullable(city);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GeoLocationImpl that = (GeoLocationImpl) o;
    return Objects.equals(country, that.country) &&
        Objects.equals(latitude, that.latitude) &&
        Objects.equals(longitude, that.longitude) &&
        Objects.equals(city, that.city);
  }

  @Override
  public int hashCode() {
    return Objects.hash(country, latitude, longitude, city);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("country", country).add("latitude", latitude)
        .add("longitude", longitude).add("city", city).toString();
  }
}
