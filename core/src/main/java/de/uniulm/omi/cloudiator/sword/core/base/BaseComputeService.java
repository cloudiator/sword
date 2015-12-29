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

package de.uniulm.omi.cloudiator.sword.core.base;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.api.ServiceConfiguration;
import de.uniulm.omi.cloudiator.sword.api.domain.*;
import de.uniulm.omi.cloudiator.sword.api.extensions.KeyPairService;
import de.uniulm.omi.cloudiator.sword.api.extensions.MigrationService;
import de.uniulm.omi.cloudiator.sword.api.extensions.PublicIpService;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnectionFactory;
import de.uniulm.omi.cloudiator.sword.api.service.ComputeService;
import de.uniulm.omi.cloudiator.sword.api.service.ConnectionService;
import de.uniulm.omi.cloudiator.sword.api.service.DiscoveryService;
import de.uniulm.omi.cloudiator.sword.api.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.api.strategy.DeleteVirtualMachineStrategy;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 02.12.14.
 */
public class BaseComputeService
    implements ComputeService<HardwareFlavor, Image, Location, VirtualMachine> {

    private final DiscoveryService<HardwareFlavor, Image, Location, VirtualMachine>
        discoveryService;
    private final CreateVirtualMachineStrategy createVirtualMachineStrategy;
    private final DeleteVirtualMachineStrategy deleteVirtualMachineStrategy;
    private final Optional<PublicIpService> publicIpService;
    private final Optional<KeyPairService> keyPairService;
    private final ConnectionService connectionService;
    private final Optional<MigrationService> migrationService;


    @Inject public BaseComputeService(CreateVirtualMachineStrategy createVirtualMachineStrategy,
        DeleteVirtualMachineStrategy deleteVirtualMachineStrategy,
        RemoteConnectionFactory remoteConnectionFactory, ServiceConfiguration serviceConfiguration,
        DiscoveryService<HardwareFlavor, Image, Location, VirtualMachine> discoveryService,
        Optional<PublicIpService> publicIpService, Optional<KeyPairService> keyPairService,
        ConnectionService connectionService, Optional<MigrationService> migrationService) {

        checkNotNull(createVirtualMachineStrategy);
        checkNotNull(deleteVirtualMachineStrategy);
        checkNotNull(remoteConnectionFactory);
        checkNotNull(serviceConfiguration);
        checkNotNull(publicIpService);
        checkNotNull(keyPairService);
        checkNotNull(connectionService);
        checkNotNull(discoveryService);
        checkNotNull(migrationService);

        this.createVirtualMachineStrategy = createVirtualMachineStrategy;
        this.deleteVirtualMachineStrategy = deleteVirtualMachineStrategy;
        this.publicIpService = publicIpService;
        this.keyPairService = keyPairService;
        this.connectionService = connectionService;
        this.discoveryService = discoveryService;
        this.migrationService = migrationService;
    }

    @Override
    public DiscoveryService<HardwareFlavor, Image, Location, VirtualMachine> discoveryService() {
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

    @Override public Optional<PublicIpService> publicIpService() {
        return publicIpService;
    }

    @Override public Optional<KeyPairService> keyPairService() {
        return keyPairService;
    }

    @Override public Optional<MigrationService> migrationService() {
        return migrationService;
    }
}
