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

package de.uniulm.omi.cloudiator.sword.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;

/**
 * Represents an offered hardware flavor by the
 * provider.
 */
public interface HardwareFlavor extends Resource {

    /**
     * Returns the number of cores the offer has. Always > 0.
     *
     * @return the number of cores.
     */
    @JsonProperty
    int numberOfCores();

    /**
     * Returns the amount of ram the offer has. Always > 0.
     *
     * @return the amount of ram.
     */
    @JsonProperty
    long mbRam();

    /**
     * Returns the amount of disk space the offer (with respect to its boot volume) has. Unit is
     * gigabyte.
     * If the disk space is unknown, null is returned.
     *
     * @return the amount of disk space or null.
     */
    @JsonProperty
    @Nullable Float gbDisk();
}
