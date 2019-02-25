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

package de.uniulm.omi.cloudiator.sword.domain;


import java.util.Optional;
import java.util.Set;

/**
 * Represents a virtual machine.
 */
public interface VirtualMachine extends Resource {

  /**
   * The {@link IpAddress}es of the virtual machine.
   */
  Set<IpAddress> ipAddresses();

  Set<IpAddress> publicIpAddresses();

  Set<IpAddress> privateIpAddresses();

  /**
   * The {@link Image} used for creating the virtual machine.
   *
   * @return {@link Optional} image
   */
  Optional<Image> image();

  /**
   * The id of the image.
   *
   * @return {@link Optional} id of the image
   */
  Optional<String> imageId();

  /**
   * The {@link HardwareFlavor} used for creating the virtual machine.
   *
   * @return {@link Optional} hardware.
   */
  Optional<HardwareFlavor> hardware();

  /**
   * The id of the hardware.
   *
   * @return {@link Optional} id of the hardware
   */
  Optional<String> hardwareId();

  /**
   * {@link Optional} login credentials.
   * <p/>
   * The login credentials are normally only available if the machine was just created.
   *
   * @return optional login credentials.
   */
  Optional<LoginCredential> loginCredential();

  VirtualMachineState remoteState();

  enum VirtualMachineState {

  }

}
