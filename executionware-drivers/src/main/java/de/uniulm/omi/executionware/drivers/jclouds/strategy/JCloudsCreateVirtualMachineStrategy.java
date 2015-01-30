/*
 * Copyright (c) 2015 University of Ulm
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

package de.uniulm.omi.executionware.drivers.jclouds.strategy;

import com.google.inject.Inject;
import de.uniulm.omi.executionware.api.converters.OneWayConverter;
import de.uniulm.omi.executionware.api.domain.VirtualMachine;
import de.uniulm.omi.executionware.api.domain.VirtualMachineTemplate;
import de.uniulm.omi.executionware.api.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.executionware.drivers.jclouds.JCloudsComputeClient;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.Template;

/**
 * Created by daniel on 12.01.15.
 */
public class JCloudsCreateVirtualMachineStrategy implements CreateVirtualMachineStrategy {

    private final JCloudsComputeClient jCloudsComputeClient;
    private final OneWayConverter<ComputeMetadata, VirtualMachine> computeMetadataVirtualMachineConverter;


    @Inject
    public JCloudsCreateVirtualMachineStrategy(JCloudsComputeClient jCloudsComputeClient, OneWayConverter<ComputeMetadata, VirtualMachine> computeMetadataVirtualMachineConverter) {
        this.jCloudsComputeClient = jCloudsComputeClient;
        this.computeMetadataVirtualMachineConverter = computeMetadataVirtualMachineConverter;
    }

    @Override
    public VirtualMachine apply(VirtualMachineTemplate virtualMachineTemplate) {

        final Template template = this.jCloudsComputeClient.templateBuilder()
                .hardwareId(virtualMachineTemplate.getHardwareFlavorId())
                .imageId(virtualMachineTemplate.getImageId())
                .locationId(virtualMachineTemplate.getLocationId())
                .build();

        return this.computeMetadataVirtualMachineConverter.apply(this.jCloudsComputeClient.createNode(template));
    }
}
