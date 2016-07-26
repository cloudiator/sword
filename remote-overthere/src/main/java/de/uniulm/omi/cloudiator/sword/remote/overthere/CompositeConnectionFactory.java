/*
 * Copyright (c) 2014-2016 University of Ulm
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

import de.uniulm.omi.cloudiator.common.os.RemoteType;
import de.uniulm.omi.cloudiator.sword.api.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnection;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnectionFactory;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteException;

import java.util.Arrays;
import java.util.Set;

/**
 * Created by daniel on 26.07.16.
 */
public class CompositeConnectionFactory implements RemoteConnectionFactory {

    private final Set<RemoteConnectionFactory> remoteConnectionFactories;

    public CompositeConnectionFactory(Set<RemoteConnectionFactory> remoteConnectionFactories) {
        this.remoteConnectionFactories = remoteConnectionFactories;
    }

    @Override
    public RemoteConnection createRemoteConnection(String remoteAddress, RemoteType remoteType,
        LoginCredential loginCredential, int port) throws RemoteException {

        RemoteException last = null;
        for (RemoteConnectionFactory remoteConnectionFactory : remoteConnectionFactories) {
            try {
                return remoteConnectionFactory
                    .createRemoteConnection(remoteAddress, remoteType, loginCredential, port);
            } catch (RemoteException e) {
                last = e;
            }
        }
        throw new RemoteException(String.format(
            "Tried all available connection factories %s but could not establish a connection.",
            Arrays.toString(remoteConnectionFactories.toArray())), last);
    }
}
