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

package de.uniulm.omi.executionware.service;

import de.uniulm.omi.executionware.api.ServiceConfiguration;
import de.uniulm.omi.executionware.api.domain.Credentials;
import de.uniulm.omi.executionware.api.domain.LoginCredential;
import de.uniulm.omi.executionware.api.properties.ServiceProperties;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 02.12.14.
 */
public class ServiceConfigurationImpl implements ServiceConfiguration {

    private final String provider;
    private final String endpoint;
    private final Credentials credentials;
    private final String nodeGroup;
    private final LoginCredential loginCredential;
    private final ServiceProperties serviceProperties;

    public ServiceConfigurationImpl(String provider, String endpoint, Credentials credentials, String nodeGroup, LoginCredential loginCredential, ServiceProperties serviceProperties) {
        checkNotNull(provider);
        checkArgument(!provider.isEmpty());
        checkNotNull(endpoint);
        checkArgument(!endpoint.isEmpty());
        checkNotNull(credentials);
        checkNotNull(nodeGroup);
        checkArgument(!nodeGroup.isEmpty());
        checkNotNull(loginCredential);
        checkNotNull(serviceProperties);

        this.provider = provider;
        this.endpoint = endpoint;
        this.credentials = credentials;
        this.nodeGroup = nodeGroup;
        this.loginCredential = loginCredential;
        this.serviceProperties = serviceProperties;
    }

    @Override
    public String getEndpoint() {
        return endpoint;
    }

    @Override
    public String getProvider() {
        return provider;
    }

    @Override
    public Credentials getCredentials() {
        return credentials;
    }

    @Override
    public String getNodeGroup() {
        return nodeGroup;
    }

    @Override
    public LoginCredential getLoginCredential() {
        return loginCredential;
    }

    @Override
    public ServiceProperties getServiceProperties() {
        return this.serviceProperties;
    }
}
