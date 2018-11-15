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

import com.google.common.base.MoreObjects;
import javax.annotation.Nullable;

/**
 * Created by daniel on 18.08.15.
 */
public class CredentialsBuilder {

  @Nullable
  private String user;
  @Nullable
  private String password;

  private CredentialsBuilder() {

  }

  private CredentialsBuilder(CloudCredential cloudCredential) {
    this.user = cloudCredential.user();
    this.password = cloudCredential.password();
  }

  public static CredentialsBuilder newBuilder() {
    return new CredentialsBuilder();
  }

  public static CredentialsBuilder of(CloudCredential cloudCredential) {
    return new CredentialsBuilder(cloudCredential);
  }


  public CredentialsBuilder user(String user) {
    this.user = user;
    return this;
  }

  public CredentialsBuilder password(String password) {
    this.password = password;
    return this;
  }

  public CloudCredential build() {
    return new CloudCredentialImpl(user, password);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).toString();
  }


}
