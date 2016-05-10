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

import com.google.common.base.Optional;
import de.uniulm.omi.cloudiator.sword.api.domain.KeyPair;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Basic implementation of the {@link KeyPair} interface.
 */
public class KeyPairImpl implements KeyPair {

    private final String id;
    private final String name;
    private final String publicKey;
    @Nullable private final String privateKey;

    KeyPairImpl(String id, String name, String publicKey, @Nullable String privateKey) {

        checkNotNull(id, "ID must not be null.");
        checkArgument(!id.isEmpty(), "ID must not be empty.");

        checkNotNull(name, "Name must not be null.");
        checkArgument(!name.isEmpty(), "Name must not be empty.");

        checkNotNull(publicKey, "Public Key must not be null.");
        checkArgument(!publicKey.isEmpty(), "Public key must not be empty.");

        if (privateKey != null) {
            checkArgument(!privateKey.isEmpty(), "Private key must not be empty.");
        }

        this.id = id;
        this.name = name;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    @Override public String id() {
        return id;
    }

    @Override public String providerId() {
        return id();
    }

    @Override public String name() {
        return name;
    }

    @Override public String publicKey() {
        return publicKey;
    }

    @Override public Optional<String> privateKey() {
        return Optional.fromNullable(privateKey);
    }

}
