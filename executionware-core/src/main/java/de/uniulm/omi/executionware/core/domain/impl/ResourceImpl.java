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

package de.uniulm.omi.executionware.core.domain.impl;

import de.uniulm.omi.executionware.api.domain.Resource;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 01.12.14.
 */
public abstract class ResourceImpl implements Resource {

    protected final String id;
    protected final String name;

    public ResourceImpl(final String id, final String name) {
        checkNotNull(id);
        checkArgument(!id.isEmpty(), "ID must not be empty.");
        this.id = id;
        checkNotNull(name);
        checkArgument(!name.isEmpty(), "Name must not be empty.");
        this.name = name;
    }

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String toString() {
        return String.format("Resource(id: %s, name: %s)", this.id, this.name);
    }
}
