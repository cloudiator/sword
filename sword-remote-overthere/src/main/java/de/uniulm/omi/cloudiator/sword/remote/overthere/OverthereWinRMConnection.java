package de.uniulm.omi.cloudiator.sword.remote.overthere;

import com.xebialabs.overthere.CmdLine;
import com.xebialabs.overthere.OverthereConnection;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnection;

/**
 * Created by daniel on 19.08.15.
 */
public class OverthereWinRMConnection implements RemoteConnection {

    private final OverthereConnection delegate;

    OverthereWinRMConnection(OverthereConnection delegate) {
        this.delegate = delegate;
    }

    @Override public int executeCommand(String command) {
        //TODO: check why the Overthere encoding doesn't work for Windows and Linux!
        //split the command into separate commands otherwise Windows commands can't be recognized
        String[] splittedCommands = command.split("\\s+");
        return this.delegate.execute(CmdLine.build(splittedCommands));
    }

    @Override public int writeFile(String pathAndFilename, String content, boolean setExecutable) {

        //todo: ignores setExecutable?

        //spilt at newline
        String[] splittedContent = content.split("\\r?\\n");

        //use powershell Add-Content to avoid line break issues with windows in config file
        for (String line : splittedContent) {
            this.executeCommand(
                "powershell -command  Add-Content " + pathAndFilename + " '" + line + "'");
        }

        //todo check this return value
        return 0;
    }

    @Override public void close() {
        delegate.close();
    }
}
