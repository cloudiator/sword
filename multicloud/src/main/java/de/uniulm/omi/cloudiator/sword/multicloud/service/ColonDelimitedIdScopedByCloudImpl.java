/*
 * Copyright (c) 2014-2017 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.multicloud.service;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 19.01.17.
 */
public class ColonDelimitedIdScopedByCloudImpl implements IdScopedByCloud {

    private final String id;
    private final String cloudId;
    private static final String DELIMITER = ":";

    ColonDelimitedIdScopedByCloudImpl(String scopedId) {
        checkNotNull(scopedId, "scopedId is null");
        checkArgument(!scopedId.isEmpty(), "scopedId is empty");
        String parts[] = scopedId.split(DELIMITER);
        if (parts.length != 2) {
            throw new IllegalArgumentException(String.format(
                "Expected scopedId %s to contain exactly one occurrence of delimiter (%s). Splitting however brought %s parts.",
                scopedId, DELIMITER, parts.length));

        }
        this.cloudId = parts[0];
        this.id = parts[1];

    }

    ColonDelimitedIdScopedByCloudImpl(String id, String cloudId) {
        checkNotNull(id, "id is null");
        checkArgument(!id.isEmpty(), "id is empty");
        checkNotNull(cloudId, "cloudId is empty");
        checkArgument(!cloudId.isEmpty(), "cloud");
        this.id = id;
        this.cloudId = cloudId;
    }

    @Override public String id() {
        return id;
    }

    @Override public String cloudId() {
        return cloudId;
    }

    @Override public String scopedId() {
        return cloudId + DELIMITER + id;
    }
}
