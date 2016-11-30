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
import de.uniulm.omi.cloudiator.sword.api.domain.Location;

/**
 * Builder for a {@link KeyPair}.
 * <p/>
 * Use {@link #newBuilder()} to create a new builder instance.
 */
public class KeyPairBuilder {

    private String id;
    private String providerId;
    private Location location;
    private String name;
    private String publicKey;
    private String privateKey;

    private KeyPairBuilder() {
    }

    /**
     * @return a new builder instance.
     */
    public static KeyPairBuilder newBuilder() {
        return new KeyPairBuilder();
    }

    /**
     * @param id of the keypair
     * @return fluid interface
     */
    public KeyPairBuilder id(String id) {
        this.id = id;
        return this;
    }

    /**
     * @param providerId of the keypair
     * @return fluid interface
     */
    public KeyPairBuilder providerId(String providerId) {
        this.providerId = providerId;
        return this;
    }

    /**
     * @param name of the keypair
     * @return fluid interface
     */
    public KeyPairBuilder name(String name) {
        this.name = name;
        return this;
    }

    public KeyPairBuilder location(Location location) {
        this.location = location;
        return this;
    }

    /**
     * @param publicKey the public key.
     * @return fluid interface.
     */
    public KeyPairBuilder publicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    /**
     * @param privateKey the private key.
     * @return fluid interface
     */
    public KeyPairBuilder privateKey(String privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    /**
     * Builds the keypair.
     *
     * @return the created keypair.
     */
    public KeyPair build() {
        return new KeyPairImpl(id, providerId, name, location, publicKey, privateKey);
    }


}
