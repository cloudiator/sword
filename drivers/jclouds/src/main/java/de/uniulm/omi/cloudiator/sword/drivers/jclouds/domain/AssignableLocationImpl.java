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

package de.uniulm.omi.cloudiator.sword.drivers.jclouds.domain;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.Set;
import org.jclouds.domain.Location;
import org.jclouds.domain.LocationScope;

/**
 * Created by daniel on 12.01.16.
 */
public class AssignableLocationImpl implements AssignableLocation {

  private final Location location;
  private final boolean isAssignable;

  public AssignableLocationImpl(Location location, boolean isAssignable) {
    checkNotNull(location);
    this.location = location;
    this.isAssignable = isAssignable;
  }

  @Override
  public boolean isAssignable() {
    return isAssignable;
  }

  @Override
  public LocationScope getScope() {
    return location.getScope();
  }

  @Override
  public String getId() {
    return location.getId();
  }

  @Override
  public String getDescription() {
    return location.getDescription();
  }

  @Override
  public Location getParent() {
    return location.getParent();
  }

  @Override
  public Map<String, Object> getMetadata() {
    return location.getMetadata();
  }

  @Override
  public Set<String> getIso3166Codes() {
    return location.getIso3166Codes();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AssignableLocationImpl that = (AssignableLocationImpl) o;

    return this.location.equals(that.location);
  }

  @Override
  public int hashCode() {
    return location.hashCode();
  }
}
