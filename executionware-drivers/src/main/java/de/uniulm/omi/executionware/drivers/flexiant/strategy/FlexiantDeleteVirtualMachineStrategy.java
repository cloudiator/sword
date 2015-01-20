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

package de.uniulm.omi.executionware.drivers.flexiant.strategy;

import com.google.inject.Inject;
import de.uniulm.omi.executionware.api.strategy.DeleteVirtualMachineStrategy;
import de.uniulm.omi.executionware.drivers.flexiant.FlexiantComputeClient;
import de.uniulm.omi.executionware.drivers.flexiant.util.FlexiantUtil;

/**
 * Created by daniel on 14.01.15.
 */
public class FlexiantDeleteVirtualMachineStrategy implements DeleteVirtualMachineStrategy {

    private final FlexiantComputeClient flexiantComputeClient;

    @Inject
    public FlexiantDeleteVirtualMachineStrategy(FlexiantComputeClient flexiantComputeClient) {
        this.flexiantComputeClient = flexiantComputeClient;
    }

    @Override
    public void apply(String virtualMachineId) {
        flexiantComputeClient.deleteServer(FlexiantUtil.stripLocation(virtualMachineId));
    }
}
