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

package de.uniulm.omi.executionware.drivers.openstack;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import de.uniulm.omi.executionware.api.ServiceConfiguration;
import de.uniulm.omi.executionware.drivers.openstack.OpenstackFloatingIpClient;
import org.jclouds.ContextBuilder;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.FloatingIP;
import org.jclouds.openstack.nova.v2_0.extensions.FloatingIPApi;

import javax.annotation.Nullable;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 19.01.15.
 */
public class OpenstackFloatingIpClientImpl implements OpenstackFloatingIpClient {

    private final NovaApi novaApi;

    @Inject
    public OpenstackFloatingIpClientImpl(ServiceConfiguration serviceConfiguration) {
        checkNotNull(serviceConfiguration);

        this.novaApi = ContextBuilder.newBuilder(serviceConfiguration.getProvider())
                .endpoint(serviceConfiguration.getEndpoint())
                .credentials(serviceConfiguration.getCredentials().getUser(), serviceConfiguration.getCredentials().getPassword())
                .buildApi(NovaApi.class);
    }

    @Override
    public boolean isAvailable(String region) {
        final Optional<? extends FloatingIPApi> floatingIPExtensionForZone = this.novaApi.getFloatingIPExtensionForZone(region);
        return floatingIPExtensionForZone.isPresent();
    }

    protected FloatingIPApi getFloatingIPExtensionForZone(String region) {
        final Optional<? extends FloatingIPApi> floatingIPExtensionForZone = this.novaApi.getFloatingIPExtensionForZone(region);
        checkArgument(floatingIPExtensionForZone.isPresent(), "Floating IP service is not available in region " + region);
        return floatingIPExtensionForZone.get();
    }

    @Override
    public Set<FloatingIP> list(String region) {
        return getFloatingIPExtensionForZone(region).list().toSet();
    }

    @Override
    @Nullable
    public FloatingIP allocateFromPool(String pool, String region) {
        return getFloatingIPExtensionForZone(region).allocateFromPool(pool);
    }

    @Override
    @Nullable
    public FloatingIP create(String region) {
        return getFloatingIPExtensionForZone(region).create();
    }

    @Override
    public void addToServer(String region, String address, String serverId) {
        getFloatingIPExtensionForZone(region).addToServer(address, serverId);
    }

    @Override
    public void removeFromServer(String region, String address, String serverId) {
        getFloatingIPExtensionForZone(region).removeFromServer(address, serverId);
    }

}



