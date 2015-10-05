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

package de.uniulm.omi.cloudiator.sword.api.service;

import com.google.common.net.HostAndPort;
import de.uniulm.omi.cloudiator.sword.api.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.api.domain.OSFamily;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnection;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteException;

/**
 * A service for creating a {@link RemoteConnection} to an ip address.
 * The remote connection allows the execution of commands on the remote
 * machine.
 */
public interface ConnectionService {

    /**
     * Creates a new remote connection to the given address.
     *
     * @param hostAndPort     the host and the port of the remote socket (mandatory).
     * @param osFamily        the {@link OSFamily} of the remote operating system (mandatory).
     * @param loginCredential the credentials for login (mandatory).
     * @return a connection to the remote machine.
     * @throws NullPointerException if any of the mandatory arguments is null.
     * @throws RemoteException      if an error occurs while establishing the connection.
     */
    RemoteConnection getRemoteConnection(HostAndPort hostAndPort, OSFamily osFamily,
        LoginCredential loginCredential) throws RemoteException;

}
