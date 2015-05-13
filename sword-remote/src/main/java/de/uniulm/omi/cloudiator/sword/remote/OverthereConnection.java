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

package de.uniulm.omi.cloudiator.sword.remote;


import com.xebialabs.overthere.*;
import com.xebialabs.overthere.cifs.CifsConnectionBuilder;
import com.xebialabs.overthere.cifs.CifsConnectionType;
import com.xebialabs.overthere.ssh.SshConnectionBuilder;
import com.xebialabs.overthere.ssh.SshConnectionType;
import de.uniulm.omi.cloudiator.sword.api.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.api.properties.Constants;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnection;

import java.io.PrintWriter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Daniel Seybold on 04.05.2015.
 */
public class OverthereConnection implements RemoteConnection {

    private final com.xebialabs.overthere.OverthereConnection overthereConnection;


    public OverthereConnection(com.xebialabs.overthere.OverthereConnection overthereConnection){

        checkNotNull(overthereConnection);

        this.overthereConnection = overthereConnection;
    }

    public int executeCommand(String command) {

        checkNotNull(command);
        checkArgument(!command.isEmpty());

        //split the command into separate commands otherwise Windows commands can't be recognized
        String [] splittedCommands = command.split("\\s+");
        System.out.println(CmdLine.build(splittedCommands));

        //String hack = "java -jar visor.jar -conf default.properties &> /dev/null &";
        int exitCode = this.overthereConnection.execute(CmdLine.build(splittedCommands));
        //int exitCode = this.overthereConnection.execute(CmdLine.build().addRaw(hack));
        return exitCode;

    }

    public int writeFile(String pathAndFilename, String content, boolean setExecutable){

        checkNotNull(pathAndFilename);
        checkNotNull(content);
        checkNotNull(setExecutable);

        checkArgument(!pathAndFilename.isEmpty());
        checkArgument(!content.isEmpty());


        OverthereFile overthereFile = this.overthereConnection.getFile(pathAndFilename);
        if(setExecutable){
            overthereFile.setExecutable(setExecutable);
        }

        PrintWriter writer = new PrintWriter(overthereFile.getOutputStream());
        try{
            writer.print(content);
        }finally {
            writer.close();
        }

        return 0;
    }

    public void close() {

        this.overthereConnection.close();

    }


}
