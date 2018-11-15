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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.MoreObjects;
import java.util.Optional;
import javax.annotation.Nullable;

/**
 * Created by daniel on 01.12.14.
 */
public abstract class ResourceImpl implements Resource {

  private final String id;
  private final String providerId;
  private final String name;
  @Nullable
  private final Location location;

  ResourceImpl(String id, String providerId, String name, @Nullable Location location) {
    checkNotNull(id, "id is required.");
    checkArgument(!id.isEmpty(), "id must not be empty.");
    this.id = id;
    checkNotNull(name, "name is required.");
    checkArgument(!name.isEmpty(), "name must not be empty.");
    this.name = name;
    checkNotNull(providerId, "providerId is required");
    checkArgument(!providerId.isEmpty(), "providerId must not be empty.");
    this.providerId = providerId;
    this.location = location;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ResourceImpl resource = (ResourceImpl) o;

    if (!id.equals(resource.id)) {
      return false;
    }
    if (!providerId.equals(resource.providerId)) {
      return false;
    }
    if (!name.equals(resource.name)) {
      return false;
    }
    return location != null ? location.equals(resource.location) : resource.location == null;
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + providerId.hashCode();
    result = 31 * result + name.hashCode();
    result = 31 * result + (location != null ? location.hashCode() : 0);
    return result;
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public String providerId() {
    return providerId;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public Optional<Location> location() {
    return Optional.ofNullable(location);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("id", id).add("name", name).toString();
  }
}
