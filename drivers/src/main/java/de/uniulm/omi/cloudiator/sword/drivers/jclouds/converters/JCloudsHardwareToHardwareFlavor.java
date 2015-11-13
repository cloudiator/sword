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

package de.uniulm.omi.cloudiator.sword.drivers.jclouds.converters;


import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.sword.api.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.core.domain.HardwareFlavorBuilder;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.Processor;
import org.jclouds.compute.domain.Volume;

import java.util.Optional;

/**
 * Created by daniel on 03.12.14.
 */
public class JCloudsHardwareToHardwareFlavor implements OneWayConverter<Hardware, HardwareFlavor> {

    @Override public HardwareFlavor apply(Hardware hardware) {
        int cores = 0;
        for (Processor processor : hardware.getProcessors()) {
            cores += processor.getCores();
        }
        Float gbDisk = null;
        final Optional<? extends Volume> bootVolume =
            hardware.getVolumes().stream().filter(Volume::isBootDevice).findFirst();
        if (bootVolume.isPresent()) {
            gbDisk = bootVolume.get().getSize();
        }

        return HardwareFlavorBuilder.newBuilder().id(hardware.getId()).name(forceName(hardware))
            .cores(cores).mbRam(hardware.getRam()).gbDisk(gbDisk).build();
    }

    private String forceName(Hardware hardware) {
        if (hardware.getName() == null) {
            return hardware.getId();
        }
        return hardware.getName();
    }
}
