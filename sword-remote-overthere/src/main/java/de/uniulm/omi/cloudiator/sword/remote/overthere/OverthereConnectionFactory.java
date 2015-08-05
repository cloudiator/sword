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

package de.uniulm.omi.cloudiator.sword.remote.overthere;


import com.xebialabs.overthere.ConnectionOptions;
import com.xebialabs.overthere.OperatingSystemFamily;
import com.xebialabs.overthere.Overthere;
import com.xebialabs.overthere.RuntimeIOException;
import com.xebialabs.overthere.cifs.CifsConnectionBuilder;
import com.xebialabs.overthere.cifs.CifsConnectionType;
import com.xebialabs.overthere.ssh.SshConnectionBuilder;
import com.xebialabs.overthere.ssh.SshConnectionType;
import de.uniulm.omi.cloudiator.sword.api.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.api.domain.OSFamily;
import de.uniulm.omi.cloudiator.sword.api.logging.InjectLogger;
import de.uniulm.omi.cloudiator.sword.api.logging.Logger;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnection;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnectionFactory;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Daniel Seybold on 06.05.2015.
 */
public class OverthereConnectionFactory implements RemoteConnectionFactory {

    @InjectLogger Logger logger;

    private final ConnectionOptions connectionOptions = new ConnectionOptions();

    /**
     * An internal counter for the number of retry approaches for Overthere connections
     */
    private static final int CONNECTION_RETRY_COUNTER = 10;

    /**
     * the factor of increasing the timeout value for an Overthere connection, if a connection approach isn't successful
     */
    private static final int INCREASE_TIMEOUT_FACTOR = 2;

    /**
     * The basic connection timeout in seconds.
     */
    private static final int BASIC_CONNECTION_TIMEOUT = 10;

    /**
     * An extendet timeout for WinRm connections (default timeout is PT60.000S)
     */
    private static final String EXTENDED_WINRM_TIMEOUT = "PT120.000S";


    @Override
    public RemoteConnection createRemoteConnection(String remoteAddress, OSFamily osFamily,
        LoginCredential loginCredential, int port) {
        checkNotNull(remoteAddress);
        checkArgument(!remoteAddress.isEmpty());

        checkNotNull(loginCredential);
        checkArgument(!loginCredential.username().isEmpty());

        checkNotNull(port);
        checkArgument(!(port == 0));

        //setting general attributes for the RemoteConnection
        this.setGeneralConnectionOptions(remoteAddress, loginCredential.username(), port);

        //opens a OS specific RemoteConnection
        return this.createRemoteConnectionWithRetry(osFamily, loginCredential);

    }

    /**
     * tries to open a Overthere connection and in case of a timeout retrying it for {@value #CONNECTION_RETRY_COUNTER}
     *
     * @param osFamily
     * @param loginCredential
     * @return
     */
    private RemoteConnection createRemoteConnectionWithRetry(OSFamily osFamily,
        LoginCredential loginCredential) {


        Exception lastException = null;
        for (int i = 1; i <= OverthereConnectionFactory.CONNECTION_RETRY_COUNTER; i++) {

            try {

                return this.createRemoteConnectionSpecific(osFamily, loginCredential);

            } catch (RuntimeIOException e) {

                lastException = e;
                if (i < OverthereConnectionFactory.CONNECTION_RETRY_COUNTER) {
                    logger.debug("Remote Connection could not be established, retry attempt: " + i);
                }

                try {
                    Thread.sleep(BASIC_CONNECTION_TIMEOUT * 1000 * INCREASE_TIMEOUT_FACTOR * i);
                } catch (InterruptedException interrupt) {
                    throw new IllegalStateException(interrupt);
                }

            }
        }

        throw new IllegalStateException(
            "Unable to connect to host " + this.connectionOptions.get(ConnectionOptions.ADDRESS) +
                " on port " + this.connectionOptions.get(ConnectionOptions.PORT) + " after "
                + OverthereConnectionFactory.CONNECTION_RETRY_COUNTER + " approaches!",
            lastException);
    }

