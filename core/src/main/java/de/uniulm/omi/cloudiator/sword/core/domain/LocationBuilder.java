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

import javax.annotation.Nullable;

/**
 * Created by daniel on 03.12.14.
 */
public class LocationBuilder {

    private String id;
    private String name;
    @Nullable private Location parent;
    private boolean isAssignable;

    private LocationBuilder() {

    }

    public static LocationBuilder newBuilder() {
        return new LocationBuilder();
    }

    public LocationImpl build() {
        return new LocationImpl(id, name, parent, isAssignable);
    }

    public LocationBuilder id(String id) {
        this.id = id;
        return this;
    }

    public LocationBuilder name(final String name) {
        this.name = name;
        return this;
    }

    public LocationBuilder assignable(boolean isAssignable) {
        this.isAssignable = isAssignable;
        return this;
    }

    public LocationBuilder parent(Location parent) {
        this.parent = parent;
        return this;
    }
}
