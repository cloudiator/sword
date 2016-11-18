/*
 * Copyright (c) 2014-2016 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.converters;

import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.sword.api.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.core.domain.HardwareFlavorBuilder;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.domain.FlavorInRegion;

/**
 * Created by daniel on 14.11.16.
 */
public class FlavorInRegionToHardwareFlavor
    implements OneWayConverter<FlavorInRegion, HardwareFlavor> {

    @Override public HardwareFlavor apply(FlavorInRegion flavorInRegion) {
        return HardwareFlavorBuilder.newBuilder().id(flavorInRegion.getId())
            .location(flavorInRegion.region()).providerId(flavorInRegion.getId())
            .name(flavorInRegion.getName()).cores(flavorInRegion.getVcpus())
            .gbDisk((float) flavorInRegion.getDisk()).mbRam(flavorInRegion.getRam()).build();
    }
}
