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

package de.uniulm.omi.cloudiator.sword.core.domain.builders;

import de.uniulm.omi.cloudiator.sword.api.domain.LoginCredential;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by daniel on 29.07.15.
 */
public class LoginCredentialBuilderTest {

    @Test public void testBuild() throws Exception {
        final LoginCredential passwordCredential =
            LoginCredentialBuilder.newBuilder().password("password").username("username").build();
        assertThat(passwordCredential.password().get(), equalTo("password"));
        assertThat(passwordCredential.username(), equalTo("username"));

        final LoginCredential keyCredential =
            LoginCredentialBuilder.newBuilder().privateKey("privateKey").username("username")
                .build();
        assertThat(keyCredential.privateKey().get(), equalTo("privateKey"));
        assertThat(passwordCredential.username(), equalTo("username"));
    }
}
