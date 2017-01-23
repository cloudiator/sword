package de.uniulm.omi.cloudiator.sword.remote.internal;

import com.google.inject.Injector;
import com.google.inject.Provides;
import de.uniulm.omi.cloudiator.sword.api.annotations.Base;
import de.uniulm.omi.cloudiator.sword.api.remote.AbstractRemoteModule;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnectionFactory;

/**
 * An abstract module for the definition of an own remote connection module.
 */
public abstract class ExtendedRemoteModule extends AbstractRemoteModule {

    @Override protected void configure() {
    }

    @Provides @Base final RemoteConnectionFactory provideBaseConnectionFactory(Injector injector) {
        return injector.getInstance(getRemoteConnectionFactory());
    }

    @Provides final RemoteConnectionFactory provideConnectionFactory(Injector injector) {
        return injector.getInstance(RetryingConnectionFactory.class);
    }
}
