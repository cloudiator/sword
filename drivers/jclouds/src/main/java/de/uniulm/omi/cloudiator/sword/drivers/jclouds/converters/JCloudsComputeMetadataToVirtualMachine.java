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

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import de.uniulm.omi.cloudiator.domain.*;
import de.uniulm.omi.cloudiator.sword.api.strategy.GetStrategy;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.domain.LoginCredentials;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Converts a jclouds {@link ComputeMetadata} object into
 * a {@link VirtualMachine} object.
 * <p>
 * Requires the compute metadata object to be of type
 * {@link NodeMetadata}, as otherwise not all required
 * information is available.
 */
public class JCloudsComputeMetadataToVirtualMachine
    implements OneWayConverter<ComputeMetadata, VirtualMachine> {

    private final OneWayConverter<LoginCredentials, LoginCredential> loginCredentialConverter;
    private final OneWayConverter<Hardware, HardwareFlavor> hardwareConverter;
    private final GetStrategy<String, Image> imageGetStrategy;

    /**
     * Constructor.
     *
     * @param loginCredentialConverter a converter for the login credentials (non null)
     * @param hardwareConverter        a converter for the hardware
     * @param imageGetStrategy         strategy to retrieve images from an ID
     */
    @Inject public JCloudsComputeMetadataToVirtualMachine(
        OneWayConverter<LoginCredentials, LoginCredential> loginCredentialConverter,
        OneWayConverter<Hardware, HardwareFlavor> hardwareConverter,
        GetStrategy<String, Image> imageGetStrategy) {

        checkNotNull(loginCredentialConverter);
        checkNotNull(hardwareConverter);
        checkNotNull(imageGetStrategy);

        this.loginCredentialConverter = loginCredentialConverter;
        this.hardwareConverter = hardwareConverter;
        this.imageGetStrategy = imageGetStrategy;
    }

    @Override public VirtualMachine apply(final ComputeMetadata computeMetadata) {

        checkArgument(computeMetadata instanceof NodeMetadata);

        VirtualMachineBuilder virtualMachineBuilder = VirtualMachineBuilder.newBuilder();
        virtualMachineBuilder.id(computeMetadata.getId())
            .providerId(computeMetadata.getProviderId()).name(forceName(computeMetadata));

        ((NodeMetadata) computeMetadata).getPrivateAddresses()
            .forEach(virtualMachineBuilder::addPrivateIpAddress);
        ((NodeMetadata) computeMetadata).getPublicAddresses()
            .forEach(virtualMachineBuilder::addPublicIpAddress);
        if (((NodeMetadata) computeMetadata).getCredentials() != null) {
            virtualMachineBuilder.loginCredential(this.loginCredentialConverter
                .apply(((NodeMetadata) computeMetadata).getCredentials()));
        }
        virtualMachineBuilder
            .hardware(hardwareConverter.apply(((NodeMetadata) computeMetadata).getHardware()));
        virtualMachineBuilder
            .image(imageGetStrategy.get(((NodeMetadata) computeMetadata).getImageId()));

        return virtualMachineBuilder.build();
    }

    private String forceName(ComputeMetadata computeMetadata) {
        if (computeMetadata.getName() == null) {
            return computeMetadata.getId();
        }
        return computeMetadata.getName();
    }


}
