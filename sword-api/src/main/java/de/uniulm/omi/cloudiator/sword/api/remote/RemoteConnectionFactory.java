package de.uniulm.omi.cloudiator.sword.api.remote;

import de.uniulm.omi.cloudiator.sword.api.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.api.domain.OSFamily;

/**
 * Created by Daniel Seybold on 06.05.2015.
 */
public interface RemoteConnectionFactory {

    /**
     * Creates a RemoteConnection to a remote host.
     *
     * @param remoteAddress   the address of the remote machine
     * @param osFamily        the operating system family of the remote
     * @param loginCredential the login credentials for login.
     * @param port            the specific port for opening a RemoteConnection.
     * @return a connection to the remote machine.
     */
    RemoteConnection createRemoteConnection(String remoteAddress, OSFamily osFamily,
        LoginCredential loginCredential, int port);
}
