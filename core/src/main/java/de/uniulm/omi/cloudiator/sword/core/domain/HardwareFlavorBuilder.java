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

import de.uniulm.omi.cloudiator.sword.api.domain.Location;

import javax.annotation.Nullable;

/**
 * Created by daniel on 03.12.14.
 */
public class HardwareFlavorBuilder {

    private String id;
    private String name;
    private int cores;
    private long mbRam;
    @Nullable private Float gbDisk;
    @Nullable private Location location;

    private HardwareFlavorBuilder() {
    }

    public static HardwareFlavorBuilder newBuilder() {
        return new HardwareFlavorBuilder();
    }

    public HardwareFlavorImpl build() {
        return new HardwareFlavorImpl(id, name, location, cores, mbRam, gbDisk);
    }

    public HardwareFlavorBuilder id(String id) {
        this.id = id;
        return this;
    }

    public HardwareFlavorBuilder name(String name) {
        this.name = name;
        return this;
    }

    public HardwareFlavorBuilder location(Location location) {
        this.location = location;
        return this;
    }

    public HardwareFlavorBuilder cores(int cores) {
        this.cores = cores;
        return this;
    }

    public HardwareFlavorBuilder mbRam(long mbRam) {
        this.mbRam = mbRam;
        return this;
    }

    public HardwareFlavorBuilder gbDisk(@Nullable Float gbDisk) {
        this.gbDisk = gbDisk;
        return this;
    }
}
