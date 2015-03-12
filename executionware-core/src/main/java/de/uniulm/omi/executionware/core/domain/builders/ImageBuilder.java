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

package de.uniulm.omi.executionware.core.domain.builders;

import de.uniulm.omi.executionware.core.domain.impl.ImageImpl;

/**
 * Created by daniel on 01.12.14.
 */
public class ImageBuilder extends ResourceBuilder {

    private ImageBuilder() {

    }

    public static ImageBuilder newBuilder() {
        return new ImageBuilder();
    }

    @Override
    public ImageBuilder id(String id) {
        super.id(id);
        return this;
    }

    @Override
    public ImageBuilder name(final String name) {
        this.name = name;
        return this;
    }

    @Override
    public ImageImpl build() {
        return new ImageImpl(this.id, this.name);
    }
}
