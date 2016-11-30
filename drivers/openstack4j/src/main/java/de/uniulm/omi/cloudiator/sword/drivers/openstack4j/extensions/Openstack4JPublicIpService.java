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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.extensions;

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.api.exceptions.PublicIpException;
import de.uniulm.omi.cloudiator.sword.api.extensions.PublicIpService;
import de.uniulm.omi.cloudiator.sword.api.strategy.PublicIpStrategy;
import de.uniulm.omi.cloudiator.sword.api.util.IdScopedByLocation;
import de.uniulm.omi.cloudiator.sword.core.util.IdScopeByLocations;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.internal.Openstack4JConstants;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.strategy.FloatingIpPoolStrategy;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.compute.ComputeFloatingIPService;
import org.openstack4j.api.compute.ComputeService;
import org.openstack4j.model.compute.FloatingIP;
import org.openstack4j.model.compute.Server;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.*;

/**
 * Basic implementation of a {@link PublicIpStrategy} for Openstack.
 * <p/>
 * todo: check of public ip service and strategy are necessary....
 */
public class Openstack4JPublicIpService implements PublicIpService {

    private final OSClient osClient;
    private final FloatingIpPoolStrategy floatingIpPoolStrategy;


    @Inject public Openstack4JPublicIpService(OSClient osClient,
        FloatingIpPoolStrategy floatingIpPoolStrategy) {


        checkNotNull(osClient, "osClient is null");
        checkNotNull(floatingIpPoolStrategy, "floatingIpPoolStrategy is null");

        this.osClient = osClient;
        this.floatingIpPoolStrategy = floatingIpPoolStrategy;
    }

    private FloatingIP allocateFromPool(ComputeFloatingIPService computeFloatingIPService,
        String virtualMachineId) throws PublicIpException {
        final Optional<String> floatingIpPool = floatingIpPoolStrategy.apply(virtualMachineId);
        if (floatingIpPool.isPresent()) {
            return computeFloatingIPService.allocateIP(floatingIpPool.get());
        }
        throw new PublicIpException(String.format(
            "Need to allocate floatingIp but pool could not be resolved. Try to configure %s.",
            Openstack4JConstants.FLOATING_IP_POOL_PROPERTY));
    }

    private String findPublicIp(ComputeFloatingIPService computeFloatingIPService,
        String virtualMachineId) throws PublicIpException {
        final Set<? extends FloatingIP> unassignedFloatingIps =
            computeFloatingIPService.list().stream()
                .filter((Predicate<FloatingIP>) floatingIP -> floatingIP.getInstanceId() == null)
                .collect(Collectors.toSet());

        if (unassignedFloatingIps.isEmpty()) {
            //no floating ip is present, allocate one from the pool.
            return allocateFromPool(computeFloatingIPService, virtualMachineId)
                .getFloatingIpAddress();
        }
        return unassignedFloatingIps.stream().findAny().get().getFloatingIpAddress();
    }

    private Server server(String virtualMachineId, ComputeService compute) {
        IdScopedByLocation virtualMachineScopedId = IdScopeByLocations.from(virtualMachineId);
        Server server = compute.servers().get(virtualMachineScopedId.getId());
        checkState(server != null,
            "Could not retrieve server with id " + virtualMachineScopedId.getId());
        return server;
    }

    @Override public String addPublicIp(String virtualMachineId) throws PublicIpException {
        checkNotNull(virtualMachineId, "virtualMachineId is null");
        checkArgument(!virtualMachineId.isEmpty(), "virtualMachineId is empty");

        final IdScopedByLocation scopedId = IdScopeByLocations.from(virtualMachineId);

        final ComputeService compute = osClient.useRegion(scopedId.getLocationId()).compute();

        final ComputeFloatingIPService computeFloatingIPService = compute.floatingIps();

        final String publicIp = findPublicIp(computeFloatingIPService, virtualMachineId);

        computeFloatingIPService.addFloatingIP(server(virtualMachineId, compute), publicIp);

        return publicIp;

    }

    @Override public void removePublicIp(String virtualMachineId, String address)
        throws PublicIpException {
        checkNotNull(virtualMachineId, "virtualMachineId is null");
        checkArgument(!virtualMachineId.isEmpty(), "virtualMachineId is empty");

        checkNotNull(address, "address is null.");
        checkArgument(!address.isEmpty(), "address is empty");

        IdScopedByLocation virtualMachineScopedId = IdScopeByLocations.from(virtualMachineId);
        final ComputeService compute =
            osClient.useRegion(virtualMachineScopedId.getLocationId()).compute();
        compute.floatingIps().removeFloatingIP(server(virtualMachineId, compute), address);
    }
}
