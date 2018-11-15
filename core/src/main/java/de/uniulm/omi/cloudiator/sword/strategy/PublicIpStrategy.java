/*
 * Copyright (c) 2014-2018 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.strategy;

/**
 * The strategy interface for the (de-)assignment of public ip.
 */
public interface PublicIpStrategy {

  /**
   * Assigns a public ip to a virtual machine.
   *
   * @param virtualMachineId the unique identifier for the virtual machine.
   * @return the assigned public ip.
   * @throws NullPointerException if the id is null
   * @throws IllegalArgumentException if the id is empty.
   */
  String assignPublicIpToVirtualMachine(String virtualMachineId);

  /**
   * Removes a public ip from a virtual machine.
   *
   * @param virtualMachineId the unique identifier for the virtual machine.
   * @param address the address to remove.
   * @throws NullPointerException if any of the argument is null.
   * @throws IllegalArgumentException if any of the string arguments is empty.
   */
  void removePublicIpFromVirtualMachine(String virtualMachineId, String address);

}
