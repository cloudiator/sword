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

package de.uniulm.omi.executionware.drivers.jclouds.converters;

import com.google.inject.Inject;
import de.uniulm.omi.executionware.api.converters.OneWayConverter;
import de.uniulm.omi.executionware.api.domain.LoginCredential;
import de.uniulm.omi.executionware.api.domain.VirtualMachine;
import de.uniulm.omi.executionware.core.domain.builders.VirtualMachineBuilder;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.domain.LoginCredentials;

/**
 * Created by daniel on 09.12.14.
 */
public class JCloudsComputeMetadataToVirtualMachine implements OneWayConverter<ComputeMetadata, VirtualMachine> {

    private final OneWayConverter<LoginCredentials,LoginCredential> loginCredentialConverter;

    @Inject
    public JCloudsComputeMetadataToVirtualMachine(OneWayConverter<LoginCredentials, LoginCredential> loginCredentialConverter) {
        this.loginCredentialConverter = loginCredentialConverter;
    }

    @Override
    public VirtualMachine apply(final ComputeMetadata computeMetadata) {

        VirtualMachineBuilder virtualMachineBuilder = VirtualMachineBuilder.newBuilder();
        virtualMachineBuilder.id(computeMetadata.getId()).description(computeMetadata.getName());

        if(computeMetadata instanceof NodeMetadata) {
            ((NodeMetadata) computeMetadata).getPrivateAddresses().forEach(virtualMachineBuilder::addPrivateIpAddress);
            ((NodeMetadata) computeMetadata).getPublicAddresses().forEach(virtualMachineBuilder::addPublicIpAddress);
            if(((NodeMetadata) computeMetadata).getCredentials() != null) {
                virtualMachineBuilder.loginCredential(this.loginCredentialConverter.apply(((NodeMetadata) computeMetadata).getCredentials()));
            }
        }

        return virtualMachineBuilder.build();
    }
}
