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

package de.uniulm.omi.cloudiator.sword.drivers.openstack.strategy;

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.api.exceptions.PublicIpException;
import de.uniulm.omi.cloudiator.sword.api.strategy.PublicIpStrategy;
import de.uniulm.omi.cloudiator.sword.api.util.IdScopedByLocation;
import de.uniulm.omi.cloudiator.sword.core.util.IdScopeByLocations;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.OpenstackFloatingIpClient;
import org.jclouds.openstack.nova.v2_0.domain.FloatingIP;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Basic implementation of a {@link PublicIpStrategy} for Openstack.
 * <p/>
 * todo: check of public ip service and strategy are necessary....
 */
public class OpenstackFloatingIpStrategy implements PublicIpStrategy {

    private final OpenstackFloatingIpClient openstackFloatingIpClient;
    private final FloatingIpPoolSupplier floatingIpPoolSupplier;

    /**
     * @param openstackFloatingIpClient a reference to the openstack floating ip client.
     * @param floatingIpPoolSupplier    a reference to the openstack floating ip supplier
     */
    @Inject public OpenstackFloatingIpStrategy(OpenstackFloatingIpClient openstackFloatingIpClient,
        FloatingIpPoolSupplier floatingIpPoolSupplier) {
        checkNotNull(floatingIpPoolSupplier);
        this.floatingIpPoolSupplier = floatingIpPoolSupplier;
        checkNotNull(openstackFloatingIpClient);
        this.openstackFloatingIpClient = openstackFloatingIpClient;
    }

    /**
     * @todo check if floating ip client is available in the given region?
     * @todo handle pools (Property?) auto resolve of name?
     * @todo maybe throw some other exception
     * @todo maybe retry assignment because of race condition?
     */
    @Override public String assignPublicIpToVirtualMachine(String virtualMachineId)
        throws PublicIpException {

        synchronized (OpenstackFloatingIpStrategy.class) {

            checkNotNull(virtualMachineId);
            checkArgument(!virtualMachineId.isEmpty());

            FloatingIP toAssign = null;
            IdScopedByLocation virtualMachineScopedId = IdScopeByLocations.from(virtualMachineId);

            //check if the floating ip service is available in the region of the virtual machine
            if (!openstackFloatingIpClient.isAvailable(virtualMachineScopedId.getLocationId())) {
                throw new PublicIpException(
                    "Openstack floating IP extension is not available in region "
                        + virtualMachineScopedId.getLocationId() + ".");
            }

            //loop all available floating ips, and check if we have an unassigned.
            for (FloatingIP floatingIP : this.openstackFloatingIpClient
                .list(virtualMachineScopedId.getLocationId())) {
                if (floatingIP.getInstanceId() == null) {
                    toAssign = floatingIP;
                    break;
                }
            }
            if (toAssign == null && floatingIpPoolSupplier.get().isPresent()) {
                // we found nothing to assign, next step is trying to allocate
                toAssign = this.openstackFloatingIpClient
                    .allocateFromPool(floatingIpPoolSupplier.get().get(),
                        virtualMachineScopedId.getLocationId());

            }
            if (toAssign == null) {
                // no other idea -> throw exception
                throw new PublicIpException(
                    "Neither possible to assign empty nor allocate new ip.");
            }
            // finally assign the found ip
            this.openstackFloatingIpClient
                .addToServer(virtualMachineScopedId.getLocationId(), toAssign.getIp(),
                    virtualMachineScopedId.getId());
            return toAssign.getIp();
        }
    }


    /**
     * @todo make configurable if floating ip should only be detached from machine or if it should be deallocated.
     * Current implementation only removes it from virtual machine
     */
    @Override public void removePublicIpFromVirtualMachine(String virtualMachineId, String address)
        throws PublicIpException {

        checkNotNull(virtualMachineId);
        checkArgument(!virtualMachineId.isEmpty());

        checkNotNull(address);
        checkArgument(!address.isEmpty());


        IdScopedByLocation virtualMachineScopedId = IdScopeByLocations.from(virtualMachineId);

        if (!openstackFloatingIpClient.isAvailable(virtualMachineScopedId.getLocationId())) {
            throw new PublicIpException(
                "Openstack floating IP extension is not available in region "
                    + virtualMachineScopedId.getLocationId() + ".");
        }

        this.openstackFloatingIpClient
            .removeFromServer(virtualMachineScopedId.getLocationId(), address,
                virtualMachineScopedId.getId());
    }
}