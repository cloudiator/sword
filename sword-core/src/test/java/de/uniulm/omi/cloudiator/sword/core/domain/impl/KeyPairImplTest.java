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

package de.uniulm.omi.cloudiator.sword.core.domain.impl;

import de.uniulm.omi.cloudiator.sword.api.domain.KeyPair;
import de.uniulm.omi.cloudiator.sword.core.domain.builders.KeyPairBuilder;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by daniel on 30.07.15.
 */
public class KeyPairImplTest {

    @Test(expected = NullPointerException.class) public void testConstructorNotAllowsNullName() {
        KeyPairBuilder.newBuilder().name(null).privateKey("privateKey").publicKey("publicKey")
            .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorDisallowsEmptyName() {
        KeyPairBuilder.newBuilder().name("").privateKey("privateKey").publicKey("publicKey")
            .build();
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorDisallowsNullPublicKey() {
        KeyPairBuilder.newBuilder().name("name").privateKey("privateKey").publicKey(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorDisallowsEmptyPublicKey() {
        KeyPairBuilder.newBuilder().name("name").privateKey("privateKey").publicKey("").build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorDisallowsEmptyPrivateKey() {
        KeyPairBuilder.newBuilder().name("name").privateKey("").publicKey("publicKey").build();
    }

    public void testContructorAllowsNullPrivateKey() {
        KeyPairBuilder.newBuilder().name("name").privateKey(null).publicKey("publicKey").build();
    }

    @Test public void testName() throws Exception {
        final KeyPair keyPair =
            KeyPairBuilder.newBuilder().name("name").privateKey("privateKey").publicKey("publicKey")
                .build();
        assertThat(keyPair.name(), equalTo("name"));
    }

    @Test public void testPublicKey() throws Exception {
        final KeyPair keyPair =
            KeyPairBuilder.newBuilder().name("name").privateKey("privateKey").publicKey("publicKey")
                .build();
        assertThat(keyPair.publicKey(), equalTo("publicKey"));
    }

    @Test public void testPrivateKey() throws Exception {
        final KeyPair keyPairNull =
            KeyPairBuilder.newBuilder().name("name").privateKey(null).publicKey("publicKey")
                .build();
        assertThat(keyPairNull.privateKey().isPresent(), equalTo(false));

        final KeyPair keyPairNotNull =
            KeyPairBuilder.newBuilder().name("name").privateKey("privateKey").publicKey("publicKey")
                .build();
        assertThat(keyPairNotNull.privateKey().get(), equalTo("privateKey"));

    }
}
