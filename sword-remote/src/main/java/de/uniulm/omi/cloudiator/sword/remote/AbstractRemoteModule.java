package de.uniulm.omi.cloudiator.sword.remote;

import com.google.inject.AbstractModule;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnectionFactory;

/**
 * An abstract module for the definition of an own remote connection module.
 */
public abstract class AbstractRemoteModule extends AbstractModule {

    @Override protected void configure() {
        bind(RemoteConnectionFactory.class)
            .toInstance(new RetryingConnectionFactory(getRemoteConnectionFactory()));
    }

    /**
     * Used for defining the class that will be used for creating
     * remote connections ({@link RemoteConnectionFactory}).
     *
     * @return a class implementing a RemoteConnectionFactory.
     */
    protected abstract RemoteConnectionFactory getRemoteConnectionFactory();
}
