/*
 * Copyright (c) 2014-2018 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.drivers.onestep.internal;

import client.Configuration;
import client.api.ApiClient;
import com.google.inject.Inject;
import com.google.inject.Provider;
import de.uniulm.omi.cloudiator.sword.domain.Cloud;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mriedl on 07.2020
 */
public class OnestepProvider implements Provider<ApiClient> {

    private final static String DELIMITER = ":";

    private final Cloud cloud;

    @Inject
    public OnestepProvider(Cloud cloud) {
        checkNotNull(cloud, "cloud is null");
        this.cloud = cloud;
    }

    @Override
    public ApiClient get() {
        String usernameClient = cloud.credential().user();
        String passwordSecret = cloud.credential().password();

        ApiClient defaultClient = Configuration.getDefaultApiClient();

//        defaultClient.setDebugging(true);
        defaultClient.setWriteTimeout(4 * 60000);
        defaultClient.setReadTimeout(4 * 60000);
        defaultClient.setConnectTimeout(4 * 60000);

        TokenSetter tokenSetter = new TokenSetter(defaultClient, usernameClient, passwordSecret);
        tokenSetter.setToken();

        return defaultClient;

    }

}
