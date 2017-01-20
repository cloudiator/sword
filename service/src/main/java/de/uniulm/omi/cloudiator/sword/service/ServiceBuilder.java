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

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import de.uniulm.omi.cloudiator.sword.api.ServiceContext;
import de.uniulm.omi.cloudiator.sword.api.domain.Cloud;
import de.uniulm.omi.cloudiator.sword.api.domain.Configuration;
import de.uniulm.omi.cloudiator.sword.api.service.ComputeService;
import de.uniulm.omi.cloudiator.sword.core.config.BaseModule;
import de.uniulm.omi.cloudiator.sword.core.logging.AbstractLoggingModule;
import de.uniulm.omi.cloudiator.sword.core.logging.NullLoggingModule;
import de.uniulm.omi.cloudiator.sword.core.properties.PropertiesBuilder;
import de.uniulm.omi.cloudiator.sword.remote.internal.AbstractRemoteModule;
import de.uniulm.omi.cloudiator.sword.remote.overthere.OverthereModule;
import de.uniulm.omi.cloudiator.sword.service.providers.ProviderConfiguration;
import de.uniulm.omi.cloudiator.sword.service.providers.Providers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 02.12.14.
 */
public class ServiceBuilder {


    private Cloud cloud;
    private Configuration configuration;
    private AbstractLoggingModule loggingModule;
    private AbstractRemoteModule remoteModule;

    public static ServiceBuilder newServiceBuilder() {
        return new ServiceBuilder();
    }

    private ServiceBuilder() {

    }

    public ServiceBuilder loggingModule(AbstractLoggingModule loggingModule) {
        this.loggingModule = loggingModule;
        return this;
    }

    public ServiceBuilder remoteModule(AbstractRemoteModule abstractRemoteModule) {
        this.remoteModule = abstractRemoteModule;
        return this;
    }

    public ServiceBuilder serviceContext(ServiceContext serviceContext) {
        this.cloud = serviceContext.cloud();
        this.configuration = serviceContext.configuration();
        return this;
    }

    public ServiceBuilder cloud(Cloud cloud) {
        this.cloud = cloud;
        return this;
    }

    public ServiceBuilder configuration(Configuration configuration) {
        this.configuration = configuration;
        return this;
    }

    public ComputeService build() {
        ServiceContext serviceContext =
            ServiceContextBuilder.newBuilder().cloud(cloud).configuration(configuration).build();
        ProviderConfiguration providerConfiguration =
            Providers.getConfigurationByName(serviceContext.cloud().api().providerName());
        checkNotNull(providerConfiguration);
        return this.buildInjector(providerConfiguration.getModules(), serviceContext,
            providerConfiguration).getInstance(providerConfiguration.getComputeService());
    }

    protected Injector buildInjector(Collection<? extends Module> modules,
        ServiceContext serviceContext, ProviderConfiguration providerConfiguration) {
        Collection<Module> basicModules =
            this.getBasicModules(serviceContext, providerConfiguration);
        basicModules.addAll(modules);
        basicModules.add(buildLoggingModule());
        basicModules.add(buildRemoteModule());
        return Guice.createInjector(basicModules);
    }

    protected Set<Module> getBasicModules(ServiceContext serviceContext,
        ProviderConfiguration providerConfiguration) {
        Set<Module> modules = new HashSet<>();
        PropertiesBuilder propertiesBuilder = PropertiesBuilder.newBuilder()
            .putProperties(providerConfiguration.getDefaultProperties().getProperties());
        modules.add(new BaseModule(serviceContext, propertiesBuilder.build()));
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
