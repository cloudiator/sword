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

package de.uniulm.omi.cloudiator.sword.core.util;

import de.uniulm.omi.cloudiator.sword.api.util.IdScopedByLocation;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Factory class for {@link IdScopedByLocation} objects.
 */
public class IdScopeByLocations {

    private IdScopeByLocations() {
    }

    /**
     * Creates a new scoped id using the supplied arguments.
     *
     * @param locationId the location part of the id (nullable).
     * @param id         the id part of the id (non null)
     * @return a new scoped id.
     * @throws NullPointerException     if the supplied id is null.
     * @throws IllegalArgumentException if the supplied strings are empty
     */
    public static IdScopedByLocation from(@Nullable String locationId, String id) {

        checkNotNull(id);
        checkArgument(!id.isEmpty());

        if (locationId != null) {
            checkArgument(!locationId.isEmpty());
        }

        return new SlashEncodedIdScopedByLocation(locationId, id);
    }

    /**
     * Creates a new scoped id from a id.
     *
     * @param id a scoped id, or an un-scoped id (non null).
     * @return a new scoped id representing the argument id.
     * @throws IllegalArgumentException if the scoped id is invalid
     */
    public static IdScopedByLocation from(String id) {
        return new SlashEncodedIdScopedByLocation(id);
    }

}
