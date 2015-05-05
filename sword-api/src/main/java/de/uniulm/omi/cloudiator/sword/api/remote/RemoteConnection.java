package de.uniulm.omi.cloudiator.sword.api.remote;

import de.uniulm.omi.cloudiator.sword.api.domain.LoginCredential;

/**
 * Created by Daniel Seybold on 04.05.2015.
 */
public interface RemoteConnection {

    /**
     * Opens a RemoteConnection to a remote host
     * @param remoteAddress
     * @param osType
     * @param loginCredential
     * @param port the
     * @return
     */
    public boolean open(String remoteAddress, String osType, LoginCredential loginCredential, int port);

    /**
     *
     * @param command the command to be executed by the RemoteConnection
     * @return the exit value of the executed command
     */
    public int executeCommand(String command);

    /**
     * Closes the existing RemoteConnection
     */
    public void close();


}
