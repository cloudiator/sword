package de.uniulm.omi.cloudiator.sword.remote.overthere;

import com.xebialabs.overthere.ConnectionOptions;
import de.uniulm.omi.cloudiator.domain.RemoteType;
import de.uniulm.omi.cloudiator.sword.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.remote.RemoteConnection;
import de.uniulm.omi.cloudiator.sword.remote.RemoteConnectionFactory;
import de.uniulm.omi.cloudiator.sword.remote.RemoteException;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by daniel on 19.08.15.
 */
public abstract class AbstractOverthereConnectionFactory implements RemoteConnectionFactory {

    @Override
    public RemoteConnection createRemoteConnection(String remoteAddress, RemoteType remoteType,
        LoginCredential loginCredential, int port) throws RemoteException {

        checkArgument(loginCredential.username().isPresent(),
            "LoginCredential does not contain user name.");
        checkArgument(
            loginCredential.password().isPresent() ^ loginCredential.privateKey().isPresent(),
            "LoginCredential must either have private key or password.");

        ConnectionOptions connectionOptions =
            buildConnectionOptions(remoteAddress, loginCredential.username().get(), port);

        if (loginCredential.password().isPresent()) {
            this.setPassword(connectionOptions, loginCredential.password().get());
        } else if (loginCredential.privateKey().isPresent()) {
            this.setKey(connectionOptions, loginCredential.privateKey().get());
        } else {
            throw new AssertionError("Illegal state of login credential.");
        }

        return openConnection(connectionOptions);
    }

    private ConnectionOptions buildConnectionOptions(String remoteAddress, String username,
        int port) {
        ConnectionOptions connectionOptions = new ConnectionOptions();
        connectionOptions.set(ConnectionOptions.ADDRESS, remoteAddress);
        connectionOptions.set(ConnectionOptions.USERNAME, username);
        connectionOptions.set(ConnectionOptions.PORT, port);

        buildConnectionOptions(connectionOptions);

        return connectionOptions;
    }

    protected abstract ConnectionOptions buildConnectionOptions(
        ConnectionOptions connectionOptions);

    protected abstract ConnectionOptions setPassword(ConnectionOptions connectionOptions,
        String password);

    protected abstract ConnectionOptions setKey(ConnectionOptions connectionOptions, String key);

    protected abstract RemoteConnection openConnection(ConnectionOptions connectionOptions)
        throws RemoteException;

}
