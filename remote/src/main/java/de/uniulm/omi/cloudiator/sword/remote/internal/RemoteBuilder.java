/*
 * Copyright (c) 2014-2015 University of Ulm
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.uniulm.omi.cloudiator.sword.remote.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.name.Names;
import de.uniulm.omi.cloudiator.sword.base.BaseConnectionService;
import de.uniulm.omi.cloudiator.sword.domain.Properties;
import de.uniulm.omi.cloudiator.sword.logging.AbstractLoggingModule;
import de.uniulm.omi.cloudiator.sword.logging.NullLoggingModule;
import de.uniulm.omi.cloudiator.sword.remote.AbstractRemoteModule;
import de.uniulm.omi.cloudiator.sword.service.ConnectionService;

/**
 * Created by daniel on 02.09.15.
 */
public class RemoteBuilder {

  private AbstractLoggingModule loggingModule;
  private AbstractRemoteModule remoteModule;
  private Properties properties;

  private RemoteBuilder() {
  }

  public static RemoteBuilder newBuilder() {
    return new RemoteBuilder();
  }

  public RemoteBuilder loggingModule(AbstractLoggingModule loggingModule) {
    this.loggingModule = loggingModule;
    return this;
  }

  public RemoteBuilder remoteModule(AbstractRemoteModule abstractRemoteModule) {
    this.remoteModule = abstractRemoteModule;
    return this;
  }

  public RemoteBuilder properties(Properties properties) {
    this.properties = properties;
    return this;
  }

  public ConnectionService build() {

    if (loggingModule == null) {
      loggingModule = new NullLoggingModule();
    }
    checkNotNull(remoteModule, "No remote module set.");

    return Guice.createInjector(loggingModule, remoteModule, new AbstractModule() {
      @Override
      protected void configure() {
        bind(ConnectionService.class).to(BaseConnectionService.class);
        if (properties != null) {
          Names.bindProperties(binder(), properties.getProperties());
        }
      }
    }).getInstance(ConnectionService.class);
  }

}
