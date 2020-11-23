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

import com.xebialabs.overthere.ConnectionOptions;
import com.xebialabs.overthere.OperatingSystemFamily;
import com.xebialabs.overthere.ssh.SshConnectionBuilder;
import com.xebialabs.overthere.ssh.SshConnectionType;
import de.uniulm.omi.cloudiator.sword.remote.RemoteConnection;
import de.uniulm.omi.cloudiator.sword.remote.RemoteException;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by daniel on 19.08.15.
 */
@Slf4j
public class OverthereSSHConnectionFactory extends AbstractOverthereConnectionFactory {

  @Override
  protected ConnectionOptions buildConnectionOptions(ConnectionOptions connectionOptions) {
    connectionOptions.set(ConnectionOptions.OPERATING_SYSTEM, OperatingSystemFamily.UNIX);
    connectionOptions.set(SshConnectionBuilder.CONNECTION_TYPE, SshConnectionType.SFTP);
    connectionOptions.set(SshConnectionBuilder.ALLOCATE_DEFAULT_PTY, true);
    log.warn("clientUser -connectionOptions - : "+ connectionOptions.toString());
    return connectionOptions;
  }

  @Override
  protected ConnectionOptions setPassword(ConnectionOptions connectionOptions, String password) {
    connectionOptions.set(ConnectionOptions.PASSWORD, password);
    return connectionOptions;
  }

  @Override
  protected ConnectionOptions setKey(ConnectionOptions connectionOptions, String key) {
    connectionOptions.set(SshConnectionBuilder.PRIVATE_KEY, key);
    return connectionOptions;
  }

  @Override
  protected RemoteConnection openConnection(ConnectionOptions connectionOptions)
      throws RemoteException {
    //todo find a better way
    return new OverthereSSHConnection(SwordOverthere.getConnection("ssh", connectionOptions));
  }
}
