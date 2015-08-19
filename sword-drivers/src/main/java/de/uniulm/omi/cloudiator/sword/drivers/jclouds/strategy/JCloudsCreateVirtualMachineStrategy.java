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

package de.uniulm.omi.cloudiator.sword.drivers.jclouds.strategy;

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.sword.api.domain.TemplateOptions;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachineTemplate;
import de.uniulm.omi.cloudiator.sword.api.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.JCloudsComputeClient;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.domain.TemplateBuilder;

/**
 * Created by daniel on 12.01.15.
 */
public class JCloudsCreateVirtualMachineStrategy implements CreateVirtualMachineStrategy {

    private final JCloudsComputeClient jCloudsComputeClient;
    private final OneWayConverter<ComputeMetadata, VirtualMachine>
        computeMetadataVirtualMachineConverter;
    private final OneWayConverter<TemplateOptions, org.jclouds.compute.options.TemplateOptions>
        templateOptionsConverter;


    @Inject public JCloudsCreateVirtualMachineStrategy(JCloudsComputeClient jCloudsComputeClient,
        OneWayConverter<ComputeMetadata, VirtualMachine> computeMetadataVirtualMachineConverter,
        OneWayConverter<TemplateOptions, org.jclouds.compute.options.TemplateOptions> templateOptionsConverter) {
        this.jCloudsComputeClient = jCloudsComputeClient;
        this.computeMetadataVirtualMachineConverter = computeMetadataVirtualMachineConverter;
        this.templateOptionsConverter = templateOptionsConverter;
    }

    @Override public VirtualMachine apply(VirtualMachineTemplate virtualMachineTemplate) {

        final TemplateBuilder templateBuilder = jCloudsComputeClient.templateBuilder();

        templateBuilder.hardwareId(virtualMachineTemplate.hardwareFlavorId())
            .imageId(virtualMachineTemplate.imageId())
            .locationId(virtualMachineTemplate.locationId());

        if (virtualMachineTemplate.templateOptions().isPresent()) {
            templateBuilder.options(
                templateOptionsConverter.apply(virtualMachineTemplate.templateOptions().get()));
        }

        final Template template = templateBuilder.build();

        return this.computeMetadataVirtualMachineConverter
            .apply(this.jCloudsComputeClient.createNode(template));
    }
}
