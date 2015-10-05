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

import de.uniulm.omi.cloudiator.sword.api.domain.Resource;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 01.12.14.
 */
public abstract class ResourceImpl implements Resource {

    protected final String id;
    protected final String name;

    ResourceImpl(final String id, final String name) {
        checkNotNull(id, "ID is required.");
        checkArgument(!id.isEmpty(), "ID must not be empty.");
        this.id = id;
        checkNotNull(name, "Name is required.");
        checkArgument(!name.isEmpty(), "Name must not be empty.");
        this.name = name;
    }

    @Override public String id() {
        return id;
    }

    @Override public String name() {
        return name;
    }

    @Override public String toString() {
        return name;
    }
}
