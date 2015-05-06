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

package de.uniulm.omi.cloudiator.sword.remote;


import com.xebialabs.overthere.ConnectionOptions;
import com.xebialabs.overthere.OperatingSystemFamily;
import com.xebialabs.overthere.Overthere;
import com.xebialabs.overthere.cifs.CifsConnectionBuilder;
import com.xebialabs.overthere.cifs.CifsConnectionType;
import com.xebialabs.overthere.ssh.SshConnectionBuilder;
import com.xebialabs.overthere.ssh.SshConnectionType;
import de.uniulm.omi.cloudiator.sword.api.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.api.domain.OSFamily;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnection;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnectionFactory;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Daniel Seybold on 06.05.2015.
 */
public class OverthereConnectionFactory implements RemoteConnectionFactory {

    private final ConnectionOptions connectionOptions = new ConnectionOptions();

    @Override
    public RemoteConnection createRemoteConnection(String remoteAddress, OSFamily osFamily, LoginCredential loginCredential, int port) {
        checkNotNull(remoteAddress);
        checkArgument(!remoteAddress.isEmpty());

        checkNotNull(loginCredential);
        checkArgument(!loginCredential.username().isEmpty());

        //setting general attributes for the RemoteConnection
        this.setGeneralConnectionOptions(remoteAddress, loginCredential.username());

        //opens a OS specific RemoteConnection
        //TODO: handle possible timeouts when opening the connection, use delegator pattern, repeat 3 times

        switch (osFamily) {
            case UNIX:
                return new OverthereConnection(this.openLinuxConnection(loginCredential));
            case WINDOWS:
                return new OverthereConnection(this.openWindowsConnection(loginCredential));
            default:
                throw new UnsupportedOperationException("Remote connection to given OSFamily (" + osFamily + ") not supported.");
        }
    }

    /**
     * Opens a RemoteConnection to a Windows server
     *
     * @param loginCredential
     * @return the Overthere Windows connection
     */
    private com.xebialabs.overthere.OverthereConnection openWindowsConnection(LoginCredential loginCredential) {

        checkArgument(loginCredential.password().isPresent());
        checkArgument(!loginCredential.password().get().isEmpty());

        this.setWindowsConnectionOptions(loginCredential.password().get());

        com.xebialabs.overthere.OverthereConnection overthereConnection = Overthere.getConnection("cifs", this.connectionOptions);

        return overthereConnection;

    }

    /**
     * Sets the general attributes for the RemoteConnection
     *
     * @param remoteAddress
     * @param username
     */
    private void setGeneralConnectionOptions(String remoteAddress, String username) {
        //TODO: handle port paramter
        this.connectionOptions.set(ConnectionOptions.ADDRESS, remoteAddress);
        this.connectionOptions.set(ConnectionOptions.USERNAME, username);
    }

    /**
     * Sets Windwos specific attributes
     *
     * @param password
     */
    private void setWindowsConnectionOptions(String password) {

        this.connectionOptions.set(ConnectionOptions.PASSWORD, password);
        this.connectionOptions.set(ConnectionOptions.OPERATING_SYSTEM, OperatingSystemFamily.WINDOWS);
        //CifsConnectionType WINRM_NATIVE is only supported on Windows hosts, use instead WINRM_INTERNAL
        this.connectionOptions.set(CifsConnectionBuilder.CONNECTION_TYPE, CifsConnectionType.WINRM_INTERNAL);

    }

    /**
     * Opens a RemoteConnection to a Linux server
     *
     * @param loginCredential
     * @return the Overthere Linux connection
     */
    private com.xebialabs.overthere.OverthereConnection openLinuxConnection(LoginCredential loginCredential) {

        this.setLinuxConnectionOptions(loginCredential);

        com.xebialabs.overthere.OverthereConnection overthereConnection = Overthere.getConnection("ssh", this.connectionOptions);

        return overthereConnection;
    }

    /**
     * Sets Linux specific attributs
     * Determines if the RemoteConnection is Key or Password authentication
     *
     * @param loginCredential
     */
    private void setLinuxConnectionOptions(LoginCredential loginCredential) {
        this.connectionOptions.set(ConnectionOptions.OPERATING_SYSTEM, OperatingSystemFamily.UNIX);
        this.connectionOptions.set(SshConnectionBuilder.CONNECTION_TYPE, SshConnectionType.SFTP);

        //determine password or key authentication
        if (loginCredential.isPrivateKeyCredential()) {
            checkArgument(!loginCredential.privateKey().get().isEmpty());

            this.connectionOptions.set(SshConnectionBuilder.PRIVATE_KEY, loginCredential.privateKey().get());

        } else {
            checkArgument(!loginCredential.password().get().isEmpty());

            this.connectionOptions.set(ConnectionOptions.PASSWORD, loginCredential.password().get());
        }

    }
}
