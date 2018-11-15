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
import com.xebialabs.overthere.cifs.CifsConnectionBuilder;
import com.xebialabs.overthere.cifs.CifsConnectionType;
import de.uniulm.omi.cloudiator.sword.remote.RemoteConnection;
import de.uniulm.omi.cloudiator.sword.remote.RemoteException;

/**
 * Created by daniel on 19.08.15.
 */
public class OverthereWinRMConnectionFactory extends AbstractOverthereConnectionFactory {

  /**
   * An extended timeout for WinRm connections (default timeout is PT60.000S)
   *
   * @todo make configurable
   */
  private static final String EXTENDED_WINRM_TIMEOUT = "PT120.000S";

  @Override
  protected ConnectionOptions buildConnectionOptions(ConnectionOptions connectionOptions) {

    connectionOptions.set(ConnectionOptions.OPERATING_SYSTEM, OperatingSystemFamily.WINDOWS);
    //CifsConnectionType WINRM_NATIVE is only supported on Windows hosts, use instead WINRM_INTERNAL
    connectionOptions
        .set(CifsConnectionBuilder.CONNECTION_TYPE, CifsConnectionType.WINRM_INTERNAL);
    //set a higher timeout for WindowsConnections
    connectionOptions.set(CifsConnectionBuilder.WINRM_TIMEMOUT, EXTENDED_WINRM_TIMEOUT);
    return connectionOptions;
  }

  @Override
  protected ConnectionOptions setPassword(ConnectionOptions connectionOptions, String password) {
    connectionOptions.set(ConnectionOptions.PASSWORD, password);
    return connectionOptions;
  }

  @Override
  protected ConnectionOptions setKey(ConnectionOptions connectionOptions, String key) {
    throw new UnsupportedOperationException("Key login is not supported by WinRM");
  }

  @Override
  protected RemoteConnection openConnection(ConnectionOptions connectionOptions)
      throws RemoteException {
    //todo find a better way
    final OverthereWinRMConnection winRMConnection =
        new OverthereWinRMConnection(SwordOverthere.getConnection("cifs", connectionOptions));
    // test the win rm connection
    winRMConnection.executeCommand("echo windows connection established");
    return winRMConnection;
  }
}
