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

import com.google.common.base.Optional;
import de.uniulm.omi.cloudiator.sword.api.domain.TemplateOptions;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachineTemplate;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Basic implementation of the {@link VirtualMachineTemplate} interface.
 */
public class VirtualMachineTemplateImpl implements VirtualMachineTemplate {

    @Nullable private final String name;
    private final String imageId;
    private final String hardwareFlavorId;
    private final String locationId;
    @Nullable private final TemplateOptions templateOptions;


    VirtualMachineTemplateImpl(@Nullable String name, String imageId, String hardwareFlavorId,
        String locationId, @Nullable TemplateOptions templateOptions) {

        checkNotNull(imageId);
        checkArgument(!imageId.isEmpty());

        checkNotNull(hardwareFlavorId);
        checkArgument(!hardwareFlavorId.isEmpty());

        checkNotNull(locationId);
        checkArgument(!locationId.isEmpty());

        this.imageId = imageId;
        this.hardwareFlavorId = hardwareFlavorId;
        this.locationId = locationId;
        this.templateOptions = templateOptions;
        this.name = name;
    }

    @Override public String name() {
        return name;
    }

    @Override public String imageId() {
        return this.imageId;
    }

    @Override public String hardwareFlavorId() {
        return this.hardwareFlavorId;
    }

    @Override public String locationId() {
        return this.locationId;
    }

    @Override public Optional<TemplateOptions> templateOptions() {
        return Optional.fromNullable(templateOptions);
    }
}
