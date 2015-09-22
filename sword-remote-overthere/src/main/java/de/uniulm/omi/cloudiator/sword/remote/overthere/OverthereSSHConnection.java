package de.uniulm.omi.cloudiator.sword.remote.overthere;

import com.xebialabs.overthere.CmdLine;
import com.xebialabs.overthere.OverthereConnection;
import com.xebialabs.overthere.OverthereFile;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnection;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnectionResponse;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteException;

import java.io.PrintWriter;

/**
 * Created by daniel on 19.08.15.
 */
//todo remove duplicated code
public class OverthereSSHConnection implements RemoteConnection {

    private final OverthereConnection delegate;

    public OverthereSSHConnection(OverthereConnection delegate) {
        this.delegate = delegate;
    }

    @Override public RemoteConnectionResponse executeCommand(String command)
        throws RemoteException {
        //TODO: check why CmdLine.build(command) escapes characters in the wrong way
        try {
            OverthereRemoteConnectionResponse response = new OverthereRemoteConnectionResponse();
            response.setExitStatus(delegate.execute(response.getStdOutExecutionOutputHandler(),
                response.getStdErrExecutionOutputHandler(), CmdLine.build().addRaw(command)));
            return response;
        } catch (Exception e) {
            throw new RemoteException(e);
        }
    }

    @Override public int writeFile(String pathAndFilename, String content, boolean setExecutable)
        throws RemoteException {
        OverthereFile overthereFile = this.delegate.getFile(pathAndFilename);
        if (setExecutable) {
            overthereFile.setExecutable(true);
        }

        // todo relies on default encoding, fix this
        try (PrintWriter writer = new PrintWriter(overthereFile.getOutputStream())) {
            writer.print(content);
        }

        //todo check this return value
        return 0;
    }

    @Override public void close() {
        delegate.close();
    }
}
