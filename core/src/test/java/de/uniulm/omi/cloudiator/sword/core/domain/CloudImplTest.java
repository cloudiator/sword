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
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

/**
 * Created by daniel on 18.01.17.
 */
public class CloudImplTest {

    private final Api testApi = ApiBuilder.newBuilder().providerName("providerName").build();
    private final Credentials testCredentials =
        CredentialsBuilder.newBuilder().password("password").user("user").build();
    private final String testEndpoint = "endpoint";

    @Test public void testApi() {
        final Cloud cloud = CloudBuilder.newBuilder().api(testApi).credentials(testCredentials)
            .endpoint(testEndpoint).build();
        assertThat(cloud.api(), is(equalTo(testApi)));
    }

    @Test public void testCredential() {
        final Cloud cloud = CloudBuilder.newBuilder().api(testApi).credentials(testCredentials)
            .endpoint(testEndpoint).build();
        assertThat(cloud.credentials(), is(equalTo(testCredentials)));
    }

    @Test public void testEndpoint() {
        final Cloud cloud = CloudBuilder.newBuilder().api(testApi).credentials(testCredentials)
            .endpoint(testEndpoint).build();
        assertThat(cloud.endpoint().get(), is(equalTo(testEndpoint)));
    }

    @Test public void testEndpointNullable() {
        final Cloud cloud =
            CloudBuilder.newBuilder().api(testApi).credentials(testCredentials).endpoint(null)
                .build();
        assertThat(cloud.endpoint(), is(equalTo(Optional.empty())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyEndpointThrowsIllegalArgumentException() {
        final Cloud cloud =
            CloudBuilder.newBuilder().api(testApi).credentials(testCredentials).endpoint("")
                .build();
    }

    @Test public void testId() {
        Cloud cloud =
            CloudBuilder.newBuilder().api(testApi).credentials(testCredentials).endpoint(null)
                .build();
        Cloud sameCloud =
            CloudBuilder.newBuilder().api(testApi).credentials(testCredentials).endpoint(null)
                .build();
        Cloud differentEndpoint = CloudBuilder.newBuilder().api(testApi).credentials(testCredentials)
            .endpoint(testEndpoint).build();
        Cloud differentApi =
            CloudBuilder.newBuilder().api(ApiBuilder.newBuilder().providerName("different").build())
                .credentials(testCredentials).endpoint(testEndpoint).build();

        assertThat(cloud.id(), not(equalTo(differentApi.id())));
        assertThat(cloud.id(), not(equalTo(differentEndpoint.id())));
        assertThat(cloud.id(), is(equalTo(sameCloud.id())));
    }


}
