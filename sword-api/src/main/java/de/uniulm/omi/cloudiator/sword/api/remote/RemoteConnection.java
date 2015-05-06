package de.uniulm.omi.cloudiator.sword.api.remote;

/**
 * Created by Daniel Seybold on 04.05.2015.
 */
public interface RemoteConnection {

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
