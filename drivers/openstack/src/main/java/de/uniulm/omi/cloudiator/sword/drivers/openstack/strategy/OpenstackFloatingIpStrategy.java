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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.OpenstackFloatingIpClient;
import de.uniulm.omi.cloudiator.sword.strategy.PublicIpStrategy;
import de.uniulm.omi.cloudiator.sword.util.IdScopeByLocations;
import de.uniulm.omi.cloudiator.sword.util.IdScopedByLocation;
import org.jclouds.openstack.nova.v2_0.domain.FloatingIP;

/**
 * Basic implementation of a {@link PublicIpStrategy} for Openstack.
 * <p/>
 * todo: check of public ip service and strategy are necessary....
 */
public class OpenstackFloatingIpStrategy implements PublicIpStrategy {

  private final OpenstackFloatingIpClient openstackFloatingIpClient;
  private final FloatingIpPoolStrategy floatingIpPoolStrategy;

  /**
   * @param openstackFloatingIpClient a reference to the openstack floating ip client.
   * @param floatingIpPoolStrategy a reference to the openstack floating ip supplier
   */
  @Inject
  public OpenstackFloatingIpStrategy(OpenstackFloatingIpClient openstackFloatingIpClient,
      FloatingIpPoolStrategy floatingIpPoolStrategy) {
    checkNotNull(floatingIpPoolStrategy);
    this.floatingIpPoolStrategy = floatingIpPoolStrategy;
    checkNotNull(openstackFloatingIpClient);
    this.openstackFloatingIpClient = openstackFloatingIpClient;
  }

  /**
   * @todo check if floating ip client is available in the given region?
   * @todo handle pools (Property?) auto resolve of name?
   * @todo maybe throw some other exception
   * @todo maybe retry assignment because of race condition?
   */
  @Override
  public String assignPublicIpToVirtualMachine(String virtualMachineId) {

    synchronized (OpenstackFloatingIpStrategy.class) {

      checkNotNull(virtualMachineId);
      checkArgument(!virtualMachineId.isEmpty());

      FloatingIP toAssign = null;
      IdScopedByLocation virtualMachineScopedId = IdScopeByLocations.from(virtualMachineId);

      //check if the floating ip service is available in the region of the virtual machine
      if (!openstackFloatingIpClient.isAvailable(virtualMachineScopedId.getLocationId())) {
        throw new IllegalStateException(
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
      if (toAssign == null && floatingIpPoolStrategy.apply(virtualMachineId).isPresent()) {
        // we found nothing to assign, next step is trying to allocate
        toAssign = this.openstackFloatingIpClient
            .allocateFromPool(floatingIpPoolStrategy.apply(virtualMachineId).get(),
                virtualMachineScopedId.getLocationId());

      }
      if (toAssign == null) {
        // no other idea -> throw exception
        throw new IllegalStateException(
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
   * @todo make configurable if floating ip should only be detached from machine or if it should be
   * deallocated. Current implementation only removes it from virtual machine
   */
  @Override
  public void removePublicIpFromVirtualMachine(String virtualMachineId,
      String address) {

    checkNotNull(virtualMachineId);
    checkArgument(!virtualMachineId.isEmpty());

    checkNotNull(address);
    checkArgument(!address.isEmpty());

    IdScopedByLocation virtualMachineScopedId = IdScopeByLocations.from(virtualMachineId);

    if (!openstackFloatingIpClient.isAvailable(virtualMachineScopedId.getLocationId())) {
      throw new IllegalStateException(
          "Openstack floating IP extension is not available in region "
              + virtualMachineScopedId.getLocationId() + ".");
    }

    this.openstackFloatingIpClient
        .removeFromServer(virtualMachineScopedId.getLocationId(), address,
            virtualMachineScopedId.getId());
  }
}
