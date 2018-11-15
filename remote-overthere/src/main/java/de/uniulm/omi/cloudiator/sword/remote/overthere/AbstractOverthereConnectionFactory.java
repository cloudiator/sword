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

package de.uniulm.omi.cloudiator.sword.remote.overthere;

import static com.google.common.base.Preconditions.checkArgument;

import com.xebialabs.overthere.ConnectionOptions;
import de.uniulm.omi.cloudiator.domain.RemoteType;
import de.uniulm.omi.cloudiator.sword.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.remote.RemoteConnection;
import de.uniulm.omi.cloudiator.sword.remote.RemoteConnectionFactory;
import de.uniulm.omi.cloudiator.sword.remote.RemoteException;

/**
 * Created by daniel on 19.08.15.
 */
public abstract class AbstractOverthereConnectionFactory implements RemoteConnectionFactory {

  @Override
  public RemoteConnection createRemoteConnection(String remoteAddress, RemoteType remoteType,
      LoginCredential loginCredential, int port) throws RemoteException {

    checkArgument(loginCredential.username().isPresent(),
        "LoginCredential does not contain user name.");
    checkArgument(
        loginCredential.password().isPresent() ^ loginCredential.privateKey().isPresent(),
        "LoginCredential must either have private key or password.");

    ConnectionOptions connectionOptions =
        buildConnectionOptions(remoteAddress, loginCredential.username().get(), port);

    if (loginCredential.password().isPresent()) {
      this.setPassword(connectionOptions, loginCredential.password().get());
    } else if (loginCredential.privateKey().isPresent()) {
      this.setKey(connectionOptions, loginCredential.privateKey().get());
    } else {
      throw new AssertionError("Illegal state of login credential.");
    }

    return openConnection(connectionOptions);
  }

  private ConnectionOptions buildConnectionOptions(String remoteAddress, String username,
      int port) {
    ConnectionOptions connectionOptions = new ConnectionOptions();
    connectionOptions.set(ConnectionOptions.ADDRESS, remoteAddress);
    connectionOptions.set(ConnectionOptions.USERNAME, username);
    connectionOptions.set(ConnectionOptions.PORT, port);

    buildConnectionOptions(connectionOptions);

    return connectionOptions;
  }

  protected abstract ConnectionOptions buildConnectionOptions(
      ConnectionOptions connectionOptions);

  protected abstract ConnectionOptions setPassword(ConnectionOptions connectionOptions,
      String password);

  protected abstract ConnectionOptions setKey(ConnectionOptions connectionOptions, String key);

  protected abstract RemoteConnection openConnection(ConnectionOptions connectionOptions)
      throws RemoteException;

}
