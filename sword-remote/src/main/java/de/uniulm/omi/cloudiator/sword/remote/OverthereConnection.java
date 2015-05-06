package de.uniulm.omi.cloudiator.sword.remote;


import com.xebialabs.overthere.*;
import com.xebialabs.overthere.cifs.CifsConnectionBuilder;
import com.xebialabs.overthere.cifs.CifsConnectionType;
import com.xebialabs.overthere.ssh.SshConnectionBuilder;
import com.xebialabs.overthere.ssh.SshConnectionType;
import de.uniulm.omi.cloudiator.sword.api.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.api.properties.Constants;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnection;

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

        //split the command into separate commands otherwise Windows commands can't be recognized
        String [] splittedCommands = command.split("\\s+");

        int exitCode = this.overthereConnection.execute(CmdLine.build(splittedCommands));

        return exitCode;

    }

    public void close() {

        this.overthereConnection.close();

    }


}
