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

package de.uniulm.omi.cloudiator.sword.service.providers;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.AbstractModule;
import de.uniulm.omi.cloudiator.sword.base.BaseComputeService;
import de.uniulm.omi.cloudiator.sword.domain.PropertiesBuilder;
import de.uniulm.omi.cloudiator.sword.drivers.azure.config.AzureComputeModule;
import de.uniulm.omi.cloudiator.sword.drivers.flexiant.config.FlexiantComputeModule;
import de.uniulm.omi.cloudiator.sword.drivers.google.config.GoogleCloudComputeModule;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.config.OpenstackComputeModule;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.config.Openstack4jComputeModule;
import de.uniulm.omi.cloudiator.sword.drivers.ec2.config.Ec2ComputeModule;
import de.uniulm.omi.cloudiator.sword.drivers.profitbricks.config.ProfitbricksComputeModule;
import de.uniulm.omi.cloudiator.sword.exceptions.ProviderNotFoundException;
import de.uniulm.omi.cloudiator.sword.properties.Constants;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    //Openstack
    final Set<AbstractModule> openstackModules = new HashSet<>();
    openstackModules.add(new OpenstackComputeModule());
    registerProvider(
        new ProviderConfiguration("openstack-nova", openstackModules, BaseComputeService.class,
            PropertiesBuilder.newBuilder().putProperty(Constants.IGNORE_LOGIN_KEYPAIR, false)
                .putProperty(Constants.IGNORE_LOGIN_PASSWORD, true)
                .putProperty(Constants.IGNORE_LOGIN_USERNAME, true).build()));
    //Openstack4j
    final Set<AbstractModule> openstack4jModules = new HashSet<>();
    openstack4jModules.add(new Openstack4jComputeModule());
    registerProvider(
        new ProviderConfiguration("openstack4j", openstack4jModules, BaseComputeService.class,
            PropertiesBuilder.newBuilder().build()));
    //Flexiant
    final Set<AbstractModule> flexiantModules = new HashSet<>();
    flexiantModules.add(new FlexiantComputeModule());
    registerProvider(
        new ProviderConfiguration("flexiant", flexiantModules, BaseComputeService.class,
            PropertiesBuilder.newBuilder().build()));
    //EC2
    final Set<AbstractModule> ec2Modules = new HashSet<>();
    ec2Modules.add(new Ec2ComputeModule());
    registerProvider(new ProviderConfiguration("aws-ec2", ec2Modules, BaseComputeService.class,
        PropertiesBuilder.newBuilder().putProperty(Constants.IGNORE_LOGIN_USERNAME, true)
            .build()));
    //Google
    final Set<AbstractModule> googleModules = new HashSet<>();
    googleModules.add(new GoogleCloudComputeModule());
    registerProvider(new ProviderConfiguration("google-compute-engine", googleModules,
        BaseComputeService.class, PropertiesBuilder.newBuilder().build()));
    //Azure
    final Set<AbstractModule> azureModules = new HashSet<>();
    azureModules.add(new AzureComputeModule());
    registerProvider(new ProviderConfiguration("azure", azureModules, BaseComputeService.class,
        PropertiesBuilder.newBuilder().build()));
    //Profitbricks
    final Set<AbstractModule> profitbricksModules = new HashSet<>();
    profitbricksModules.add(new ProfitbricksComputeModule());
    registerProvider(
        new ProviderConfiguration("profitbricks-rest", profitbricksModules,
            BaseComputeService.class,
            PropertiesBuilder.newBuilder().build()));
  }

  public static void registerProvider(ProviderConfiguration providerConfiguration) {
    checkNotNull(providerConfiguration);
    registry.put(providerConfiguration.getName(), providerConfiguration);
  }

  public static ProviderConfiguration getConfigurationByName(String name) {

    if (!registry.containsKey(name)) {
      throw new ProviderNotFoundException(String
          .format("Could not find provider %s. Available providers are: %s", name,
              registry.keySet()));
    }

    return registry.get(name);
  }

}
