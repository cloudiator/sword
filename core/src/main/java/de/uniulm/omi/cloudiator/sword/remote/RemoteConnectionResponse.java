package de.uniulm.omi.cloudiator.sword.remote;

/**
 * Created by Daniel Seybold on 06.05.2015.
 */
public interface RemoteConnectionResponse {

    String stdOut();

    String stdErr();

    int getExitStatus();
}
