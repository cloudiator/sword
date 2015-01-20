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

package de.uniulm.omi.executionware.core.domain.impl;

import com.google.common.base.Optional;
import de.uniulm.omi.executionware.api.domain.LoginCredential;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 15.01.15.
 */
public class LoginCredentialImpl implements LoginCredential {

    private final String username;
    private final Optional<String> password;
    private final Optional<String> privateKey;

    public LoginCredentialImpl(String username, Optional<String> password, Optional<String> privateKey) {

        checkNotNull(username);
        checkArgument(!username.isEmpty(), "Username must not be empty.");
        checkNotNull(password);
        checkNotNull(privateKey);

        checkArgument(password.isPresent() || privateKey.isPresent(), "Password or private key must be present.");

        this.username = username;
        this.password = password;
        this.privateKey = privateKey;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public Optional<String> getPassword() {
        return this.password;
    }

    @Override
    public Optional<String> getPrivateKey() {
        return this.privateKey;
    }

    @Override
    public boolean isPasswordCredential() {
        return this.password.isPresent();
    }

    @Override
    public boolean isPrivateKeyCredential() {
        return this.privateKey.isPresent();
    }
}
