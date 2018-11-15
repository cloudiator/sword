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

package de.uniulm.omi.cloudiator.sword.remote;

import de.uniulm.omi.cloudiator.domain.RemoteType;
import de.uniulm.omi.cloudiator.sword.domain.LoginCredential;

/**
 * Created by Daniel Seybold on 06.05.2015.
 */
public interface RemoteConnectionFactory {

  /**
   * Creates a RemoteConnection to a remote host.
   *
   * @param remoteAddress the address of the remote machine
   * @param remoteType the type of remote connection
   * @param loginCredential the login credentials for login.
   * @param port the specific port for opening a RemoteConnection.
   * @return a connection to the remote machine.
   * @throws IllegalArgumentException if the login credential misses username, and one of password
   * or private key.
   * @throws RemoteException if it was not possible to establish the connection.
   */
  RemoteConnection createRemoteConnection(String remoteAddress, RemoteType remoteType,
      LoginCredential loginCredential, int port) throws RemoteException;
}
