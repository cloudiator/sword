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

package de.uniulm.omi.cloudiator.sword.service;

import de.uniulm.omi.cloudiator.sword.api.ServiceConfiguration;
import de.uniulm.omi.cloudiator.sword.api.domain.Credentials;

import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 02.12.14.
 */
public class ServiceConfigurationImpl implements ServiceConfiguration {

    private final String provider;
    @Nullable private final String endpoint;
    private final Credentials credentials;
    private final String nodeGroup;

    ServiceConfigurationImpl(String provider, @Nullable String endpoint, Credentials credentials,
        String nodeGroup) {
        checkNotNull(provider);
        checkArgument(!provider.isEmpty());
        if (endpoint != null) {
            checkArgument(!endpoint.isEmpty());
        }
        checkNotNull(credentials);
        checkNotNull(nodeGroup);
        checkArgument(!nodeGroup.isEmpty());

        this.provider = provider;
        this.endpoint = endpoint;
        this.credentials = credentials;
        this.nodeGroup = nodeGroup;
    }

    @Override public Optional<String> getEndpoint() {
        return Optional.ofNullable(endpoint);
    }

    @Override public String getProvider() {
        return provider;
    }

    @Override public Credentials getCredentials() {
        return credentials;
    }

    @Override public String getNodeGroup() {
        return nodeGroup;
    }
}