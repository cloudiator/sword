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

/**
 * Created by daniel on 18.01.17.
 */
public class CloudBuilder {

    private Api api;
    private String endpoint;
    private Credentials credentials;

    private CloudBuilder() {

    }

    public static CloudBuilder newBuilder() {
        return new CloudBuilder();
    }

    public CloudBuilder api(Api api) {
        this.api = api;
        return this;
    }

    public CloudBuilder endpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public CloudBuilder credentials(Credentials credentials) {
        this.credentials = credentials;
        return this;
    }

    public Cloud build() {
        return new CloudImpl(api, endpoint, credentials);
    }

}
