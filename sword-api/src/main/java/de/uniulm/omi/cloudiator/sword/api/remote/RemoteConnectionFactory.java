package de.uniulm.omi.cloudiator.sword.api.remote;

import de.uniulm.omi.cloudiator.sword.api.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.api.domain.OSFamily;

/**
 * Created by Daniel Seybold on 06.05.2015.
 */
public interface RemoteConnectionFactory {

    /**
     * Creates a RemoteConnection to a remote host
     * @param remoteAddress
     * @param osFamily
     * @param loginCredential
     * @param port the specific port for opening a RemoteConnection
     * @return
     */
    RemoteConnection createRemoteConnection(String remoteAddress, OSFamily osFamily,
        LoginCredential loginCredential, int port);
}
