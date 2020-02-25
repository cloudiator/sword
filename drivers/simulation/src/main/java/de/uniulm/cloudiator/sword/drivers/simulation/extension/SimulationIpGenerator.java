/*
 * Copyright (c) 2014-2019 University of Ulm
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.uniulm.cloudiator.sword.drivers.simulation.extension;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.inject.Inject;
import de.uniulm.cloudiator.sword.drivers.simulation.state.VirtualMachineStore;
import de.uniulm.omi.cloudiator.sword.domain.IpAddress;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachineBuilder;
import de.uniulm.omi.cloudiator.sword.extensions.PublicIpExtension;

public class SimulationIpGenerator implements PublicIpExtension {


  private final VirtualMachineStore virtualMachineStore;

  @Inject
  public SimulationIpGenerator(
      VirtualMachineStore virtualMachineStore) {
    this.virtualMachineStore = virtualMachineStore;
  }

  @Override
  public IpAddress addPublicIp(String virtualMachineId) {
    final IpAddress generated = IpGenerator.generatePublic();

    checkArgument(virtualMachineStore.get(virtualMachineId) != null,
        String.format("VirtualMachine with the id %s does not exist.", virtualMachineId));

    final VirtualMachine updated = VirtualMachineBuilder
        .of(virtualMachineStore.get(virtualMachineId))
        .addIpAddress(generated).build();

    virtualMachineStore.update(updated);

    return generated;

  }

  @Override
  public void removePublicIp(String virtualMachineId, String address) {
    //todo
    throw new UnsupportedOperationException("Removing ips is currently not supported");
  }
}
