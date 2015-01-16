/*
 * Copyright (c) 2014 University of Ulm
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

package de.uniulm.omi.executionware.drivers.jclouds.suppliers;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import de.uniulm.omi.executionware.api.converters.Converter;
import de.uniulm.omi.executionware.api.domain.HardwareFlavor;
import de.uniulm.omi.executionware.api.supplier.Supplier;
import de.uniulm.omi.executionware.drivers.jclouds.JCloudsComputeClient;
import org.jclouds.compute.domain.Hardware;

import java.util.Set;

/**
 * Created by daniel on 03.12.14.
 */
public class HardwareSupplier implements Supplier<Set<? extends HardwareFlavor>> {

    private final JCloudsComputeClient jCloudsComputeClient;
    private final Converter<org.jclouds.compute.domain.Hardware, HardwareFlavor> jCloudsHardwareToHardwareFlavor;

    @Inject
    public HardwareSupplier(JCloudsComputeClient jCloudsComputeClient, Converter<Hardware, HardwareFlavor> jCloudsHardwareToHardwareFlavor) {
        this.jCloudsComputeClient = jCloudsComputeClient;
        this.jCloudsHardwareToHardwareFlavor = jCloudsHardwareToHardwareFlavor;
    }

    @Override
    public Set<? extends HardwareFlavor> get() {
        return Sets.newHashSet(Iterables.transform(jCloudsComputeClient.listHardwareProfiles(), this.jCloudsHardwareToHardwareFlavor));
    }
}
