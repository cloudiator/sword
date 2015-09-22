package de.uniulm.omi.cloudiator.sword.remote.overthere;

import com.xebialabs.overthere.ConnectionOptions;
import com.xebialabs.overthere.OperatingSystemFamily;
import com.xebialabs.overthere.Overthere;
import com.xebialabs.overthere.ssh.SshConnectionBuilder;
import com.xebialabs.overthere.ssh.SshConnectionType;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnection;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteException;

/**
 * Created by daniel on 19.08.15.
 */
public class OverthereSSHConnectionFactory extends AbstractOverthereConnectionFactory {

    @Override
    protected ConnectionOptions buildConnectionOptions(ConnectionOptions connectionOptions) {
        connectionOptions.set(ConnectionOptions.OPERATING_SYSTEM, OperatingSystemFamily.UNIX);
        connectionOptions.set(SshConnectionBuilder.CONNECTION_TYPE, SshConnectionType.SFTP);
        return connectionOptions;
    }

    @Override
    protected ConnectionOptions setPassword(ConnectionOptions connectionOptions, String password) {
        connectionOptions.set(ConnectionOptions.PASSWORD, password);
        return connectionOptions;
    }

    @Override protected ConnectionOptions setKey(ConnectionOptions connectionOptions, String key) {
        connectionOptions.set(SshConnectionBuilder.PRIVATE_KEY, key);
        return connectionOptions;
    }

    @Override protected RemoteConnection openConnection(ConnectionOptions connectionOptions)
        throws RemoteException {
        //todo find a better way
        try {
            return new OverthereSSHConnection(Overthere.getConnection("ssh", connectionOptions));
        } catch (Exception e) {
            throw new RemoteException(e);
        }

    }
}
