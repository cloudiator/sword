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

package de.uniulm.omi.cloudiator.sword.api.service;

import com.google.common.base.Optional;
import com.google.common.net.HostAndPort;
import de.uniulm.omi.cloudiator.sword.api.domain.*;
import de.uniulm.omi.cloudiator.sword.api.extensions.PublicIpService;
import de.uniulm.omi.cloudiator.sword.api.ssh.SshConnection;

import javax.annotation.Nullable;

/**
 * Created by daniel on 29.10.14.
 */
public interface ComputeService {

    @Nullable
    public Image getImage(String id);

    @Nullable
    public VirtualMachine getVirtualMachine(String id);

    @Nullable
    public Location getLocation(String id);

    @Nullable
    public HardwareFlavor getHardwareFlavor(String id);

    public Iterable<HardwareFlavor> listHardwareFlavors();

    public Iterable<Image> listImages();

    public Iterable<Location> listLocations();

    public Iterable<VirtualMachine> listVirtualMachines();

    public void deleteVirtualMachine(String virtualMachineId);

    public VirtualMachine createVirtualMachine(VirtualMachineTemplate virtualMachineTemplate);

    public SshConnection getSshConnection(HostAndPort hostAndPort);

    public Optional<PublicIpService> getPublicIpService();
}
