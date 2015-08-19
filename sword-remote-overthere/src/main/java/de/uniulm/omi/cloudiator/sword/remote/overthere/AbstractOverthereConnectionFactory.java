package de.uniulm.omi.cloudiator.sword.remote.overthere;

import com.xebialabs.overthere.ConnectionOptions;
import de.uniulm.omi.cloudiator.sword.api.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.api.domain.OSFamily;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnection;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnectionFactory;

/**
 * Created by daniel on 19.08.15.
 */
public abstract class AbstractOverthereConnectionFactory implements RemoteConnectionFactory {

    @Override
    public RemoteConnection createRemoteConnection(String remoteAddress, OSFamily osFamily,
        LoginCredential loginCredential, int port) {
        ConnectionOptions connectionOptions =
            buildConnectionOptions(remoteAddress, loginCredential.username(), port);

        if (loginCredential.isPasswordCredential()) {
            this.setPassword(connectionOptions, loginCredential.password().get());
        } else if (loginCredential.isPrivateKeyCredential()) {
            this.setKey(connectionOptions, loginCredential.privateKey().get());
        } else {
            throw new IllegalStateException(
                "Login credentials do not provide password or private key.");
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

    protected abstract RemoteConnection openConnection(ConnectionOptions connectionOptions);

}
