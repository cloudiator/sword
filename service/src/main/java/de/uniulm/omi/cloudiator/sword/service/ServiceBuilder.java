/*
 * Copyright (c) 2014-2015 University of Ulm
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.uniulm.omi.cloudiator.sword.service;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import de.uniulm.omi.cloudiator.sword.config.BaseModule;
import de.uniulm.omi.cloudiator.sword.config.DefaultMetaModule;
import de.uniulm.omi.cloudiator.sword.domain.Cloud;
import de.uniulm.omi.cloudiator.sword.domain.PropertiesBuilder;
import de.uniulm.omi.cloudiator.sword.logging.AbstractLoggingModule;
import de.uniulm.omi.cloudiator.sword.logging.NullLoggingModule;
import de.uniulm.omi.cloudiator.sword.remote.AbstractRemoteModule;
import de.uniulm.omi.cloudiator.sword.remote.overthere.OverthereModule;
import de.uniulm.omi.cloudiator.sword.service.providers.ProviderConfiguration;
import de.uniulm.omi.cloudiator.sword.service.providers.Providers;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by daniel on 02.12.14.
 */
public class ServiceBuilder {


  private Cloud cloud;
  private AbstractLoggingModule loggingModule;
  private AbstractRemoteModule remoteModule;
  private DefaultMetaModule metaModule = new DefaultMetaModule();

  private ServiceBuilder() {

  }

  public static ServiceBuilder newServiceBuilder() {
    return new ServiceBuilder();
  }

  public ServiceBuilder loggingModule(AbstractLoggingModule loggingModule) {
    this.loggingModule = loggingModule;
    return this;
  }

  public ServiceBuilder remoteModule(AbstractRemoteModule abstractRemoteModule) {
    this.remoteModule = abstractRemoteModule;
    return this;
  }

  public ServiceBuilder metaModule(DefaultMetaModule abstractMetaModule) {
    this.metaModule = abstractMetaModule;
    return this;
  }

  public ServiceBuilder cloud(Cloud cloud) {
    this.cloud = cloud;
    return this;
  }

  public ComputeService build() {
    ProviderConfiguration providerConfiguration =
        Providers.getConfigurationByName(cloud.api().providerName());
    checkNotNull(providerConfiguration);
    return this.buildInjector(providerConfiguration.getModules(), providerConfiguration)
        .getInstance(providerConfiguration.getComputeService());
  }

  protected Injector buildInjector(Collection<? extends Module> modules,
      ProviderConfiguration providerConfiguration) {
    Collection<Module> basicModules = this.getBasicModules(providerConfiguration);
    basicModules.addAll(modules);
    basicModules.add(buildLoggingModule());
    basicModules.add(buildRemoteModule());
    basicModules.add(metaModule);
    return Guice.createInjector(basicModules);
  }

  protected Set<Module> getBasicModules(ProviderConfiguration providerConfiguration) {
    Set<Module> modules = new HashSet<>();
    PropertiesBuilder propertiesBuilder = PropertiesBuilder.newBuilder()
        .putProperties(providerConfiguration.getDefaultProperties().getProperties())
        .putProperties(cloud.configuration().properties().getProperties());
    modules.add(new BaseModule(cloud, propertiesBuilder.build()));
    return modules;
  }

  private AbstractLoggingModule buildLoggingModule() {
    if (loggingModule == null) {
      return new NullLoggingModule();
    }
    return loggingModule;
  }

  private AbstractRemoteModule buildRemoteModule() {
    if (remoteModule == null) {
      return new OverthereModule();
    }
    return remoteModule;
  }
}
