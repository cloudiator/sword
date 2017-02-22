package de.uniulm.omi.cloudiator.sword.remote;

import java.io.Closeable;
import java.io.File;

/**
 * Represents a connection to a remote node, e.g. via SSH.
 */
public interface RemoteConnection extends Closeable {

    /**
     * @param command the command to be executed by the RemoteConnection
     * @return the {@link RemoteConnectionResponse} of the command.
     * @throws RemoteException if an error occurs during communication.
     */
    RemoteConnectionResponse executeCommand(String command) throws RemoteException;

    File downloadFile(String path) throws RemoteException;

    /**
     * Writes content to the specified file, creates the file if it doesn't exist
     *
     * @param pathAndFilename to the file
     * @param content         of the file to write
     * @param setExecutable   if the file is executable
     * @return the exit value when the file is written
     * @throws RemoteException if an error occurs during communication.
     */
    int writeFile(String pathAndFilename, String content, boolean setExecutable)
        throws RemoteException;

    @Override
    void close();
}
