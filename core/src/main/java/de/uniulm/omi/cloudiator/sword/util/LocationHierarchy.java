/*
 * Copyright (c) 2014-2016 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.util;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Iterables;
import de.uniulm.omi.cloudiator.domain.LocationScope;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.annotation.Nullable;

/**
 * The LocationHierarchy class.
 * <p>
 * Helper class for iterating over Locations and finding special locations
 * of interest.
 */
public class LocationHierarchy implements Iterable<Location> {

  private final Location location;

  private LocationHierarchy(Location location) {
    checkNotNull(location, "location is null");
    this.location = location;
  }

  /**
   * Creates a LocationHierarchy using the given location as bottom.
   *
   * @param location the bottom location.
   * @return a LocationHierarchy.
   */
  public static LocationHierarchy of(Location location) {
    return new LocationHierarchy(location);
  }

  /**
   * Returns the topmost location of the hierarchy.
   *
   * @return the topmost location of the hierarchy
   */
  public Location topmostLocation() {
    return Iterables.getLast(this);
  }

  /**
   * Returns an {@link Optional} first parent location that has the given scope.
   * <p>
   * Will iterate upwards through the location hierarchy and return the first
   * location that matches the given scope.
   *
   * @param locationScope the scope to check for
   * @return an {@link Optional} first parent location or {@link Optional::empty}
   * @throws NullPointerException if the given locationScope is null.
   */
  public Optional<Location> firstParentLocationWithScope(LocationScope locationScope) {
    checkNotNull(locationScope, "locationScope is null");

    if (location.locationScope().equals(locationScope)) {
      return Optional.of(location);
    }

    for (Location iteration : this) {
      if (iteration.locationScope().equals(locationScope)) {
        return Optional.of(iteration);
      }
    }
    return Optional.empty();
  }

  @Override
  public Iterator<Location> iterator() {
    return new LocationIterator(location);
  }

  private static class LocationIterator implements Iterator<Location> {

    @Nullable
    private Location cursor;

    private LocationIterator(Location start) {
      checkNotNull(start, "start is null.");
      cursor = start;
    }

    @Override
    public boolean hasNext() {
      return cursor != null;
    }

    @Override
    public Location next() {
      if (cursor == null) {
        throw new NoSuchElementException();
      }
      Location current = cursor;
      cursor = current.parent().orElse(null);
      return current;
    }
  }
}
