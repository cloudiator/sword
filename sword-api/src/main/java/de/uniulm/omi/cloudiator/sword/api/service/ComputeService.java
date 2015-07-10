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
import de.uniulm.omi.cloudiator.sword.api.extensions.KeyPairService;
import de.uniulm.omi.cloudiator.sword.api.extensions.PublicIpService;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnection;

/**
 * Created by daniel on 29.10.14.
 */
public interface ComputeService<H extends HardwareFlavor, I extends Image, L extends Location, V extends VirtualMachine>
    extends DiscoveryService<H, I, L, V> {

    void deleteVirtualMachine(String virtualMachineId);

    V createVirtualMachine(VirtualMachineTemplate virtualMachineTemplate);

    RemoteConnection getRemoteConnection(HostAndPort hostAndPort, OSFamily osFamily,
        LoginCredential loginCredential);

    Optional<PublicIpService> getPublicIpService();

    Optional<KeyPairService> getKeyPairService();
}
