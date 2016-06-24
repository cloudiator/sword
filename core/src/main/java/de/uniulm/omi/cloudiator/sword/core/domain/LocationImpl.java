/*
 * Copyright (c) 2014-2015 University of Ulm
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.uniulm.omi.cloudiator.sword.core.domain;

import com.google.common.base.MoreObjects;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.LocationScope;

import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 03.12.14.
 */
public class LocationImpl implements Location {

    private final String id;
    private final String name;
    private final LocationScope locationScope;
    @Nullable private final Location parent;
    private final boolean isAssignable;

    /**
     * Constructor. For building new objects use {@link LocationBuilder}.
     *
     * @param id            the id of the location (mandatory)
     * @param name          the name of the location (mandatory)
     * @param parent        the parent location (optional)
     * @param isAssignable  if the location is assignable (mandatory)
     * @param locationScope the scope of the location (mandatory)
     * @throws NullPointerException     if a mandatory parameter is null.
     * @throws IllegalArgumentException if a mandatory string attribute is empty.
     */
    LocationImpl(String id, String name, @Nullable Location parent, boolean isAssignable,
        LocationScope locationScope) {
        checkNotNull(id, "Location must have an ID");
        checkArgument(!id.isEmpty(), "Location ID must not be empty.");
        this.id = id;
        checkNotNull(name, "Location must have a name.");
        checkArgument(!name.isEmpty(), "Location name must not be empty.");
        this.name = name;
        this.parent = parent;
        this.isAssignable = isAssignable;
        checkNotNull(locationScope);
        this.locationScope = locationScope;
    }

    @Override public String id() {
        return this.id;
    }

    @Override public String providerId() {
        return id();
    }

    @Override public String name() {
        return this.name;
    }

    @Override public Optional<Location> parent() {
        return Optional.ofNullable(parent);
    }

    @Override public LocationScope locationScope() {
        return locationScope;
    }

    @Override public boolean isAssignable() {
        return this.isAssignable;
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id).add("providerId", providerId())
            .add("name", name).add("parent", parent).add("isAssignable", isAssignable)
            .add("locationScope", locationScope).toString();
    }
}
