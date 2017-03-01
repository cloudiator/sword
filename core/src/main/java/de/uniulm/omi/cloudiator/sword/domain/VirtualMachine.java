/*
 * Copyright (c) 2014-2017 University of Ulm
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



import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;
import java.util.Set;

/**
 * Represents a virtual machine.
 */
public interface VirtualMachine extends Resource {

    enum State {

    }

    /**
     * The public ip addresses under which this virtual machine is reachable.
     *
     * @return an immutable set of ip addresses.
     */
    Set<String> publicAddresses();

    /**
     * The private up addresses assigned to the virtual machine.
     *
     * @return an immutable set of up addresses.
     */
    Set<String> privateAddresses();

    /**
     * The {@link Image} used for creating the virtual machine.
     *
     * @return {@link Optional} image
     */
    Optional<Image> image();

    /**
     * The {@link HardwareFlavor} used for creating the virtual machine.
     *
     * @return {@link Optional} hardware.
     */
    Optional<HardwareFlavor> hardware();

    /**
     * {@link Optional} login credentials.
     * <p/>
     * The login credentials are normally only available
     * if the machine was just created.
     *
     * @return optional login credentials.
     */
    Optional<LoginCredential> loginCredential();

    State state();

}
