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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 04.02.15.
 */
public class IdScopeByLocations {

    private IdScopeByLocations() {

    }

    public static IdScopedByLocation from(String locationId, String id) {
        checkNotNull(locationId);
        checkArgument(!locationId.isEmpty());
        checkNotNull(id);
        checkArgument(!id.isEmpty());

        return new IdScopedByLocationImpl(id, locationId);
    }

    public static IdScopedByLocation from(String idWithLocation) {
        checkNotNull(idWithLocation);
        checkArgument(!idWithLocation.isEmpty());

        String[] parts = idWithLocation.split(IdScopedByLocationImpl.DELIMITER);

        checkArgument(parts.length == 2);

        return new IdScopedByLocationImpl(parts[0], parts[1]);
    }

}
