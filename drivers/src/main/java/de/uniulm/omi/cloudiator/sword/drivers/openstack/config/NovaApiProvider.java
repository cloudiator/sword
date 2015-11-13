/*
 * Copyright (c) 2014-2015 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.drivers.openstack.config;

import com.google.inject.Inject;
import com.google.inject.Provider;
import de.uniulm.omi.cloudiator.sword.api.ServiceConfiguration;
import org.jclouds.ContextBuilder;
import org.jclouds.openstack.nova.v2_0.NovaApi;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by daniel on 19.05.15.
 */
public class NovaApiProvider implements Provider<NovaApi> {

    private final ServiceConfiguration serviceConfiguration;

    @Inject public NovaApiProvider(ServiceConfiguration serviceConfiguration) {
        this.serviceConfiguration = serviceConfiguration;
    }

    @Override public NovaApi get() {

        checkArgument(serviceConfiguration.getEndpoint().isPresent(),
            "Nova requires a configured endpoint.");

        //todo duplicates code from JCloudsComputeClientImpl
        final ContextBuilder contextBuilder =
            ContextBuilder.newBuilder(serviceConfiguration.getProvider())
                .credentials(serviceConfiguration.getCredentials().user(),
                    serviceConfiguration.getCredentials().password());

        if(serviceConfiguration.getEndpoint().isPresent()) {
            contextBuilder.endpoint(serviceConfiguration.getEndpoint().get());
        }
        return contextBuilder.buildApi(NovaApi.class);
    }
}
