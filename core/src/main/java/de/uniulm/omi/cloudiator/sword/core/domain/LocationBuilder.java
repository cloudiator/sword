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

import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.LocationScope;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A builder for immutable {@link Location} objects.
 */
public class LocationBuilder {

    private String id;
    private String name;
    @Nullable private Location parent;
    private boolean isAssignable;
    private LocationScope locationScope;

    /**
     * Use LocationBuilder::newBuilder to create builder objects.
     */
    private LocationBuilder() {
        //intentionally empty
    }

    private LocationBuilder(Location location) {
        id = location.id();
        name = location.name();
        parent = location.parent().orElse(null);
        isAssignable = location.isAssignable();
        locationScope = location.locationScope();
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
        return new LocationImpl(id, name, parent, isAssignable, locationScope);
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
}
