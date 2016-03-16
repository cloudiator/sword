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

import de.uniulm.omi.cloudiator.sword.api.domain.KeyPair;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by daniel on 30.07.15.
 */
public class KeyPairImplTest {

    private final String testId = "id";
    private final String testName = "name";
    private final String testPrivateKey = "privateKey";
    private final String testPublicKey = "publicKey";
    private KeyPair validKeyPair;
    private KeyPairBuilder validKeyPairBuilder;

    @Before public void before() {
        this.validKeyPairBuilder =
            KeyPairBuilder.newBuilder().id(testId).name(testName).privateKey(testPrivateKey)
                .publicKey(testPublicKey);
        this.validKeyPair = validKeyPairBuilder.build();
    }

    @Test(expected = NullPointerException.class) public void testConstructorNotAllowsNullId() {
        validKeyPairBuilder.id(null).build();
    }

    @Test(expected = IllegalArgumentException.class) public void testConstructorDisallowsEmptyId() {
        validKeyPairBuilder.id("").build();
    }

    @Test(expected = NullPointerException.class) public void testConstructorNotAllowsNullName() {
        validKeyPairBuilder.name(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorDisallowsEmptyName() {
        validKeyPairBuilder.name("").build();
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorDisallowsNullPublicKey() {
        validKeyPairBuilder.publicKey(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorDisallowsEmptyPublicKey() {
        validKeyPairBuilder.publicKey("").build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorDisallowsEmptyPrivateKey() {
        validKeyPairBuilder.privateKey("").build();
    }

    @Test public void testConstructorAllowsNullPrivateKey() {
        validKeyPairBuilder.privateKey(null).build();
    }

    @Test public void testId() throws Exception {
        assertThat(validKeyPair.id(), equalTo(testId));
    }

    @Test public void testProviderId() throws Exception {
        assertThat(validKeyPair.providerId(), equalTo(testId));
    }

    @Test public void testName() throws Exception {
        assertThat(validKeyPair.name(), equalTo(testName));
    }

    @Test public void testPublicKey() throws Exception {
        assertThat(validKeyPair.publicKey(), equalTo(testPublicKey));
    }

    @Test public void testPrivateKey() throws Exception {
        assertThat(validKeyPair.privateKey().get(), equalTo(testPrivateKey));
    }
}
