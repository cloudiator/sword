package de.uniulm.omi.cloudiator.sword.remote;


import com.xebialabs.overthere.ConnectionOptions;
import com.xebialabs.overthere.OperatingSystemFamily;
import com.xebialabs.overthere.Overthere;
import com.xebialabs.overthere.OverthereConnection;
import com.xebialabs.overthere.cifs.CifsConnectionBuilder;
import com.xebialabs.overthere.cifs.CifsConnectionType;
import com.xebialabs.overthere.ssh.SshConnectionBuilder;
import com.xebialabs.overthere.ssh.SshConnectionType;
import de.uniulm.omi.cloudiator.sword.api.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.api.properties.Constants;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnection;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnectionFactory;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Daniel Seybold on 06.05.2015.
 */
public class RemoteConnectionFactoryImpl implements RemoteConnectionFactory {

    private final ConnectionOptions connectionOptions = new ConnectionOptions();

    @Override
    public RemoteConnection createRemoteConnection(String remoteAddress, String osType, LoginCredential loginCredential, int port) {
        checkNotNull(remoteAddress);
        checkArgument(!remoteAddress.isEmpty());

        checkNotNull(loginCredential);
        checkArgument(!loginCredential.username().isEmpty());

        //setting general attributes for the RemoteConnection
        this.setGeneralConnectionOptions(remoteAddress, loginCredential.username());

        //opens a OS specific RemoteConnection
        //TODO: handle possible timeouts when opening the connection
        if(osType.equals(Constants.OS_TYPE_LINUX)){


            return new RemoteConnectionImpl(this.openLinuxConnection(loginCredential));

        }else if(osType.equals(Constants.OS_TYPE_WINDWOS)){

            return new RemoteConnectionImpl(this.openWindowsConnection(loginCredential));
        }else{
            return null;
        }

    }

    /**
     * Opens a RemoteConnection to a Windows server
     * @param loginCredential
     * @return the Overthere Windows connection
     *
     */
    private OverthereConnection openWindowsConnection(LoginCredential loginCredential){

        checkArgument(loginCredential.password().isPresent());
        checkArgument(!loginCredential.password().get().isEmpty());

        this.setWindowsConnectionOptions(loginCredential.password().get());

        OverthereConnection overthereConnection  = Overthere.getConnection("cifs", this.connectionOptions);

        return overthereConnection;

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
     * @return the Overthere Linux connection
     */
    private OverthereConnection openLinuxConnection(LoginCredential loginCredential){

        this.setLinuxConnectionOptions(loginCredential);

        OverthereConnection overthereConnection = Overthere.getConnection("ssh", this.connectionOptions);

        return overthereConnection;
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

            this.connectionOptions.set(SshConnectionBuilder.PRIVATE_KEY, loginCredential.privateKey().get());

        }else{
            checkArgument(!loginCredential.password().get().isEmpty());

            this.connectionOptions.set(ConnectionOptions.PASSWORD, loginCredential.password().get());
        }

    }
}
