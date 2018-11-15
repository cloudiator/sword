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

package de.uniulm.omi.cloudiator.sword.domain;

import static com.google.common.base.Preconditions.checkNotNull;

import de.uniulm.omi.cloudiator.domain.LocationScope;
import java.util.HashMap;
import java.util.Map;

/**
 * A builder for immutable {@link Location} objects.
 */
public class LocationBuilder {

  private String id;
  private String providerId;
  private String name;
  private Location parent;
  private boolean isAssignable;
  private LocationScope locationScope;
  private GeoLocation geoLocation;
  private Map<String, String> tags = new HashMap<>();

  /**
   * Use LocationBuilder::newBuilder to create builder objects.
   */
  private LocationBuilder() {
    //intentionally empty
  }

  private LocationBuilder(Location location) {
    id = location.id();
    providerId = location.providerId();
    name = location.name();
    parent = location.parent().orElse(null);
    isAssignable = location.isAssignable();
    locationScope = location.locationScope();
    geoLocation = location.geoLocation().orElse(null);
    tags = new HashMap<>(location.tags());
  }

  /**
   * @return creates a new builder object.
   */
  public static LocationBuilder newBuilder() {
    return new LocationBuilder();
  }

  public static LocationBuilder of(Location location) {
    checkNotNull(location, "location is null");
    return new LocationBuilder(location);
  }

  /**
   * Builds the location.
   *
   * @return the location.
   */
  public Location build() {
    return new LocationImpl(id, providerId, name, parent, isAssignable, locationScope, geoLocation,
        tags);
  }

  /**
   * @param id unique id of the location.
   * @return fluent interface.
   */
  public LocationBuilder id(String id) {
    this.id = id;
    return this;
  }

  /**
   * @param key key of the tag
   * @param value value of the tag
   * @return fluent interface
   */
  public LocationBuilder addTag(String key, String value) {
    this.tags.put(key, value);
    return this;
  }

  /**
   * @param name the (human-readable) name of the location.
   * @return fluent interface.
   */
  public LocationBuilder name(final String name) {
    this.name = name;
    return this;
  }

  /**
   * @param isAssignable if the location is assignable to a virtual machine.
   * @return fluent interface.
   */
  public LocationBuilder assignable(boolean isAssignable) {
    this.isAssignable = isAssignable;
    return this;
  }

  /**
   * @param parent the parent location of the location.
   * @return fluent interface.
   */
  public LocationBuilder parent(Location parent) {
    this.parent = parent;
    return this;
  }

  /**
   * @param locationScope the {@link LocationScope} of the location.
   * @return fluent interface.
   */
  public LocationBuilder scope(LocationScope locationScope) {
    this.locationScope = locationScope;
    return this;
  }

  /**
   * @param geoLocation of the location.
   * @return fluent interface
   */
  public LocationBuilder geoLocation(GeoLocation geoLocation) {
    this.geoLocation = geoLocation;
    return this;
  }

  /**
   * @param providerId of the location
   * @return fluent interface
   */
  public LocationBuilder providerId(String providerId) {
    this.providerId = providerId;
    return this;
  }
}
