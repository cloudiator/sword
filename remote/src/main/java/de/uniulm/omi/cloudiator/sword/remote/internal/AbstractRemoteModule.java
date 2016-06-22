package de.uniulm.omi.cloudiator.sword.remote.internal;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import de.uniulm.omi.cloudiator.sword.api.annotations.Base;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnectionFactory;

/**
 * An abstract module for the definition of an own remote connection module.
 */
public abstract class AbstractRemoteModule extends AbstractModule {

    @Override protected void configure() {
    }

    @Provides @Base final RemoteConnectionFactory provideBaseConnectionFactory(Injector injector) {
        return getRemoteConnectionFactory(injector);
    }

    @Provides final RemoteConnectionFactory provideConnectionFactory(Injector injector) {
        return injector.getInstance(RetryingConnectionFactory.class);
    }

    /**
     * Used for defining the class that will be used for creating
     * remote connections ({@link RemoteConnectionFactory}).
     *
     * @return a class implementing a RemoteConnectionFactory.
     */
    protected abstract RemoteConnectionFactory getRemoteConnectionFactory(Injector injector);
}
