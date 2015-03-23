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

package de.uniulm.omi.cloudiator.sword.core.domain.builders;

import de.uniulm.omi.cloudiator.sword.core.domain.impl.VirtualMachineTemplateImpl;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachineTemplate;

/**
 * Created by daniel on 09.01.15.
 */
public class VirtualMachineTemplateBuilder {

    private String imageId;
    private String hardwareFlavorId;
    private String locationId;

    private VirtualMachineTemplateBuilder() {
    }

    public static VirtualMachineTemplateBuilder newBuilder() {
        return new VirtualMachineTemplateBuilder();
    }

    public VirtualMachineTemplateBuilder image(final String imageId) {
        this.imageId = imageId;
        return this;
    }

    public VirtualMachineTemplateBuilder hardwareFlavor(final String hardwareFlavorId) {
        this.hardwareFlavorId = hardwareFlavorId;
        return this;
    }

    public VirtualMachineTemplateBuilder location(final String locationId) {
        this.locationId = locationId;
        return this;
    }

    public VirtualMachineTemplate build() {
        return new VirtualMachineTemplateImpl(imageId, hardwareFlavorId, locationId);
    }


}
