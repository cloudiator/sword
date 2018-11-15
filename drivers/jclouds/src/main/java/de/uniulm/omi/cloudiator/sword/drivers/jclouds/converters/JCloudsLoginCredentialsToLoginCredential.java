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

package de.uniulm.omi.cloudiator.sword.drivers.jclouds.converters;


import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.uniulm.omi.cloudiator.sword.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.domain.LoginCredentialBuilder;
import de.uniulm.omi.cloudiator.sword.properties.Constants;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import org.jclouds.domain.LoginCredentials;

/**
 * Created by daniel on 27.01.15.
 */
public class JCloudsLoginCredentialsToLoginCredential
    implements OneWayConverter<LoginCredentials, LoginCredential> {

  @Inject(optional = true)
  @Named(Constants.IGNORE_LOGIN_KEYPAIR)
  private boolean ignoreKeyPair = false;
  @Inject(optional = true)
  @Named(Constants.IGNORE_LOGIN_PASSWORD)
  private boolean ignorePassword = false;
  @Inject(optional = true)
  @Named(Constants.IGNORE_LOGIN_USERNAME)
  private boolean ignoreUser = false;

  @Override
  public LoginCredential apply(LoginCredentials loginCredentials) {

    checkNotNull(loginCredentials);

    final LoginCredentialBuilder loginCredentialBuilder = LoginCredentialBuilder.newBuilder();
    if (!ignoreUser) {
      loginCredentialBuilder.username(loginCredentials.getUser());
    }
    if (loginCredentials.getOptionalPassword().isPresent() && !ignorePassword) {
      loginCredentialBuilder.password(loginCredentials.getOptionalPassword().get());
    }
    if (loginCredentials.getOptionalPrivateKey().isPresent() && !ignoreKeyPair) {
      loginCredentialBuilder.privateKey(loginCredentials.getOptionalPrivateKey().get());
    }

    return loginCredentialBuilder.build();
  }
}
