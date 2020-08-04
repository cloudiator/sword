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

package de.uniulm.omi.cloudiator.sword.drivers.onestep.suppliers;

import client.model.templates.Cluster;
import com.google.common.base.Supplier;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavorBuilder;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.domain.ImageTemplate;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.domain.ImageTemplatesSet;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.internal.HardwareFlavourNamingStrategy;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class HardwareSupplier implements Supplier<Set<HardwareFlavor>> {
    private static int CPU_STEP = 1;
    private static int RAM_STEP_IN_GB = 1;
    private static int DISK_STEP_IN_GB = 10;

    private final ImageTemplatesSet imageTemplatesSet;

    @Inject
    public HardwareSupplier(ImageTemplatesSet imageTemplatesSet) {
        this.imageTemplatesSet = checkNotNull(imageTemplatesSet, "imageTemplatesSet is null");
    }

    @Override
    public Set<HardwareFlavor> get() {
        Set<Cluster> clusters = imageTemplatesSet.getImageTemplates()
                .stream()
                .map(ImageTemplate::getCluster)
                .collect(Collectors.toSet());

        return createAllPossibleHardwareFlavors(clusters);
    }

    private Set<HardwareFlavor> createAllPossibleHardwareFlavors(Set<Cluster> clusters) {
        Set<HardwareFlavor> hardwareFlavors = new HashSet<>();
        int hardwareFlavourNo = 0;
        for (Cluster cluster : clusters) {
            for (int cpu = cluster.getResources().getCpu().getMin(); cpu <= cluster.getResources().getCpu().getMax();
                 cpu += CPU_STEP) {
                for (int ram = cluster.getResources().getRam().getMin(); ram <= cluster.getResources().getRam().getMax();
                     ram += RAM_STEP_IN_GB) {
                    //for (double disk = cluster.getResources().getPrimaryDisk().getMin(); disk <= cluster.getResources().getPrimaryDisk().getMax();
                     //    disk += DISK_STEP_IN_GB) {
                        hardwareFlavors.add(HardwareFlavorBuilder.newBuilder()
                                .id(HardwareFlavourNamingStrategy.createHardwareFlavourNameFromClusterId(cluster.getId(), hardwareFlavourNo))
                                .providerId(cluster.getName())
                                .name(cluster.getName())
                                .cores(cpu)
                                .gbDisk(null) //this is related to Template not to Hardware, so it must be empty
                                .mbRam(ram)
                                .build());
                        hardwareFlavourNo++;
                    //}
                }

            }

        }

        return hardwareFlavors;
    }
}
