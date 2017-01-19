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
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachineTemplate;
import de.uniulm.omi.cloudiator.sword.api.extensions.KeyPairService;
import de.uniulm.omi.cloudiator.sword.api.extensions.PublicIpService;
import de.uniulm.omi.cloudiator.sword.api.extensions.SecurityGroupService;

/**
 * Compute service interface. Offers method for interaction with the compute API of the cloud
 * providers.
 */
public interface ComputeService {

    /**
     * Returns a discovery service offering the discovery of stored entities at the
     * cloud provider
     *
     * @return the discovery service.
     */
    DiscoveryService discoveryService();

    /**
     * Deletes a virtual machine.
     *
     * @param virtualMachineId a mandatory id of the virtual machine.
     * @throws NullPointerException     if the virtual machine id is null.
     * @throws IllegalArgumentException if the virtual machine id is empty.
     */
    void deleteVirtualMachine(String virtualMachineId);

    /**
     * Creates a virtual machine ({@link VirtualMachine}) for the given {@link VirtualMachineTemplate}.
     *
     * @param virtualMachineTemplate mandatory virtual machine template.
     * @return the created virtual machine.
     * @throws NullPointerException if the virtual machine template is null.
     */
    VirtualMachine createVirtualMachine(VirtualMachineTemplate virtualMachineTemplate);

    /**
     * Returns a {@link ConnectionService} that is used for connecting to
     * virtual machines.
     *
     * @return a connection service.
     */
    ConnectionService connectionService();

    /**
     * Returns an {@link Optional} {@link PublicIpService} for the cloud provider.
     *
     * @return an optional public ip service.
     */
    Optional<PublicIpService> publicIpService();

    /**
     * Returns an {@link Optional} {@link KeyPairService} for the cloud provider.
     *
     * @return an optional key pair service.
     */
    Optional<KeyPairService> keyPairService();

    /**
     * Returns an {@link Optional} {@link SecurityGroupService} for the cloud provider.
     *
     * @return an optional security group service.
     */
    Optional<SecurityGroupService> securityGroupService();
}