    /**
     * Creates the operating system specific Overthere connection
     *
     * @param osFamily
     * @param loginCredential
     * @return
     */
    private RemoteConnection createRemoteConnectionSpecific(OSFamily osFamily,
        LoginCredential loginCredential) {

        switch (osFamily) {
            case UNIX:
                return new OverthereConnection(this.openLinuxConnection(loginCredential));
            case WINDOWS:
                OverthereConnection windowsConnection =
                    new OverthereConnection(this.openWindowsConnection(loginCredential));
                this.checkWindowsConnection(windowsConnection);
                //return new OverthereConnection(this.openWindowsConnection(loginCredential));
                return windowsConnection;
            default:
                throw new UnsupportedOperationException(
                    "Remote connection to given OSFamily (" + osFamily + ") not supported.");
        }
    }

    /**
     * Opens a RemoteConnection to a Windows server
     *
     * @param loginCredential
     * @return the Overthere Windows connection
     */
    private com.xebialabs.overthere.OverthereConnection openWindowsConnection(
        LoginCredential loginCredential) {

        checkNotNull(loginCredential.password());
        checkArgument(loginCredential.password().isPresent());
        checkArgument(!loginCredential.password().get().isEmpty());

        this.setWindowsConnectionOptions(loginCredential.password().get());

        return Overthere.getConnection("cifs", this.connectionOptions);

    }

    /**
     * Sets the general attributes for the RemoteConnection
     *
     * @param remoteAddress
     * @param username
     * @param port
     */
    private void setGeneralConnectionOptions(String remoteAddress, String username, int port) {

        this.connectionOptions.set(ConnectionOptions.ADDRESS, remoteAddress);
        this.connectionOptions.set(ConnectionOptions.USERNAME, username);
        this.connectionOptions.set(ConnectionOptions.PORT, port);
    }

    /**
     * Sets Windows specific attributes
     *
     * @param password
     */
    private void setWindowsConnectionOptions(String password) {

        this.connectionOptions.set(ConnectionOptions.PASSWORD, password);
        this.connectionOptions
            .set(ConnectionOptions.OPERATING_SYSTEM, OperatingSystemFamily.WINDOWS);
        //CifsConnectionType WINRM_NATIVE is only supported on Windows hosts, use instead WINRM_INTERNAL
        this.connectionOptions
            .set(CifsConnectionBuilder.CONNECTION_TYPE, CifsConnectionType.WINRM_INTERNAL);
        //set a higher timeout for WindowsConnections
        this.connectionOptions.set(CifsConnectionBuilder.WINRM_TIMEMOUT,
            OverthereConnectionFactory.EXTENDED_WINRM_TIMEOUT);

    }

    /**
     * Opens a RemoteConnection to a Linux server
     *
     * @param loginCredential
     * @return the Overthere Linux connection
     */
    private com.xebialabs.overthere.OverthereConnection openLinuxConnection(
        LoginCredential loginCredential) {

        this.setLinuxConnectionOptions(loginCredential);

        return Overthere.getConnection("ssh", this.connectionOptions);
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

            this.connectionOptions
                .set(SshConnectionBuilder.PRIVATE_KEY, loginCredential.privateKey().get());

        } else {
            checkArgument(!loginCredential.password().get().isEmpty());

            this.connectionOptions
                .set(ConnectionOptions.PASSWORD, loginCredential.password().get());
        }

    }

    /**
     * Test if the WindowsConnection is established with a dummy command
     * Necessary because for Windows Overthere throws TimeoutException with first command execution
     *
     * @param windowsConnection
     */
    private void checkWindowsConnection(OverthereConnection windowsConnection) {
        checkNotNull(windowsConnection);

        //execute dummy command to check if Connection is enabled
        windowsConnection.executeCommand("echo windows connection established");
    }
}
