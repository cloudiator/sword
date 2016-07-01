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

package de.uniulm.omi.cloudiator.sword.drivers.openstack.strategy;

import com.google.common.base.Optional;
import de.uniulm.omi.cloudiator.sword.api.util.IdScopedByLocation;
import de.uniulm.omi.cloudiator.sword.core.util.IdScopeByLocations;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.extensions.FloatingIPPoolApi;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Retrieves the floating ip pool if there exists only one.
 */
public class OneFloatingIpPoolStrategy implements FloatingIpPoolStrategy {

    private final NovaApi novaApi;

    public OneFloatingIpPoolStrategy(NovaApi novaApi) {
        this.novaApi = novaApi;
    }

    @Override public Optional<String> apply(String virtualMachine) {
        checkNotNull(virtualMachine);
        IdScopedByLocation virtualMachineScopedId = IdScopeByLocations.from(virtualMachine);

        final Optional<FloatingIPPoolApi> optionalFloatingIPPoolApi =
            novaApi.getFloatingIPPoolApi(virtualMachineScopedId.getLocationId());
        if (!optionalFloatingIPPoolApi.isPresent()) {
            return Optional.absent();
        }
        final FloatingIPPoolApi floatingIPPoolApi = optionalFloatingIPPoolApi.get();
        if (floatingIPPoolApi.list().size() != 1) {
            return Optional.absent();
        }

        return Optional.of(floatingIPPoolApi.list().get(0).getName());
    }
}
