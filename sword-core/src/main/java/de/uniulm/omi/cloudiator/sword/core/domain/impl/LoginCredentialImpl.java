/*
 * Copyright (c) 2014-2015 University of Ulm
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.uniulm.omi.cloudiator.sword.core.domain.impl;

import com.google.common.base.Optional;
import de.uniulm.omi.cloudiator.sword.api.domain.LoginCredential;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Basic implementation of the {@link LoginCredential} interface.
 * <p/>
 * Use {@link de.uniulm.omi.cloudiator.sword.core.domain.builders.LoginCredentialBuilder}
 * to create new objects.
 */
public class LoginCredentialImpl implements LoginCredential {

    private final String username;
    private final Optional<String> password;
    private final Optional<String> privateKey;

    /**
     * Constructor.
     * <p/>
     * Either password <b>XOR</b> private key must be supplied.
     *
     * @param username   the username (mandatory)
     * @param password   an {@link Optional} password
     * @param privateKey an {@link Optional} private key
     * @throws NullPointerException     if any of the arguments is null.
     * @throws IllegalArgumentException if any of the above Strings is empty.
     * @todo validate if private key is a real private key?
     */
    public LoginCredentialImpl(String username, Optional<String> password,
        Optional<String> privateKey) {

        checkNotNull(username, "Username is mandatory.");
        checkArgument(!username.isEmpty(), "Username must not be empty.");

        if (password.isPresent()) {
            checkArgument(!password.get().isEmpty(),
                "If a password is supplied, it must not be empty.");
        }
        if (privateKey.isPresent()) {
            checkArgument(!privateKey.get().isEmpty(),
                "If a private key is supplied, it must not be empty.");
        }

        checkNotNull(password, "Null is not allowed for password, use Optional.");
        checkNotNull(privateKey, "Null is not allowed for private key, use Optional");

        checkArgument(password.isPresent() ^ privateKey.isPresent(),
            "Password (exclusive)or private key must be present.");

        this.username = username;
        this.password = password;
        this.privateKey = privateKey;
    }

    @Override public String username() {
        return this.username;
    }

    @Override public Optional<String> password() {
        return this.password;
    }

    @Override public Optional<String> privateKey() {
        return this.privateKey;
    }

    @Override public boolean isPasswordCredential() {
        return this.password.isPresent();
    }

    @Override public boolean isPrivateKeyCredential() {
        return this.privateKey.isPresent();
    }
}
