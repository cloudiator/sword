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
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.MoreObjects;
import java.math.BigDecimal;

/**
 * Created by daniel on 09.03.17.
 */
public class GeoLocationImpl implements GeoLocation {

  private final String country;
  private final BigDecimal latitude;
  private final BigDecimal longitude;
  private final String city;

  GeoLocationImpl(String country, BigDecimal latitude, BigDecimal longitude, String city) {

    checkNotNull(country, "country is null");
    checkArgument(!country.isEmpty(), "country is empty");
    checkNotNull(latitude, "latitude is null");
    checkNotNull(longitude, "longitude is null");
    checkNotNull(city, "city is null");
    checkArgument(!city.isEmpty(), "city is empty");

    this.country = country;
    this.latitude = latitude;
    this.longitude = longitude;
    this.city = city;
  }

  @Override
  public String country() {
    return country;
  }

  @Override
  public BigDecimal latitude() {
    return latitude;
  }

  @Override
  public BigDecimal longitude() {
    return longitude;
  }

  @Override
  public String city() {
    return city;
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

    if (!country.equals(that.country)) {
      return false;
    }
    if (!latitude.equals(that.latitude)) {
      return false;
    }
    if (!longitude.equals(that.longitude)) {
      return false;
    }
    return city.equals(that.city);
  }

  @Override
  public int hashCode() {
    int result = country.hashCode();
    result = 31 * result + latitude.hashCode();
    result = 31 * result + longitude.hashCode();
    result = 31 * result + city.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("country", country).add("latitude", latitude)
        .add("longitude", longitude).add(city, "city").toString();
  }
}
