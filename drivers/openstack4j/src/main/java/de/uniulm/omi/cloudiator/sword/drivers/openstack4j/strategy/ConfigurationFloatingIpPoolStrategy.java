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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.strategy;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.internal.Openstack4JConstants;
import java.util.Optional;


/**
 * Retrieves the floating ip pool by reading the configuration.
 */
public class ConfigurationFloatingIpPoolStrategy implements FloatingIpPoolStrategy {

  private @Inject(optional = true)
  @Named(Openstack4JConstants.FLOATING_IP_POOL_PROPERTY)
  String
      floatingIpPoolName = null;

  @Override
  public Optional<String> apply(String virtualMachine) {
    checkNotNull(virtualMachine);
    return Optional.ofNullable(floatingIpPoolName);
  }
}
