/*
 * Copyright (c) 2014-2015 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.api.domain;

import javax.annotation.Nullable;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 11.12.15.
 */
public enum LocationScope implements Iterable {

    PROVIDER(null), REGION(PROVIDER), ZONE(REGION), HOST(ZONE);

    private final LocationScope parent;

    LocationScope(LocationScope parent) {
        this.parent = parent;
    }

    public Optional<LocationScope> parent() {
        return Optional.ofNullable(parent);
    }

    public Set<LocationScope> parents() {
        Set<LocationScope> parents = new HashSet<>();
        if (parent().isPresent()) {
            LocationScope parent = parent().get();
            parents.add(parent);
            parents.addAll(parent.parents());
        }
        return parents;
    }

    /**
     * Checks if the given location scope is a parent of this.
     *
     * @param locationScope the location scope to test for.
     * @return true of given scope is parent of this one, false if not.
     */
    public boolean hasParent(LocationScope locationScope) {
        return this.parents().contains(locationScope);
    }

    private static class LocationScopeIterator implements Iterator<LocationScope> {

        @Nullable private LocationScope cursor;

        private LocationScopeIterator(LocationScope start) {
            checkNotNull(start, "start is null");
            this.cursor = start;
        }

        @Override public boolean hasNext() {
            return cursor != null;
        }

        @Override public LocationScope next() {
            if (cursor == null) {
                throw new NoSuchElementException();
            }
            LocationScope current = cursor;
            cursor = cursor.parent().orElse(null);
            return current;
        }
    }

    @Override public Iterator iterator() {
        return new LocationScopeIterator(this);
    }
}
