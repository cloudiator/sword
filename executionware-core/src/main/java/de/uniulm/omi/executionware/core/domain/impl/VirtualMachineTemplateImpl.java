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

import de.uniulm.omi.executionware.api.domain.VirtualMachineTemplate;

/**
 * Created by daniel on 09.01.15.
 */
public class VirtualMachineTemplateImpl implements VirtualMachineTemplate {

    private final String imageId;
    private final String hardwareFlavorId;
    private final String locationId;

    public VirtualMachineTemplateImpl(String imageId, String hardwareFlavorId, String locationId) {
        this.imageId = imageId;
        this.hardwareFlavorId = hardwareFlavorId;
        this.locationId = locationId;
    }

    @Override
    public String getImageId() {
        return this.imageId;
    }

    @Override
    public String getHardwareFlavorId() {
        return this.hardwareFlavorId;
    }

    @Override
    public String getLocationId() {
        return this.locationId;
    }
}
