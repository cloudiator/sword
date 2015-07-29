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


import com.xebialabs.overthere.CmdLine;
import com.xebialabs.overthere.ConnectionOptions;
import com.xebialabs.overthere.OperatingSystemFamily;
import com.xebialabs.overthere.OverthereFile;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnection;

import java.io.PrintWriter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by Daniel Seybold on 04.05.2015.
 */
public class OverthereConnection implements RemoteConnection {

    private final com.xebialabs.overthere.OverthereConnection overthereConnection;


    public OverthereConnection(com.xebialabs.overthere.OverthereConnection overthereConnection) {

        checkNotNull(overthereConnection);

        this.overthereConnection = overthereConnection;
    }

    public int executeCommand(String command) {

        checkNotNull(command);
        checkArgument(!command.isEmpty());

        int returnCode;

        switch ((OperatingSystemFamily) this.overthereConnection.getOptions()
            .get(ConnectionOptions.OPERATING_SYSTEM)) {
            case WINDOWS:
                returnCode = this.executeWindwosCommand(command);
                this.checkReturnCode(returnCode, command, OperatingSystemFamily.WINDOWS);
                return returnCode;

            case UNIX:
                returnCode = this.executeLinuxCommand(command);
                this.checkReturnCode(returnCode, command, OperatingSystemFamily.UNIX);
                return returnCode;
            default:
                throw new UnsupportedOperationException(
                    "Execution of RemoteConnection to given OSFamily ("
                        + ConnectionOptions.OPERATING_SYSTEM + ") not supported.");
        }



    }

    private int executeLinuxCommand(String command) {

        //wrong escaping of the characters
        //int exitCode = this.overthereConnection.execute(CmdLine.build(splittedCommands));

        //TODO: check why CmdLine.build(command) escapes characters in the wrong way
        return this.overthereConnection.execute(CmdLine.build().addRaw(command));
    }

    private int executeWindwosCommand(String command) {

        //TODO: check why the Overthere encoding doesn't work for Windows and Linux!
        //split the command into separate commands otherwise Windows commands can't be recognized
        String[] splittedCommands = command.split("\\s+");

        //int exitCode = this.overthereConnection.execute(CmdLine.build().addRaw(command));

        return this.overthereConnection.execute(CmdLine.build(splittedCommands));

    }

    public int writeFile(String pathAndFilename, String content, boolean setExecutable) {

        checkNotNull(pathAndFilename);
        checkNotNull(content);
        checkNotNull(setExecutable);

        checkArgument(!pathAndFilename.isEmpty());
        checkArgument(!content.isEmpty());

        //TODO: check again if windows wrtieFile works or build a switch case instead

        switch ((OperatingSystemFamily) this.overthereConnection.getOptions()
            .get(ConnectionOptions.OPERATING_SYSTEM)) {
            case WINDOWS:
                return this.writeFileToWindows(pathAndFilename, content);

            case UNIX:
                return this.writeFileToUnix(pathAndFilename, content, setExecutable);
            default:
                throw new UnsupportedOperationException(
                    "Execution of RemoteConnection.writeFile to given OSFamily ("
                        + ConnectionOptions.OPERATING_SYSTEM + ") not supported.");
        }


    }


    /**
     * Using the Overthere method to write a file to the Linux filesystem
     *
     * @param pathAndFilename
     * @param content
     * @param setExecutable
     * @return
     */
    private int writeFileToUnix(String pathAndFilename, String content, boolean setExecutable) {
        OverthereFile overthereFile = this.overthereConnection.getFile(pathAndFilename);
        if (setExecutable) {
            overthereFile.setExecutable(setExecutable);
        }

        try (PrintWriter writer = new PrintWriter(overthereFile.getOutputStream())) {
            writer.print(content);
        }

        return 0;
    }

    /**
     * Writes a file to the Windows filesystem, using simple echo >> command to avoid escaping/line break issues
     * Original Overthere writeFile method didn't work for windows
     *
     * @param pathAndFilename the absolute path to the file and filename
     * @param content         of the file to write
     * @return
     */
    private int writeFileToWindows(String pathAndFilename, String content) {

        //spilt at newline
        String[] splittedContent = content.split("\\r?\\n");

        //use powershell Add-Content to avoid line break issues with windows in config file
        for (String line : splittedContent) {
            this.executeCommand(
                "powershell -command  Add-Content " + pathAndFilename + " '" + line + "'");
        }

        return 0;

    }


    private void checkReturnCode(int returnCode, String command,
        OperatingSystemFamily operatingSystemFamily) {

        checkNotNull(returnCode);
        checkNotNull(command);
        checkNotNull(operatingSystemFamily);

        checkArgument(!command.isEmpty());


        if (returnCode != 0) {
            throw new RuntimeException(
                "RemoteConnection command failed on " + operatingSystemFamily.toString() + " host: "
                    + command);
        }
    }



    public void close() {

        this.overthereConnection.close();

    }


}
