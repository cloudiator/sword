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

package de.uniulm.omi.cloudiator.sword.domain;

import com.google.common.base.MoreObjects;

import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by daniel on 03.12.14.
 */
public class HardwareFlavorImpl extends ResourceImpl implements HardwareFlavor {

    private int cores;
    private long ram;
    @Nullable Float gbDisk;

    HardwareFlavorImpl(String id, String providerId, String name, @Nullable Location location,
        int cores, long ram, @Nullable Float gbDisk) {
        super(id, providerId, name, location);
        checkArgument(cores > 0, "Cores must be > 0");
        checkArgument(ram > 0, "RAM must be > 0");
        if (gbDisk != null) {
            checkArgument(gbDisk > 0, "Disk must be > 0");
        }
        this.cores = cores;
        this.ram = ram;
        this.gbDisk = gbDisk;
    }

    @Override public int numberOfCores() {
        return cores;
    }

    @Override public long mbRam() {
        return ram;
    }

    @Nullable @Override public Optional<Float> gbDisk() {
        return Optional.ofNullable(gbDisk);
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id()).add("providerId", providerId())
            .add("name", id()).add("cores", cores).add("ram", ram).add("disk", gbDisk).toString();
    }
}
