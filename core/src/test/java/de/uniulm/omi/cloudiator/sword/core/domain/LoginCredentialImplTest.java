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

package de.uniulm.omi.cloudiator.sword.core.domain;

import de.uniulm.omi.cloudiator.sword.api.domain.LoginCredential;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by daniel on 29.07.15.
 */
public class LoginCredentialImplTest {

    private String testUser = "user";
    private String testPassword = "password";
    private String testPrivateKey =
        "shglehsjkghejkshgjkehskgjehjkghesjkhgejkshgjkehsgjkhesjkghejkshgjkeshkgheks";
    private LoginCredential validLoginCredential;

    @Before public void before() {
        validLoginCredential =
            LoginCredentialBuilder.newBuilder().username(testUser).password(testPassword)
                .privateKey(testPrivateKey).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorForbidsEmptyPassword() {
        LoginCredentialBuilder.newBuilder().username(testUser).password("")
            .privateKey(testPrivateKey).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorForbidsEmptyUsername() {
        LoginCredentialBuilder.newBuilder().username("").password(testPassword)
            .privateKey(testPrivateKey).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorForbidsEmptyPrivateKey() {
        LoginCredentialBuilder.newBuilder().username(testUser).password(testPassword).privateKey("")
            .build();
    }

    @Test public void testUsername() throws Exception {
        assertThat(validLoginCredential.username().get(), equalTo(testUser));
    }

    @Test public void testPassword() throws Exception {
        assertThat(validLoginCredential.password().get(), equalTo(testPassword));
    }

    @Test public void testPrivateKey() throws Exception {
        assertThat(validLoginCredential.privateKey().get(), equalTo(testPrivateKey));
    }

    @Test public void testBuilderOf() {
        LoginCredentialBuilder builder = LoginCredentialBuilder.of(validLoginCredential);
        LoginCredential toTest = builder.build();
        assertThat(toTest.password().get(), equalTo(validLoginCredential.password().get()));
        assertThat(toTest.privateKey().get(), equalTo(validLoginCredential.privateKey().get()));
        assertThat(toTest.username().get(), equalTo(validLoginCredential.username().get()));
    }
}
