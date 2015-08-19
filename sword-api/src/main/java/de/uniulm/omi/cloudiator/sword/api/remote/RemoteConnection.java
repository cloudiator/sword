package de.uniulm.omi.cloudiator.sword.api.remote;

import java.io.Closeable;

/**
 * Created by Daniel Seybold on 04.05.2015.
 */
public interface RemoteConnection extends Closeable {

    /**
     *
     * @param command the command to be executed by the RemoteConnection
     * @return the exit value of the executed command
     */
    public int executeCommand(String command);

    /**
     * Writes content to the specified file, creates the file if it doesn't exist
     * @param pathAndFilename to the file
<<<<<<< Updated upstream
     * @param content of the file to write
     * @param setExecutable
     * @return the exit value when the file is written
     */
    public int writeFile(String pathAndFilename, String content, boolean setExecutable);

    /**
     * Closes the existing RemoteConnection
     */
    public void close();


=======
     * @param content         of the file to write
     * @param setExecutable   make the written file executable
     * @return the exit value when the file is written
     */
    int writeFile(String pathAndFilename, String content, boolean setExecutable);
>>>>>>> Stashed changes
}
