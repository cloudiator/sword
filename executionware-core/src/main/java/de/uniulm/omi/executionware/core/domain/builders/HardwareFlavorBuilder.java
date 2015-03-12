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

import de.uniulm.omi.executionware.core.domain.impl.HardwareFlavorImpl;

/**
 * Created by daniel on 03.12.14.
 */
public class HardwareFlavorBuilder extends ResourceBuilder {

    private int cores;
    private int mbRam;

    private HardwareFlavorBuilder() {
        
    }

    public static HardwareFlavorBuilder newBuilder() {
        return new HardwareFlavorBuilder();
    }

    @Override
    public HardwareFlavorImpl build() {
        return new HardwareFlavorImpl(this.id, this.name, this.cores, this.mbRam);
    }

    @Override
    public HardwareFlavorBuilder id(String id) {
        super.id(id);
        return this;
    }

    @Override
    public HardwareFlavorBuilder name(final String name) {
        this.name = name;
        return this;
    }

    public HardwareFlavorBuilder cores(int cores) {
        this.cores = cores;
        return this;
    }

    public HardwareFlavorBuilder mbRam(int mbRam) {
        this.mbRam = mbRam;
        return this;
    }
}
