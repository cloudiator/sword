/*
 * Copyright (c) 2014 University of Ulm
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
import de.uniulm.omi.executionware.core.domain.impl.CredentialsImpl;

import javax.annotation.Nullable;

/**
 * Created by daniel on 02.12.14.
 */
public class ServiceConfigurationBuilder {

    private String endpoint;
    private String username;
    private String password;
    private String provider;
    @Nullable
    private String nodeGroup;

    public ServiceConfigurationBuilder endpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public ServiceConfigurationBuilder username(String username) {
        this.username = username;
        return this;
    }

    public ServiceConfigurationBuilder password(String password) {
        this.password = password;
        return this;
    }

    public ServiceConfigurationBuilder provider(String provider) {
        this.provider = provider;
        return this;
    }

    public ServiceConfigurationBuilder nodeGroup(String nodeGroup) {
        this.nodeGroup = nodeGroup;
        return this;
    }

    public ServiceConfiguration build() {
        return new ServiceConfigurationImpl(this.provider, this.endpoint, new CredentialsImpl(username, password), this.nodeGroup);
    }

}
