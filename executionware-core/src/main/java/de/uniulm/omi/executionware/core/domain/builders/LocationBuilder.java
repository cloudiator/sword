/*
 * Copyright (c) 2014 University of Ulm
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

package de.uniulm.omi.executionware.core.domain.builders;

import de.uniulm.omi.executionware.core.domain.impl.LocationImpl;

/**
 * Created by daniel on 03.12.14.
 */
public class LocationBuilder extends DescribedResourceBuilder {

    private boolean isAssignable;

    @Override
    public LocationImpl build() {
        return new LocationImpl(this.id, this.isAssignable, this.description);
    }

    @Override
    public LocationBuilder id(String id) {
        super.id(id);
        return this;
    }

    public LocationBuilder assignable(boolean isAssignable) {
        this.isAssignable = isAssignable;
        return this;
    }

    @Override
    public LocationBuilder description(String description) {
        super.description(description);
        return this;
    }
}
