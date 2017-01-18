/*
 * Copyright (c) 2014-2017 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.core.domain;

import de.uniulm.omi.cloudiator.sword.api.domain.Api;
import de.uniulm.omi.cloudiator.sword.api.domain.Cloud;
import de.uniulm.omi.cloudiator.sword.api.domain.Credentials;

import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 18.01.17.
 */
public class CloudImpl implements Cloud {

    private final Api api;
    @Nullable private final String endpoint;
    private final Credentials credentials;

    public CloudImpl(Api api, @Nullable String endpoint, Credentials credentials) {

        checkNotNull(api, "api is null.");
        if (endpoint != null) {
            checkArgument(!endpoint.isEmpty());
        }
        checkNotNull(credentials, "credentials is null");

        this.api = api;
        this.endpoint = endpoint;
        this.credentials = credentials;
    }

    @Override public Api api() {
        return api;
    }

    @Override public Optional<String> endpoint() {
        return Optional.ofNullable(endpoint);
    }

    @Override public Credentials credentials() {
        return credentials;
    }
}
