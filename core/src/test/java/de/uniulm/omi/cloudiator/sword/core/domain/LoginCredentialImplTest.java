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

import de.uniulm.omi.cloudiator.sword.core.domain.LoginCredentialBuilder;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by daniel on 29.07.15.
 */
public class LoginCredentialImplTest {


    @Test(expected = IllegalArgumentException.class) public void testConstructorEmptyPassword() {
        LoginCredentialBuilder.newBuilder().username("user").password("").build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorMissesPasswordAndPrivateKey() {
        LoginCredentialBuilder.newBuilder().username("user").build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorHasPasswordAndPrivateKey() {
        LoginCredentialBuilder.newBuilder().username("user").password("password")
            .privateKey("privateKey").build();
    }

    @Test(expected = NullPointerException.class) public void testConstructorHasNullUsername() {
        LoginCredentialBuilder.newBuilder().username(null).password("password").build();
    }

    @Test(expected = IllegalArgumentException.class) public void testConstructorHasEmptyUsername() {
        LoginCredentialBuilder.newBuilder().username("").password("password").build();
    }

    @Test public void testUsername() throws Exception {
        assertThat(LoginCredentialBuilder.newBuilder().username("user").password("password").build()
            .username(), equalTo("user"));
    }

    @Test public void testPassword() throws Exception {
        assertThat(LoginCredentialBuilder.newBuilder().username("user").password("password").build()
            .password().get(), equalTo("password"));
    }

    @Test public void testPrivateKey() throws Exception {
        assertThat(
            LoginCredentialBuilder.newBuilder().username("user").privateKey("privateKey").build()
                .privateKey().get(), equalTo("privateKey"));
    }

    @Test public void testIsPasswordCredential() throws Exception {
        assertThat(LoginCredentialBuilder.newBuilder().username("user").password("password").build()
            .isPasswordCredential(), equalTo(true));
    }

    @Test public void testIsPrivateKeyCredential() throws Exception {
        assertThat(
            LoginCredentialBuilder.newBuilder().username("user").privateKey("privateKey").build()
                .isPrivateKeyCredential(), equalTo(true));
    }
}
