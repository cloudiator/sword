/*
 * Copyright (c) 2014-2018 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.domain;


import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.MoreObjects;
import java.util.Optional;
import javax.annotation.Nullable;

/**
 * Basic implementation of the {@link LoginCredential} interface.
 * <p/>
 * Use {@link LoginCredentialBuilder} to create new objects.
 */
public class LoginCredentialImpl implements LoginCredential {

  @Nullable
  private final String username;
  @Nullable
  private final String password;
  @Nullable
  private final String privateKey;

  LoginCredentialImpl(@Nullable String username, @Nullable String password,
      @Nullable String privateKey) {

    if (username != null) {
      checkArgument(!username.isEmpty(), "Username must not be empty.");
    }

    if (password != null) {
      checkArgument(!password.isEmpty(), "Password must not be empty.");
    }

    if (privateKey != null) {
      checkArgument(!privateKey.isEmpty(), "Private key must not be empty.");
    }

    this.username = username;
    this.password = password;
    this.privateKey = privateKey;
  }

  @Override
  public Optional<String> username() {
    return Optional.ofNullable(username);
  }

  @Override
  public Optional<String> password() {
    return Optional.ofNullable(password);
  }

  @Override
  public Optional<String> privateKey() {
    return Optional.ofNullable(privateKey);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("username", username())
        .add("password", password()).add("privateKey", privateKey).toString();
  }
}
