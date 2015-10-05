package de.uniulm.omi.cloudiator.sword.remote.overthere;

import de.uniulm.omi.cloudiator.sword.api.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.api.domain.OSFamily;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnection;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnectionFactory;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteException;

/**
 * Created by daniel on 19.08.15.
 */
public class OverthereDecidingConnectionFactory implements RemoteConnectionFactory {

    private final OverthereSSHConnectionFactory overthereSSHConnectionFactory;
    private final OverthereWinRMConnectionFactory overthereWinRMConnectionFactory;

    public OverthereDecidingConnectionFactory() {
        overthereSSHConnectionFactory = new OverthereSSHConnectionFactory();
        overthereWinRMConnectionFactory = new OverthereWinRMConnectionFactory();
    }

    @Override
    public RemoteConnection createRemoteConnection(String remoteAddress, OSFamily osFamily,
        LoginCredential loginCredential, int port) throws RemoteException {

        switch (osFamily) {
            case UNIX:
                return overthereSSHConnectionFactory
                    .createRemoteConnection(remoteAddress, osFamily, loginCredential, port);
            case WINDOWS:
                return overthereWinRMConnectionFactory
                    .createRemoteConnection(remoteAddress, osFamily, loginCredential, port);
            default:
                throw new AssertionError("Unsupported osFamily detected.");
        }
    }
}
