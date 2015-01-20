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

package de.uniulm.omi.executionware.core.base;

import com.google.common.base.Optional;
import com.google.common.net.HostAndPort;
import com.google.inject.Inject;
import de.uniulm.omi.executionware.api.ServiceConfiguration;
import de.uniulm.omi.executionware.api.domain.*;
import de.uniulm.omi.executionware.api.extensions.PublicIpService;
import de.uniulm.omi.executionware.api.service.ComputeService;
import de.uniulm.omi.executionware.api.ssh.SshConnection;
import de.uniulm.omi.executionware.api.ssh.SshConnectionFactory;
import de.uniulm.omi.executionware.api.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.executionware.api.strategy.DeleteVirtualMachineStrategy;
import de.uniulm.omi.executionware.api.supplier.Supplier;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 02.12.14.
 */
public class BaseComputeService implements ComputeService {

    private final Supplier<Set<? extends Image>> imageSupplier;
    private final Supplier<Set<? extends Location>> locationSupplier;
    private final Supplier<Set<? extends HardwareFlavor>> hardwareFlavorSupplier;
    private final Supplier<Set<? extends VirtualMachine>> virtualMachineSupplier;
    private final CreateVirtualMachineStrategy createVirtualMachineStrategy;
    private final DeleteVirtualMachineStrategy deleteVirtualMachineStrategy;
    private final SshConnectionFactory sshConnectionFactory;
    private final ServiceConfiguration serviceConfiguration;
    private final Optional<PublicIpService> publicIpService;

    @Inject
    public BaseComputeService(
            Supplier<Set<? extends Image>> imageSupplier,
            Supplier<Set<? extends Location>> locationSupplier,
            Supplier<Set<? extends HardwareFlavor>> hardwareFlavorSupplier,
            Supplier<Set<? extends VirtualMachine>> virtualMachineSupplier,
            CreateVirtualMachineStrategy createVirtualMachineStrategy,
            DeleteVirtualMachineStrategy deleteVirtualMachineStrategy,
            SshConnectionFactory sshConnectionFactory,
            ServiceConfiguration serviceConfiguration,
            Optional<PublicIpService> publicIpService
    ) {

        checkNotNull(imageSupplier);
        checkNotNull(locationSupplier);
        checkNotNull(hardwareFlavorSupplier);
        checkNotNull(virtualMachineSupplier);
        checkNotNull(createVirtualMachineStrategy);
        checkNotNull(deleteVirtualMachineStrategy);
        checkNotNull(sshConnectionFactory);
        checkNotNull(serviceConfiguration);
        checkNotNull(publicIpService);
        this.imageSupplier = imageSupplier;
        this.locationSupplier = locationSupplier;
        this.hardwareFlavorSupplier = hardwareFlavorSupplier;
        this.virtualMachineSupplier = virtualMachineSupplier;
        this.createVirtualMachineStrategy = createVirtualMachineStrategy;
        this.deleteVirtualMachineStrategy = deleteVirtualMachineStrategy;
        this.sshConnectionFactory = sshConnectionFactory;
        this.serviceConfiguration = serviceConfiguration;
        this.publicIpService = publicIpService;
    }

    @Override
    public Image getImage() {
        return null;
    }

    @Override
    public VirtualMachine getVirtualMachine() {
        return null;
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public HardwareFlavor getFlavor() {
        return null;
    }

    @Override
    public Iterable<? extends HardwareFlavor> listHardwareFlavors() {
        return this.hardwareFlavorSupplier.get();
    }

    @Override
    public Iterable<? extends Image> listImages() {
        return this.imageSupplier.get();
    }

    @Override
    public Iterable<? extends Location> listLocations() {
        return this.locationSupplier.get();
    }

    @Override
    public Iterable<? extends VirtualMachine> listVirtualMachines() {
        return this.virtualMachineSupplier.get();
    }

    @Override
    public void deleteVirtualMachine(String virtualMachineId) {
        this.deleteVirtualMachineStrategy.apply(virtualMachineId);
    }

    @Override
    public VirtualMachine createVirtualMachine(final VirtualMachineTemplate virtualMachineTemplate) {
        return this.createVirtualMachineStrategy.apply(virtualMachineTemplate);
    }

    @Override
    public SshConnection getSshConnection(HostAndPort hostAndPort) {
        return this.sshConnectionFactory.create(hostAndPort, serviceConfiguration.getLoginCredential());
    }

    @Override
    public Optional<PublicIpService> getPublicIpService() {
        return this.publicIpService;
    }
}
