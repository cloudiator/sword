package de.uniulm.omi.cloudiator.sword.remote;

import de.uniulm.omi.cloudiator.sword.api.remote.AbstractRemoteModule;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnectionFactory;

/**
 * Created by Daniel Seybold on 06.05.2015.
 */
public class OverthereModule extends AbstractRemoteModule {
    @Override
    protected Class<? extends RemoteConnectionFactory> getRemoteConnectionFactory() {
        return OverthereConnectionFactory.class;
    }
}
