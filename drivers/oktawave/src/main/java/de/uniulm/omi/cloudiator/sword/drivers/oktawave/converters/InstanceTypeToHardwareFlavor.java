/*
 * Copyright (c) 2014-2018 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.drivers.oktawave.converters;

import com.oktawave.api.client.model.InstanceType;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavorBuilder;
import de.uniulm.omi.cloudiator.util.OneWayConverter;

public class InstanceTypeToHardwareFlavor implements OneWayConverter<InstanceType, HardwareFlavor> {

    @Override
    public HardwareFlavor apply(InstanceType instanceType) {
        return HardwareFlavorBuilder.newBuilder()
                .id(String.valueOf(instanceType.getId()))
                .providerId(instanceType.getName())
                .name(instanceType.getName())
                .cores(instanceType.getCpu())
                .gbDisk(null) //this is related to Template not to Hardware, so it must be empty
                .mbRam(instanceType.getRam())
                .build();
    }
}
