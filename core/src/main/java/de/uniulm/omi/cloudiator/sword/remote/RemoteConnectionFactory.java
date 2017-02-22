package de.uniulm.omi.cloudiator.sword.remote;

import de.uniulm.omi.cloudiator.domain.LoginCredential;
import de.uniulm.omi.cloudiator.domain.RemoteType;

/**
 * Created by Daniel Seybold on 06.05.2015.
 */
public interface RemoteConnectionFactory {

    /**
     * Creates a RemoteConnection to a remote host.
     *
     * @param remoteAddress   the address of the remote machine
     * @param remoteType      the type of remote connection
     * @param loginCredential the login credentials for login.
     * @param port            the specific port for opening a RemoteConnection.
     * @return a connection to the remote machine.
     * @throws IllegalArgumentException if the login credential misses username, and one of password or private key.
     * @throws RemoteException          if it was not possible to establish the connection.
     */
    RemoteConnection createRemoteConnection(String remoteAddress, RemoteType remoteType,
        LoginCredential loginCredential, int port) throws RemoteException;
}
