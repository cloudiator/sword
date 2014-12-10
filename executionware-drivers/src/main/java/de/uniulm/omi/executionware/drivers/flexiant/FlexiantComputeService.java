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

package de.uniulm.omi.executionware.drivers.flexiant;

import com.google.inject.Inject;
import de.uniulm.omi.executionware.api.domain.HardwareFlavor;
import de.uniulm.omi.executionware.api.domain.Image;
import de.uniulm.omi.executionware.api.domain.Location;
import de.uniulm.omi.executionware.api.domain.VirtualMachine;
import de.uniulm.omi.executionware.api.supplier.Supplier;
import de.uniulm.omi.executionware.core.base.BaseComputeService;

import java.util.Set;

/**
 * Created by daniel on 05.12.14.
 */
public class FlexiantComputeService extends BaseComputeService {

    @Inject
    public FlexiantComputeService(
            Supplier<Set<? extends Image>> imageSupplier,
            Supplier<Set<? extends Location>> locationSupplier,
            Supplier<Set<? extends HardwareFlavor>> hardwareFlavorSupplier,
            Supplier<Set<? extends VirtualMachine>> virtualMachineSupplier) {
        super(
                imageSupplier,
                locationSupplier,
                hardwareFlavorSupplier,
                virtualMachineSupplier
        );
    }
}
