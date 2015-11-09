/*
 * Copyright (c) 2014-2015 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.drivers.openstack.converters;

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.sword.api.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.core.domain.VirtualMachineBuilder;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.converters.JCloudsComputeMetadataToVirtualMachine;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.domain.LoginCredentials;

/**
 * Overwrites functionality of the standard jclouds converter {@link de.uniulm.omi.cloudiator.sword.drivers.jclouds.converters.JCloudsComputeMetadataToVirtualMachine}
 * <p/>
 * The jclouds implementation for Openstack seems to guess the username for the
 * login credentials. This obviously causes problems when using the for logins.
 * <p/>
 * For this purpose, this custom converter removes the login credentials from
 * the object until we find a better solution for this issue.
 * <p/>
 * In addition this should not impose a huge problem, as we support keypairs for
 * openstack.
 */
public class ComputeMetadataConverterOverwrite
    implements OneWayConverter<ComputeMetadata, VirtualMachine> {

    private final OneWayConverter<ComputeMetadata, VirtualMachine> baseJcloudsConverter;

    @Inject public ComputeMetadataConverterOverwrite(
        OneWayConverter<LoginCredentials, LoginCredential> loginCredentialConverter) {
        //todo directly inject dependency?
        this.baseJcloudsConverter =
            new JCloudsComputeMetadataToVirtualMachine(loginCredentialConverter);
    }

    @Override public VirtualMachine apply(ComputeMetadata computeMetadata) {
        // overwrite original login credential with null
        return VirtualMachineBuilder.of(baseJcloudsConverter.apply(computeMetadata))
            .loginCredential(null).build();
    }
}
