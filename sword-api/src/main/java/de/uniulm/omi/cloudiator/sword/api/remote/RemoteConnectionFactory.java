package de.uniulm.omi.cloudiator.sword.api.remote;

import de.uniulm.omi.cloudiator.sword.api.domain.LoginCredential;

/**
 * Created by Daniel Seybold on 06.05.2015.
 */
public interface RemoteConnectionFactory {

    /**
     * Creates a RemoteConnection to a remote host
     * @param remoteAddress
     * @param osType
     * @param loginCredential
     * @param port the specific port for opening a RemoteConnection
     * @return
     */
    public RemoteConnection createRemoteConnection(String remoteAddress, String osType, LoginCredential loginCredential, int port);
}
