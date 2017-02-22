/*
 * Copyright (c) 2014-2017 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.multicloud.service;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.extensions.PublicIpExtension;

import static com.google.common.base.Preconditions.*;

/**
 * Created by daniel on 24.01.17.
 */
public class MultiCloudPublicIpExtension implements PublicIpExtension {

    private final ComputeServiceProvider computeServiceProvider;

    @Inject public MultiCloudPublicIpExtension(ComputeServiceProvider computeServiceProvider) {
        this.computeServiceProvider = computeServiceProvider;
    }

    private PublicIpExtension publicIpService(String virtualMachineId) {
        final IdScopedByCloud scopedVirtualMachineId = IdScopedByClouds.from(virtualMachineId);
        final Optional<PublicIpExtension> publicIpExtensionOptionalOptional =
            computeServiceProvider.forId(scopedVirtualMachineId.cloudId()).publicIpExtension();
        checkState(publicIpExtensionOptionalOptional.isPresent(), String
            .format("PublicIpExtension is not available for cloud %s.",
                scopedVirtualMachineId.cloudId()));
        return publicIpExtensionOptionalOptional.get();
    }

    @Override public String addPublicIp(String virtualMachineId) {
        checkNotNull(virtualMachineId, "virtualMachineId is null");
        checkArgument(!virtualMachineId.isEmpty(), "virtualMachineId is empty");
        return publicIpService(virtualMachineId)
            .addPublicIp(IdScopedByClouds.from(virtualMachineId).id());
    }

    @Override public void removePublicIp(String virtualMachineId, String address) {
        checkNotNull(virtualMachineId, "virtualMachineId is null");
        checkArgument(!virtualMachineId.isEmpty(), "virtualMachineId is empty");
        checkNotNull(address, "address is null");
        checkArgument(!address.isEmpty(), "address is empty");
        publicIpService(virtualMachineId)
            .removePublicIp(IdScopedByClouds.from(virtualMachineId).id(), address);
    }
}
