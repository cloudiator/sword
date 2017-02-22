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

package de.uniulm.omi.cloudiator.sword.util;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Basic implementation of the {@link IdScopedByLocation}
 * interface.
 */
public class SlashEncodedIdScopedByLocation implements IdScopedByLocation {

    @Nullable private final String locationId;
    private final String id;
    static final String DELIMITER = "/";

    SlashEncodedIdScopedByLocation(@Nullable String locationId, String id) {
        this.locationId = locationId;
        this.id = id;
    }

    SlashEncodedIdScopedByLocation(String id) {
        checkNotNull(id, "id is null");
        checkArgument(!id.isEmpty(), "id is empty");

        String[] parts = id.split(SlashEncodedIdScopedByLocation.DELIMITER);

        switch (parts.length) {
            case 1:
                this.locationId = null;
                this.id = parts[0];
                break;
            case 2:
                this.locationId = parts[0];
                this.id = parts[1];
                break;
            default:
                throw new IllegalArgumentException("Could not calculate scoped id from " + id);
        }
    }

    @Override public String getId() {
        return this.id;
    }

    @Nullable @Override public String getLocationId() {
        return this.locationId;
    }

    @Override public String getIdWithLocation() {
        if (locationId != null) {
            return this.getLocationId() + DELIMITER + this.getId();
        }
        return this.getId();
    }
}
