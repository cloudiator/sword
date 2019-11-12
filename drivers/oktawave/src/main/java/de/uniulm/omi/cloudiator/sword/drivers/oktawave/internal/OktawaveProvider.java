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

package de.uniulm.omi.cloudiator.sword.drivers.oktawave.internal;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.oktawave.api.client.handler.ApiClient;
import com.oktawave.api.client.handler.Configuration;
import com.oktawave.api.client.handler.auth.OAuth;
import de.uniulm.omi.cloudiator.sword.domain.Cloud;
import jdk.nashorn.internal.parser.Token;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by pszkup on 08.05.2019
 */
public class OktawaveProvider implements Provider<ApiClient> {

    private static Logger LOGGER = LoggerFactory.getLogger(OktawaveProvider.class);
    private final static String DELIMITER = ":";

    private final Cloud cloud;

    @Inject
    public OktawaveProvider(Cloud cloud) {
        checkNotNull(cloud, "cloud is null");
        this.cloud = cloud;
    }

    @Override
    public ApiClient get() {
        String usernameClient = cloud.credential().user();
        String passwordSecret = cloud.credential().password();

        Pair<String, String> usernameClientPair = parse(usernameClient);
        Pair<String, String> passwordSecretPair = parse(passwordSecret);

        ApiClient defaultClient = Configuration.getDefaultApiClient();

//        defaultClient.setDebugging(true);
        defaultClient.setWriteTimeout(4 * 60000);
        defaultClient.setReadTimeout(4 * 60000);
        defaultClient.setConnectTimeout(4 * 60000);

        TokenUpdater tokenUpdater = new TokenUpdater(defaultClient, usernameClientPair.getRight(), passwordSecretPair.getRight(), usernameClientPair.getLeft(), passwordSecretPair.getLeft());
        new Thread(tokenUpdater).start();

        return defaultClient;

    }

    private Pair<String, String> parse(String string) {
        int colonIndex = string.lastIndexOf(DELIMITER);
        if (colonIndex == -1) {
            throw new IllegalStateException("Expected user '" + string + "' to be of format xxx:yyy");
        }
        String left = string.substring(0, colonIndex);
        String right = string.substring(colonIndex + 1);
        return Pair.of(left, right);
    }

}
