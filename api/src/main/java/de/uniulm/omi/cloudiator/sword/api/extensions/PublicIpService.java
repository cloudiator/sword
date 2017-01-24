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

package de.uniulm.omi.cloudiator.sword.api.extensions;

import de.uniulm.omi.cloudiator.sword.api.exceptions.PublicIpException;

/**
 * An interface that handles the assignment of public ips to
 * virtual machines.
 */
public interface PublicIpService {

    /**
     * Adds a public ip to the virtual machine.
     *
     * @param virtualMachineId the unique identifier for the virtual machine.
     * @return the assigned ip address as string (mandatory).
     * @throws NullPointerException     if the virtualMachineId is null
     * @throws IllegalArgumentException if the virtualMachineId is empty.
     * @todo define format of the address.
     */
    String addPublicIp(String virtualMachineId);

    /**
     * Removes the public ip from the virtual machine.
     *
     * @param virtualMachineId the unique identifier for the virtual machine (mandatory).
     * @param address          the ip address which shall be removed (mandatory).
     * @throws NullPointerException     if one of the argument is null.
     * @throws IllegalArgumentException if one of the arguments is empty.
     * @todo define format of the address.
     */
    void removePublicIp(String virtualMachineId, String address);
}
