package de.uniulm.omi.cloudiator.sword.api.remote;

import com.google.inject.AbstractModule;

/**
 * Created by Daniel Seybold on 06.05.2015.
 */
public abstract class AbstractRemoteModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(RemoteConnectionFactory.class).to(getRemoteConnectionFactory());
    }

    protected abstract Class<? extends RemoteConnectionFactory> getRemoteConnectionFactory();
}
