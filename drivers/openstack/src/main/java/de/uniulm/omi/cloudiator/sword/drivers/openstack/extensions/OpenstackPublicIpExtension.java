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

package de.uniulm.omi.cloudiator.sword.drivers.openstack.extensions;

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.api.extensions.PublicIpExtension;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.strategy.OpenstackFloatingIpStrategy;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Implementation of the {@link PublicIpExtension} interface for Openstack.
 */
public class OpenstackPublicIpExtension implements PublicIpExtension {

    private final OpenstackFloatingIpStrategy openstackFloatingIpStrategy;

    /**
     * @param openstackFloatingIpStrategy a mandatory strategy for assigning public ips in openstack.
     */
    @Inject public OpenstackPublicIpExtension(
        OpenstackFloatingIpStrategy openstackFloatingIpStrategy) {
        checkNotNull(openstackFloatingIpStrategy);
        this.openstackFloatingIpStrategy = openstackFloatingIpStrategy;
    }

    @Override public String addPublicIp(String virtualMachineId)  {
        checkNotNull(virtualMachineId);
        checkArgument(!virtualMachineId.isEmpty());
        return this.openstackFloatingIpStrategy.assignPublicIpToVirtualMachine(virtualMachineId);
    }

    @Override public void removePublicIp(String virtualMachineId, String address) {

        checkNotNull(virtualMachineId);
        checkArgument(!virtualMachineId.isEmpty());
        checkNotNull(address);
        checkArgument(!address.isEmpty());

        this.openstackFloatingIpStrategy
            .removePublicIpFromVirtualMachine(virtualMachineId, address);
    }
}
