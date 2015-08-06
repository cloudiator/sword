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

package de.uniulm.omi.cloudiator.sword.drivers.jclouds.suppliers;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.api.supplier.Supplier;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.JCloudsComputeClient;
import org.jclouds.compute.domain.ComputeMetadata;

import java.util.Set;

/**
 * Created by daniel on 09.12.14.
 */
public class VirtualMachineSupplier implements Supplier<Set<VirtualMachine>> {

    private final JCloudsComputeClient jCloudsComputeClient;
    private final OneWayConverter<ComputeMetadata, VirtualMachine>
        jCloudsComputeMetadataToVirtualMachine;

    @Inject public VirtualMachineSupplier(JCloudsComputeClient jCloudsComputeClient,
        OneWayConverter<ComputeMetadata, VirtualMachine> jCloudsComputeMetadataToVirtualMachine) {
        this.jCloudsComputeClient = jCloudsComputeClient;
        this.jCloudsComputeMetadataToVirtualMachine = jCloudsComputeMetadataToVirtualMachine;
    }

    @Override public Set<VirtualMachine> get() {
        return Sets.newHashSet(Iterables.transform(jCloudsComputeClient.listNodes(),
            this.jCloudsComputeMetadataToVirtualMachine));
    }
}
