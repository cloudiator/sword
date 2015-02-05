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

package de.uniulm.omi.executionware.drivers.flexiant.suppliers;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import de.uniulm.omi.executionware.api.converters.OneWayConverter;
import de.uniulm.omi.executionware.api.domain.HardwareFlavor;
import de.uniulm.omi.executionware.api.supplier.Supplier;
import de.uniulm.omi.executionware.drivers.flexiant.FlexiantComputeClient;
import de.uniulm.omi.flexiant.domain.impl.Hardware;

import java.util.Set;

/**
 * Created by daniel on 05.12.14.
 */
public class HardwareSupplier implements Supplier<Set<HardwareFlavor>> {

    private final FlexiantComputeClient flexiantComputeClient;
    private final OneWayConverter<Hardware, HardwareFlavor> hardwareConverter;

    @Inject
    public HardwareSupplier(FlexiantComputeClient flexiantComputeClient, OneWayConverter<Hardware, HardwareFlavor> hardwareConverter) {
        this.flexiantComputeClient = flexiantComputeClient;
        this.hardwareConverter = hardwareConverter;
    }

    @Override
    public Set<HardwareFlavor> get() {
        return Sets.newHashSet(Iterables.transform(flexiantComputeClient.listHardware(), this.hardwareConverter));
    }
}
