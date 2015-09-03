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
import de.uniulm.omi.cloudiator.sword.core.domain.CredentialsBuilder;

/**
 * Created by daniel on 02.12.14.
 */
class ServiceConfigurationBuilder {

    private String endpoint;
    private String username;
    private String password;
    private String provider;
    private String nodeGroup;

    ServiceConfigurationBuilder endpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    ServiceConfigurationBuilder username(String username) {
        this.username = username;
        return this;
    }

    ServiceConfigurationBuilder password(String password) {
        this.password = password;
        return this;
    }

    ServiceConfigurationBuilder provider(String provider) {
        this.provider = provider;
        return this;
    }

    ServiceConfigurationBuilder nodeGroup(String nodeGroup) {
        this.nodeGroup = nodeGroup;
        return this;
    }

    ServiceConfiguration build() {
        return new ServiceConfigurationImpl(this.provider, this.endpoint,
            CredentialsBuilder.newBuilder().user(username).password(password).build(),
            this.nodeGroup);
    }

}
