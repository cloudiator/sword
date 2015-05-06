package de.uniulm.omi.cloudiator.sword.core.remote;


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
public class RemoteConnectionImpl implements RemoteConnection {

    private OverthereConnection overthereConnection;
    private ConnectionOptions connectionOptions = new ConnectionOptions();



    public boolean open(String remoteAddress, String osType, LoginCredential loginCredential, int port) {

        checkNotNull(remoteAddress);
        checkArgument(!remoteAddress.isEmpty());

        checkNotNull(loginCredential);
        checkArgument(!loginCredential.username().isEmpty());

        //setting general attributes for the RemoteConnection
        this.setGeneralConnectionOptions(remoteAddress, loginCredential.username());

        //opens a OS specific RemoteConnection
        //TODO: handle possible timeouts when opening the connection
        if(osType.equals(Constants.OS_TYPE_LINUX)){

            this.openLinuxConnection(loginCredential);
            return true;

        }else if(osType.equals(Constants.OS_TYPE_WINDWOS)){
            this.openWindowsConnection(loginCredential);
            return true;
        }else{
            return false;
        }


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


    /**
     * Opens a RemoteConnection to a Windows server
     * @param loginCredential
     */
    private void openWindowsConnection(LoginCredential loginCredential){

        checkArgument(loginCredential.password().isPresent());
        checkArgument(!loginCredential.password().get().isEmpty());

        this.setWindowsConnectionOptions(loginCredential.password().get());

        this.overthereConnection  = Overthere.getConnection("cifs", this.connectionOptions);

    }

    /**
     * Sets the general attributes for the RemoteConnection
     * @param remoteAddress
     * @param username
     */
    private void setGeneralConnectionOptions(String remoteAddress, String username){

        this.connectionOptions.set(ConnectionOptions.ADDRESS, remoteAddress);
        this.connectionOptions.set(ConnectionOptions.USERNAME, username);
    }

    /**
     * Sets Windwos specific attributes
     * @param password
     */
    private void setWindowsConnectionOptions(String password){

        this.connectionOptions.set(ConnectionOptions.PASSWORD, password);
        this.connectionOptions.set(ConnectionOptions.OPERATING_SYSTEM, OperatingSystemFamily.WINDOWS);
        //CifsConnectionType WINRM_NATIVE is only supported on Windows hosts, use instead WINRM_INTERNAL
        this.connectionOptions.set(CifsConnectionBuilder.CONNECTION_TYPE, CifsConnectionType.WINRM_INTERNAL);

    }

    /**
     * Opens a RemoteConnection to a Linux server
     * @param loginCredential
     */
    private void openLinuxConnection(LoginCredential loginCredential){

        this.setLinuxConnectionOptions(loginCredential);

        this.overthereConnection = Overthere.getConnection("ssh", this.connectionOptions);

    }

    /**
     * Sets Linux specific attributs
     * Determines if the RemoteConnection is Key or Password authentication
     * @param loginCredential
     */
    private void setLinuxConnectionOptions(LoginCredential loginCredential){
        this.connectionOptions.set(ConnectionOptions.OPERATING_SYSTEM, OperatingSystemFamily.UNIX);
        this.connectionOptions.set(SshConnectionBuilder.CONNECTION_TYPE, SshConnectionType.SFTP);

        //determine password or key authentication
        if(loginCredential.isPrivateKeyCredential()){
            checkArgument(!loginCredential.privateKey().get().isEmpty());

            this.connectionOptions.set(SshConnectionBuilder.PRIVATE_KEY,loginCredential.privateKey().get());

        }else{
            checkArgument(!loginCredential.password().get().isEmpty());

            this.connectionOptions.set(ConnectionOptions.PASSWORD, loginCredential.password().get());
        }

    }



}
