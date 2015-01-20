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

package de.uniulm.omi.executionware.drivers.openstack.extendsions;

import com.google.inject.Inject;
import de.uniulm.omi.executionware.api.extensions.PublicIpService;
import de.uniulm.omi.executionware.drivers.openstack.strategy.OpenstackFloatingIpStrategy;

/**
 * Created by daniel on 19.01.15.
 */
public class OpenstackPublicIpService implements PublicIpService {

    private final OpenstackFloatingIpStrategy openstackFloatingIpStrategy;

    @Inject
    public OpenstackPublicIpService(OpenstackFloatingIpStrategy openstackFloatingIpStrategy) {
        this.openstackFloatingIpStrategy = openstackFloatingIpStrategy;
    }

    @Override
    public String addPublicIp(String virtualMachineId) {
        return this.openstackFloatingIpStrategy.assignPublicIpToVirtualMachine(virtualMachineId);
    }

    @Override
    public void removePublicIp(String virtualMachineId, String address) {
        this.openstackFloatingIpStrategy.removePublicIpFromVirtualMachine(virtualMachineId, address);
    }
}
