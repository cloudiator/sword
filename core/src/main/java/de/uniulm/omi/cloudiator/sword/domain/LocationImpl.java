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
import com.google.common.base.Preconditions;
import de.uniulm.omi.cloudiator.domain.LocationScope;
import java.util.Optional;
import javax.annotation.Nullable;

/**
 * Created by daniel on 03.12.14.
 */
public class LocationImpl implements Location {

  private final String id;
  private final String providerId;
  private final String name;
  private final LocationScope locationScope;
  @Nullable
  private final Location parent;
  private final boolean isAssignable;
  @Nullable
  private final GeoLocation geoLocation;

  /**
   * Constructor. For building new objects use {@link LocationBuilder}.
   *
   * @param id the id of the location (mandatory)
   * @param providerId the providerId of the location (mandatory)
   * @param name the name of the location (mandatory)
   * @param parent the parent location (optional)
   * @param isAssignable if the location is assignable (mandatory)
   * @param locationScope the scope of the location (mandatory)
   * @param geoLocation the geoLocation (optional)
   * @throws NullPointerException if a mandatory parameter is null.
   * @throws IllegalArgumentException if a mandatory string attribute is empty.
   */
  LocationImpl(String id, String providerId, String name, @Nullable Location parent,
      boolean isAssignable,
      LocationScope locationScope, @Nullable GeoLocation geoLocation) {
    checkNotNull(id, "id is null");
    checkArgument(!id.isEmpty(), "id is empty");
    this.id = id;
    checkNotNull(providerId, "providerId is null");
    checkArgument(!providerId.isEmpty(), "providerId is empty");
    this.providerId = providerId;
    checkNotNull(name, "name is null");
    checkArgument(!name.isEmpty(), "name is empty");
    this.name = name;
    this.parent = parent;
    this.isAssignable = isAssignable;
    checkNotNull(locationScope);
    this.locationScope = locationScope;
    this.geoLocation = geoLocation;

    if (parent != null) {
      Preconditions.checkArgument(locationScope.hasParent(parent.locationScope()), String
          .format(
              "LocationScope of parent location (%s) needs to be larger than the location scope of this location (%s).",
              parent.locationScope(), locationScope));
    }

  }

  @Override
  public String id() {
    return this.id;
  }

  @Override
  public String providerId() {
    return providerId;
  }

  @Override
  public String name() {
    return this.name;
  }

  @Override
  public Optional<Location> parent() {
    return Optional.ofNullable(parent);
  }

  @Override
  public Optional<GeoLocation> geoLocation() {
    return Optional.ofNullable(geoLocation);
  }

  @Override
  public LocationScope locationScope() {
    return locationScope;
  }

  @Override
  public boolean isAssignable() {
    return this.isAssignable;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    LocationImpl location = (LocationImpl) o;

    if (isAssignable != location.isAssignable) {
      return false;
    }
    if (!id.equals(location.id)) {
      return false;
    }
    if (!providerId.equals(location.providerId)) {
      return false;
    }
    if (!name.equals(location.name)) {
      return false;
    }
    if (locationScope != location.locationScope) {
      return false;
    }
    if (parent != null ? !parent.equals(location.parent) : location.parent != null) {
      return false;
    }
    return geoLocation != null ? geoLocation.equals(location.geoLocation)
        : location.geoLocation == null;
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + providerId.hashCode();
    result = 31 * result + name.hashCode();
    result = 31 * result + locationScope.hashCode();
    result = 31 * result + (parent != null ? parent.hashCode() : 0);
    result = 31 * result + (isAssignable ? 1 : 0);
    result = 31 * result + (geoLocation != null ? geoLocation.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("id", id).add("providerId", providerId())
        .add("name", name).add("parent", parent).add("isAssignable", isAssignable)
        .add("locationScope", locationScope).add("geoLocation", geoLocation).toString();
  }
}
