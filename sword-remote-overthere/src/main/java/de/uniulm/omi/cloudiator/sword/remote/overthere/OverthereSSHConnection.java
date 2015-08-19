package de.uniulm.omi.cloudiator.sword.remote.overthere;

import com.xebialabs.overthere.CmdLine;
import com.xebialabs.overthere.OverthereConnection;
import com.xebialabs.overthere.OverthereFile;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnection;

import java.io.PrintWriter;

/**
 * Created by daniel on 19.08.15.
 */
public class OverthereSSHConnection implements RemoteConnection {

    private final OverthereConnection delegate;

    public OverthereSSHConnection(OverthereConnection delegate) {
        this.delegate = delegate;
    }

    @Override public int executeCommand(String command) {
        //TODO: check why CmdLine.build(command) escapes characters in the wrong way
        return this.delegate.execute(CmdLine.build().addRaw(command));
    }

    @Override public int writeFile(String pathAndFilename, String content, boolean setExecutable) {
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
