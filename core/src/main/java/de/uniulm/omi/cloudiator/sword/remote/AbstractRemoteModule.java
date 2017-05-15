package de.uniulm.omi.cloudiator.sword.remote;

import com.google.inject.AbstractModule;

/**
 * An abstract module for the definition of an own remote connection module.
 */
public abstract class AbstractRemoteModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(RemoteConnectionFactory.class).to(getRemoteConnectionFactory());
  }

  /**
   * Used for defining the class that will be used for creating
   * remote connections ({@link RemoteConnectionFactory}).
   *
   * @return a class implementing a RemoteConnectionFactory.
   */
  protected abstract Class<? extends RemoteConnectionFactory> getRemoteConnectionFactory();
}
