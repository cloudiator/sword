/*
 * Copyright (c) 2014-2016 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.strategy;


import java.util.Optional;

/**
 * Supplies the name of the floating ip pool to use
 * when trying to allocate new floating ip addresses for the given virtual machine
 */
public interface FloatingIpPoolStrategy {

    /**
     * Supplies an floating ip pool for the given vm id.
     *
     * @param virtualMachine the id of the virtual machine.
     * @return an {@link Optional} identifier for the floating ip pool.
     */
    Optional<String> apply(String virtualMachine);
}
