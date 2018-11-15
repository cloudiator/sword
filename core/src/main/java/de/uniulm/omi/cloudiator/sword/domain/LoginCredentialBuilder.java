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

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;


/**
 * Builder for a LoginCredential object. Create new builder by calling newBuilder() method.
 */
public class LoginCredentialBuilder {

  @Nullable
  private String username;
  @Nullable
  private String privateKey;
  @Nullable
  private String password;

  private LoginCredentialBuilder() {

  }

  private LoginCredentialBuilder(LoginCredential loginCredential) {
    username = loginCredential.username().orElse(null);
    privateKey = loginCredential.privateKey().orElse(null);
    password = loginCredential.password().orElse(null);
  }

  /**
   * Creates a new Builder.
   *
   * @return a new builder.
   */
  public static LoginCredentialBuilder newBuilder() {
    return new LoginCredentialBuilder();
  }

  /**
   * Returns a builder that is initialized with the values of the given credential.
   *
   * @param loginCredential the login credential used for initialization.
   * @return a builder
   */
  public static LoginCredentialBuilder of(LoginCredential loginCredential) {
    checkNotNull(loginCredential, "loginCredential is null");
    return new LoginCredentialBuilder(loginCredential);
  }

  /**
   * Builds the object.
   *
   * @return the created login credential.
   */
  public LoginCredential build() {
    return new LoginCredentialImpl(username, password, privateKey);
  }

  /**
   * The username used for login.
   *
   * @param username the username for the user.
   * @return fluid interface
   */
  public LoginCredentialBuilder username(@Nullable String username) {
    this.username = username;
    return this;
  }

  /**
   * The private key.
   *
   * @param privateKey the private key used for login.
   * @return fluid interface
   */
  public LoginCredentialBuilder privateKey(@Nullable String privateKey) {
    this.privateKey = privateKey;
    return this;
  }

  /**
   * The password for the login.
   *
   * @param password the password for the login.
   * @return fluid interface
   */
  public LoginCredentialBuilder password(@Nullable String password) {
    this.password = password;
    return this;
  }
}
