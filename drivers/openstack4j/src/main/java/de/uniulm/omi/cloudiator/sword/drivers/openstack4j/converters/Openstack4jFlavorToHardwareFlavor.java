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
import org.openstack4j.model.compute.Flavor;

import javax.annotation.Nullable;

/**
 * Created by daniel on 14.11.16.
 */
public class Openstack4jFlavorToHardwareFlavor implements OneWayConverter<Flavor, HardwareFlavor> {

    @Nullable @Override public HardwareFlavor apply(Flavor flavor) {

        return HardwareFlavorBuilder.newBuilder().id(flavor.getId()).providerId(flavor.getId())
            .name(flavor.getName()).cores(flavor.getVcpus()).gbDisk((float) flavor.getDisk())
            .mbRam(flavor.getRam()).build();
    }
}
