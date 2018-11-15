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

import com.google.common.collect.Sets;
import de.uniulm.omi.cloudiator.domain.RemoteType;
import de.uniulm.omi.cloudiator.sword.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.remote.RemoteConnection;
import de.uniulm.omi.cloudiator.sword.remote.RemoteConnectionFactory;
import de.uniulm.omi.cloudiator.sword.remote.RemoteException;
import java.util.LinkedHashSet;

/**
 * Created by daniel on 19.08.15.
 */
public class OverthereDecidingConnectionFactory implements RemoteConnectionFactory {

  private final RemoteConnectionFactory overthereSSHConnectionFactory;
  private final RemoteConnectionFactory overthereWinRMConnectionFactory;
  private final RemoteConnectionFactory compositeConnectionFactory;

  public OverthereDecidingConnectionFactory() {

    overthereSSHConnectionFactory = new OverthereSSHConnectionFactory();
    overthereWinRMConnectionFactory = new OverthereWinRMConnectionFactory();

    final LinkedHashSet<RemoteConnectionFactory> remoteConnectionFactories =
        Sets.newLinkedHashSetWithExpectedSize(2);
    remoteConnectionFactories.add(overthereSSHConnectionFactory);
    remoteConnectionFactories.add(overthereWinRMConnectionFactory);
    compositeConnectionFactory = new CompositeConnectionFactory(remoteConnectionFactories);
  }

  @Override
  public RemoteConnection createRemoteConnection(String remoteAddress, RemoteType remoteType,
      LoginCredential loginCredential, int port) throws RemoteException {

    switch (remoteType) {
      case SSH:
        return overthereSSHConnectionFactory
            .createRemoteConnection(remoteAddress, remoteType, loginCredential, port);
      case WINRM:
        return overthereWinRMConnectionFactory
            .createRemoteConnection(remoteAddress, remoteType, loginCredential, port);
      case UNKNOWN:
        return compositeConnectionFactory
            .createRemoteConnection(remoteAddress, remoteType, loginCredential, port);
      default:
        throw new AssertionError("Unsupported osFamily detected.");
    }
  }
}
