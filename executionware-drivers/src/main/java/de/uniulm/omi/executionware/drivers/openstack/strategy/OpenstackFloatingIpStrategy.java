/*
 * Copyright (c) 2015 University of Ulm
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

package de.uniulm.omi.executionware.drivers.openstack.strategy;

import com.google.inject.Inject;
import de.uniulm.omi.executionware.api.strategy.PublicIpStrategy;
import de.uniulm.omi.executionware.drivers.OpenstackFloatingIpClient;
import org.jclouds.openstack.nova.v2_0.domain.FloatingIP;

/**
 * Created by daniel on 19.01.15.
 */
public class OpenstackFloatingIpStrategy implements PublicIpStrategy {

    private final OpenstackFloatingIpClient openstackFloatingIpClient;

    @Inject
    public OpenstackFloatingIpStrategy(OpenstackFloatingIpClient openstackFloatingIpClient) {
        this.openstackFloatingIpClient = openstackFloatingIpClient;
    }

    @Override
    /**
     * @todo handle pools (Property?)
     * @todo maybe throw some other exception
     * @todo maybe retry assignment because of race condition?
     */
    public String assignPublicIpToVirtualMachine(String virtualMachineId) {
        FloatingIP toAssign = null;

        //loop all available floating ips, and check if we have an unassigned.
        for (FloatingIP floatingIP : this.openstackFloatingIpClient.list(getRegionFromVirtualMachineId(virtualMachineId))) {
            if (floatingIP.getInstanceId() == null) {
                toAssign = floatingIP;
                break;
            }
        }
        if (toAssign == null) {
            // we found nothing to assign, next step is trying to allocate
            toAssign = this.openstackFloatingIpClient.create(getRegionFromVirtualMachineId(virtualMachineId));

        }
        if (toAssign == null) {
            // no other idea -> throw exception
            throw new RuntimeException("Neither possible to assign empty nor allocate new ip.");
        }
        // finally assign the found ip
        this.openstackFloatingIpClient.addToServer(getRegionFromVirtualMachineId(virtualMachineId), toAssign.getIp(), getVirtualMachineId(virtualMachineId));
        return toAssign.getIp();
    }

    @Override
    /**
     * @todo make configurable if floating ip should only be detached from machine or if it should be deallocated.
     * Current implementation only removes it from virtual machine
     */
    public void removePublicIpFromVirtualMachine(String virtualMachineId, String address) {
        this.openstackFloatingIpClient.removeFromServer(getRegionFromVirtualMachineId(virtualMachineId), address, getVirtualMachineId(virtualMachineId));
    }

    /**
     * @todo: we need this at many places, refactor it.
     */
    protected String getRegionFromVirtualMachineId(String virtualMachineId) {
        return virtualMachineId.split("/")[0];
    }

    /**
     * @todo: we need this at many places, refactor it.
     */
    protected String getVirtualMachineId(String virtualMachineId) {
        return virtualMachineId.split("/")[1];
    }
}
