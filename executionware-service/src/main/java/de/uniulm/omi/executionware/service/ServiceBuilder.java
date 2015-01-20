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

package de.uniulm.omi.executionware.service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import de.uniulm.omi.executionware.api.ServiceConfiguration;
import de.uniulm.omi.executionware.api.domain.LoginCredential;
import de.uniulm.omi.executionware.api.properties.ServiceProperties;
import de.uniulm.omi.executionware.api.service.ComputeService;
import de.uniulm.omi.executionware.core.config.BaseModule;
import de.uniulm.omi.executionware.service.providers.ProviderConfiguration;
import de.uniulm.omi.executionware.service.providers.Providers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 02.12.14.
 */
public class ServiceBuilder {

    private final ServiceConfigurationBuilder serviceConfigurationBuilder;

    private ServiceBuilder(String provider) {
        this.serviceConfigurationBuilder = new ServiceConfigurationBuilder();
        this.serviceConfigurationBuilder.provider(provider);
    }

    public static ServiceBuilder newServiceBuilder(String provider) {
        return new ServiceBuilder(provider);
    }

    public ServiceBuilder endpoint(String endpoint) {
        this.serviceConfigurationBuilder.endpoint(endpoint);
        return this;
    }

    public ServiceBuilder credentials(String username, String password) {
        this.serviceConfigurationBuilder.username(username);
        this.serviceConfigurationBuilder.password(password);
        return this;
    }

    public ServiceBuilder nodeGroup(String nodeGroup) {
        this.serviceConfigurationBuilder.nodeGroup(nodeGroup);
        return this;
    }

    public ServiceBuilder loginCredentials(LoginCredential loginCredential) {
        this.serviceConfigurationBuilder.loginCredential(loginCredential);
        return this;
    }

    public ServiceBuilder properties(ServiceProperties serviceProperties) {
        this.serviceConfigurationBuilder.properties(serviceProperties);
        return this;
    }

    public ComputeService build() {
        ServiceConfiguration serviceConfiguration = this.serviceConfigurationBuilder.build();
        ProviderConfiguration providerConfiguration = Providers.getConfigurationByName(serviceConfiguration.getProvider());
        checkNotNull(providerConfiguration);
        return this.buildInjector(providerConfiguration.getModules(), serviceConfiguration).getInstance(providerConfiguration.getComputeService());
    }

    protected Injector buildInjector(Collection<? extends Module> modules, ServiceConfiguration serviceConfiguration) {
        Collection<Module> basicModules = this.getBasicModules(serviceConfiguration);
        basicModules.addAll(modules);
        return Guice.createInjector(basicModules);
    }

    protected Set<Module> getBasicModules(ServiceConfiguration serviceConfiguration) {
        Set<Module> modules = new HashSet<>();
        modules.add(new BaseModule(serviceConfiguration));
        return modules;
    }
}
