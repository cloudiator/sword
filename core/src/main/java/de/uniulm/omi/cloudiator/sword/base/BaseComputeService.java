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

package de.uniulm.omi.cloudiator.sword.base;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachineTemplate;
import de.uniulm.omi.cloudiator.sword.extensions.KeyPairExtension;
import de.uniulm.omi.cloudiator.sword.extensions.PublicIpExtension;
import de.uniulm.omi.cloudiator.sword.extensions.SecurityGroupExtension;
import de.uniulm.omi.cloudiator.sword.remote.RemoteConnectionFactory;
import de.uniulm.omi.cloudiator.sword.service.ComputeService;
import de.uniulm.omi.cloudiator.sword.service.ConnectionService;
import de.uniulm.omi.cloudiator.sword.service.DiscoveryService;
import de.uniulm.omi.cloudiator.sword.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.strategy.DeleteVirtualMachineStrategy;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 02.12.14.
 */
public class BaseComputeService implements ComputeService {

    private final DiscoveryService discoveryService;
    private final CreateVirtualMachineStrategy createVirtualMachineStrategy;
    private final DeleteVirtualMachineStrategy deleteVirtualMachineStrategy;
    private final Optional<PublicIpExtension> publicIpService;
    private final Optional<KeyPairExtension> keyPairService;
    private final Optional<SecurityGroupExtension> securityGroupService;
    private final ConnectionService connectionService;


    @Inject public BaseComputeService(CreateVirtualMachineStrategy createVirtualMachineStrategy,
        DeleteVirtualMachineStrategy deleteVirtualMachineStrategy,
        RemoteConnectionFactory remoteConnectionFactory, DiscoveryService discoveryService,
        Optional<PublicIpExtension> publicIpService, Optional<KeyPairExtension> keyPairService,
        Optional<SecurityGroupExtension> securityGroupService,
        ConnectionService connectionService) {



        checkNotNull(createVirtualMachineStrategy);
        checkNotNull(deleteVirtualMachineStrategy);
        checkNotNull(remoteConnectionFactory);
        checkNotNull(publicIpService);
        checkNotNull(keyPairService);
        checkNotNull(securityGroupService);
        checkNotNull(connectionService);
        checkNotNull(discoveryService);

        this.createVirtualMachineStrategy = createVirtualMachineStrategy;
        this.deleteVirtualMachineStrategy = deleteVirtualMachineStrategy;
        this.publicIpService = publicIpService;
        this.keyPairService = keyPairService;
        this.securityGroupService = securityGroupService;
        this.connectionService = connectionService;
        this.discoveryService = discoveryService;
    }

    @Override public DiscoveryService discoveryService() {
        return discoveryService;
    }

    @Override public void deleteVirtualMachine(String virtualMachineId) {
        checkNotNull(virtualMachineId);
        checkArgument(!virtualMachineId.isEmpty());
        deleteVirtualMachineStrategy.apply(virtualMachineId);
    }

    @Override public VirtualMachine createVirtualMachine(
        final VirtualMachineTemplate virtualMachineTemplate) {
        checkNotNull(virtualMachineTemplate);
        return createVirtualMachineStrategy.apply(virtualMachineTemplate);
    }

    @Override public ConnectionService connectionService() {
        return connectionService;
    }

    @Override public Optional<PublicIpExtension> publicIpExtension() {
        return publicIpService;
    }

    @Override public Optional<KeyPairExtension> keyPairExtension() {
        return keyPairService;
    }

    @Override public Optional<SecurityGroupExtension> securityGroupExtension() {
        return securityGroupService;
    }
}
