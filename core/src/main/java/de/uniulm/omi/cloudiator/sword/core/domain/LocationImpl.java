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
    @Nullable private final Location parent;
    private final boolean isAssignable;


    LocationImpl(String id, String name, @Nullable Location parent, boolean isAssignable) {
        checkNotNull(id, "Location must have an ID");
        checkArgument(!id.isEmpty(), "Location ID must not be empty.");
        this.id = id;
        checkNotNull(name, "Location must have a name.");
        checkArgument(!name.isEmpty(), "Location name must not be empty.");
        this.name = name;
        this.parent = parent;
        this.isAssignable = isAssignable;
    }

    @Override public String id() {
        return this.id;
    }

    @Override public String name() {
        return this.name;
    }

    @Override public Optional<Location> parent() {
        return Optional.ofNullable(parent);
    }

    @Override public boolean isAssignable() {
        return this.isAssignable;
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id).add("name", name)
            .add("isAssignable", isAssignable).toString();
    }
}
