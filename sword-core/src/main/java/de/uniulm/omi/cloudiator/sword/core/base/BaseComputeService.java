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
import com.google.common.net.HostAndPort;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.api.ServiceConfiguration;
import de.uniulm.omi.cloudiator.sword.api.domain.*;
import de.uniulm.omi.cloudiator.sword.api.extensions.PublicIpService;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnection;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnectionFactory;
import de.uniulm.omi.cloudiator.sword.api.service.ComputeService;
import de.uniulm.omi.cloudiator.sword.api.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.api.strategy.DeleteVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.api.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.sword.api.supplier.Supplier;

import javax.annotation.Nullable;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 02.12.14.
 */
public class BaseComputeService implements ComputeService {

    private final Supplier<Set<Image>> imageSupplier;
    private final Supplier<Set<Location>> locationSupplier;
    private final Supplier<Set<HardwareFlavor>> hardwareFlavorSupplier;
    private final Supplier<Set<VirtualMachine>> virtualMachineSupplier;
    private final CreateVirtualMachineStrategy createVirtualMachineStrategy;
    private final DeleteVirtualMachineStrategy deleteVirtualMachineStrategy;
    private final RemoteConnectionFactory remoteConnectionFactory;
    private final ServiceConfiguration serviceConfiguration;
    private final Optional<PublicIpService> publicIpService;
    private final GetStrategy<String, Image> imageGetStrategy;
    private final GetStrategy<String, Location> locationGetStrategy;
    private final GetStrategy<String, HardwareFlavor> hardwareFlavorGetStrategy;
    private final GetStrategy<String, VirtualMachine> virtualMachineGetStrategy;

    @Inject
    public BaseComputeService(Supplier<Set<Image>> imageSupplier,
                              Supplier<Set<Location>> locationSupplier,
                              Supplier<Set<HardwareFlavor>> hardwareFlavorSupplier,
                              Supplier<Set<VirtualMachine>> virtualMachineSupplier,
                              GetStrategy<String, Image> imageGetStrategy,
                              GetStrategy<String, Location> locationGetStrategy,
                              GetStrategy<String, HardwareFlavor> hardwareFlavorGetStrategy,
                              GetStrategy<String, VirtualMachine> virtualMachineGetStrategy,
                              CreateVirtualMachineStrategy createVirtualMachineStrategy,
                              DeleteVirtualMachineStrategy deleteVirtualMachineStrategy,
                              RemoteConnectionFactory remoteConnectionFactory, ServiceConfiguration serviceConfiguration,
                              Optional<PublicIpService> publicIpService) {

        checkNotNull(imageSupplier);
        checkNotNull(locationSupplier);
        checkNotNull(hardwareFlavorSupplier);
        checkNotNull(virtualMachineSupplier);
        checkNotNull(createVirtualMachineStrategy);
        checkNotNull(deleteVirtualMachineStrategy);
        checkNotNull(remoteConnectionFactory);
        checkNotNull(serviceConfiguration);
        checkNotNull(publicIpService);
        checkNotNull(imageGetStrategy);
        checkNotNull(locationGetStrategy);
        checkNotNull(hardwareFlavorGetStrategy);
        checkNotNull(virtualMachineGetStrategy);
        this.imageSupplier = imageSupplier;
        this.locationSupplier = locationSupplier;
        this.hardwareFlavorSupplier = hardwareFlavorSupplier;
        this.virtualMachineSupplier = virtualMachineSupplier;
        this.createVirtualMachineStrategy = createVirtualMachineStrategy;
        this.deleteVirtualMachineStrategy = deleteVirtualMachineStrategy;
        this.remoteConnectionFactory = remoteConnectionFactory;
        this.serviceConfiguration = serviceConfiguration;
        this.publicIpService = publicIpService;
        this.imageGetStrategy = imageGetStrategy;
        this.locationGetStrategy = locationGetStrategy;
        this.hardwareFlavorGetStrategy = hardwareFlavorGetStrategy;
        this.virtualMachineGetStrategy = virtualMachineGetStrategy;
    }

    @Override
    @Nullable
    public Image getImage(String id) {
        checkNotNull(id);
        return this.imageGetStrategy.get(id);
    }

    @Override
    @Nullable
    public VirtualMachine getVirtualMachine(String id) {
        checkNotNull(id);
        return this.virtualMachineGetStrategy.get(id);
    }

    @Override
    @Nullable
    public Location getLocation(String id) {
        checkNotNull(id);
        return this.locationGetStrategy.get(id);
    }

    @Override
    @Nullable
    public HardwareFlavor getHardwareFlavor(String id) {
        checkNotNull(id);
        return this.hardwareFlavorGetStrategy.get(id);
    }

    @Override
    public Iterable<HardwareFlavor> listHardwareFlavors() {
        return this.hardwareFlavorSupplier.get();
    }

    @Override
    public Iterable<Image> listImages() {
        return this.imageSupplier.get();
    }

    @Override
    public Iterable<Location> listLocations() {
        return this.locationSupplier.get();
    }

    @Override
    public Iterable<VirtualMachine> listVirtualMachines() {
        return this.virtualMachineSupplier.get();
    }

    @Override
    public void deleteVirtualMachine(String virtualMachineId) {
        this.deleteVirtualMachineStrategy.apply(virtualMachineId);
    }

    @Override
    public VirtualMachine createVirtualMachine(
            final VirtualMachineTemplate virtualMachineTemplate) {
        return this.createVirtualMachineStrategy.apply(virtualMachineTemplate);
    }

    @Override
    public RemoteConnection getRemoteConnection(HostAndPort hostAndPort, OSFamily osFamily) {
        return this.remoteConnectionFactory.createRemoteConnection(hostAndPort.getHostText(), osFamily, serviceConfiguration.getLoginCredential(), hostAndPort.getPort());
    }

    @Override
    public Optional<PublicIpService> getPublicIpService() {
        return this.publicIpService;
    }
}
