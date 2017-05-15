/*
 * Copyright (c) 2014-2017 University of Ulm
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
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.MoreObjects;

/**
 * Created by daniel on 01.12.14.
 */
public class CloudCredentialImpl implements CloudCredential {

  private final String user;
  private final String password;

  CloudCredentialImpl(String user, String password) {

    checkNotNull(user, "user is null");
    checkNotNull(password, "password is null");
    checkArgument(!user.isEmpty(), "user is empty");
    checkArgument(!password.isEmpty(), "password is empty");

    this.user = user;
    this.password = password;
  }

  @Override
  public String user() {
    return this.user;
  }

  @Override
  public String password() {
    return this.password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CloudCredentialImpl that = (CloudCredentialImpl) o;

    if (!user.equals(that.user)) {
      return false;
    }
    return password.equals(that.password);
  }

  @Override
  public int hashCode() {
    int result = user.hashCode();
    result = 31 * result + password.hashCode();
    return result;
  }

  @Override
  public String id() {
    return user;
  }

  @Override
  public String toString() {
    //todo: do not disclose password
    return MoreObjects.toStringHelper(this).add("user", user).add("password", password)
        .toString();
  }
}
