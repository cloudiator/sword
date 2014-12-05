/*
 * Copyright (c) 2014 University of Ulm
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

package de.uniulm.omi.executionware.service.providers;

import com.google.inject.AbstractModule;
import de.uniulm.omi.executionware.api.exceptions.ProviderNotFoundException;
import de.uniulm.omi.executionware.drivers.flexiant.FlexiantComputeService;
import de.uniulm.omi.executionware.drivers.flexiant.config.FlexiantComputeModule;
import de.uniulm.omi.executionware.drivers.jclouds.JCloudsComputeService;
import de.uniulm.omi.executionware.drivers.jclouds.config.JCloudsComputeModule;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 02.12.14.
 */
public class Providers {

    private static final Map<String, ProviderConfiguration> registry = new HashMap<>();

    static {
        registerDefaultProviders();
    }

    private Providers() {

    }

    private static void registerDefaultProviders() {
        final Set<AbstractModule> jCloudsModules = new HashSet<>();
        jCloudsModules.add(new JCloudsComputeModule());
        registerProvider(new ProviderConfiguration("openstack-nova", jCloudsModules, JCloudsComputeService.class));
        Set<AbstractModule> flexiantModules = new HashSet<>();
        flexiantModules.add(new FlexiantComputeModule());
        registerProvider(new ProviderConfiguration("flexiant", flexiantModules, FlexiantComputeService.class));
    }

    public static void registerProvider(ProviderConfiguration providerConfiguration) {
        checkNotNull(providerConfiguration);
        registry.put(providerConfiguration.getName(), providerConfiguration);
    }

    public static ProviderConfiguration getConfigurationByName(String name) throws ProviderNotFoundException {

        if (!registry.containsKey(name)) {
            throw new ProviderNotFoundException(String.format("Could not find provider %s. Available providers are: %s", name, registry.keySet()));
        }

        return registry.get(name);
    }

}
