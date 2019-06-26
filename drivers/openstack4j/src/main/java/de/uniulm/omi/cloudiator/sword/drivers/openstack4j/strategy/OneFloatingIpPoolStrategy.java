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
import com.google.inject.Provider;
import de.uniulm.omi.cloudiator.sword.util.IdScopeByLocations;
import de.uniulm.omi.cloudiator.sword.util.IdScopedByLocation;
import java.util.List;
import java.util.Optional;
import org.openstack4j.api.OSClient;

/**
 * Retrieves the floating ip pool if there exists only one.
 */
public class OneFloatingIpPoolStrategy implements FloatingIpPoolStrategy {

  private final Provider<OSClient> osClient;

  @Inject
  public OneFloatingIpPoolStrategy(Provider<OSClient> osClient) {
    this.osClient = osClient;
  }

  @Override
  public Optional<String> apply(String virtualMachine) {
    checkNotNull(virtualMachine, "virtualMachine is null.");
    IdScopedByLocation virtualMachineScopedId = IdScopeByLocations.from(virtualMachine);

    final List<String> poolNames =
        osClient.get().useRegion(virtualMachineScopedId.getLocationId()).compute().floatingIps()
            .getPoolNames();
    if (poolNames.size() != 1) {
      return Optional.empty();
    }
    return poolNames.stream().findAny();
  }
}
