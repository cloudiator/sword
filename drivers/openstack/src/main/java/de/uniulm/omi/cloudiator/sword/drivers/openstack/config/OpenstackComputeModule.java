/*
 * Copyright (c) 2014-2018 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.drivers.openstack.config;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import de.uniulm.omi.cloudiator.sword.domain.KeyPair;
import de.uniulm.omi.cloudiator.sword.domain.TemplateOptions;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.JCloudsComputeClient;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.config.JCloudsComputeModule;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.OpenstackComputeClientImpl;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.converters.NovaKeyPairToKeypair;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.converters.TemplateOptionsToNovaTemplateOptions;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.domain.KeyPairInRegion;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.extensions.OpenstackKeyPairExtension;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.extensions.OpenstackPublicIpExtension;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.extensions.OpenstackQuotaExtension;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.strategy.CompositeFloatingIpPoolStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.strategy.ConfigurationFloatingIpPoolStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.strategy.FloatingIpPoolStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.strategy.OneFloatingIpPoolStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.strategy.OpenstackCreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.strategy.OpenstackDeleteVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.extensions.KeyPairExtension;
import de.uniulm.omi.cloudiator.sword.extensions.PublicIpExtension;
import de.uniulm.omi.cloudiator.sword.extensions.QuotaExtension;
import de.uniulm.omi.cloudiator.sword.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.strategy.DeleteVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import java.util.Set;
import org.jclouds.openstack.nova.v2_0.NovaApi;

/**
 * Compute module for the openstack nova compute api.
 */
public class OpenstackComputeModule extends JCloudsComputeModule {

  @Override
  protected Class<? extends OneWayConverter<TemplateOptions, org.jclouds.compute.options.TemplateOptions>> templateOptionsConverter() {
    return TemplateOptionsToNovaTemplateOptions.class;
  }

  @Override
  protected void configure() {
    super.configure();
    bind(NovaApi.class).toProvider(NovaApiProvider.class);
    bind(new TypeLiteral<OneWayConverter<KeyPairInRegion, KeyPair>>() {
    }).to(NovaKeyPairToKeypair.class);
  }

  @Override
  protected Optional<PublicIpExtension> publicIpService(Injector injector) {
    //todo should be dependent on openstack floating ip extension being available.
    return Optional.fromNullable(injector.getInstance(OpenstackPublicIpExtension.class));
  }

  @Override
  protected Optional<KeyPairExtension> keyPairService(Injector injector) {
    //todo should be dependent on openstack key pair extension being available.
    return Optional.fromNullable(injector.getInstance(OpenstackKeyPairExtension.class));
  }

  @Override
  protected Optional<QuotaExtension> quotaExtension(Injector injector) {
    return Optional.of(injector.getInstance(OpenstackQuotaExtension.class));
  }

  @Override
  protected JCloudsComputeClient overrideComputeClient(Injector injector,
      JCloudsComputeClient originalComputeClient) {
    return new OpenstackComputeClientImpl(originalComputeClient,
        injector.getInstance(NovaApi.class));
  }

  @Override
  protected CreateVirtualMachineStrategy overrideCreateVirtualMachineStrategy(Injector injector,
      CreateVirtualMachineStrategy original) {
    return injector.getInstance(OpenstackCreateVirtualMachineStrategy.class);
  }

  @Override
  protected DeleteVirtualMachineStrategy overrideDeleteVirtualMachineStrategy(Injector injector,
      DeleteVirtualMachineStrategy original) {
    return new OpenstackDeleteVirtualMachineStrategy(original);
  }

  @Provides
  @Singleton
  FloatingIpPoolStrategy provideFloatingIpPoolStrategy(Injector injector) {
    Set<FloatingIpPoolStrategy> availableStrategies = Sets.newLinkedHashSetWithExpectedSize(2);
    availableStrategies.add(injector.getInstance(ConfigurationFloatingIpPoolStrategy.class));
    availableStrategies.add(injector.getInstance(OneFloatingIpPoolStrategy.class));
    return new CompositeFloatingIpPoolStrategy(availableStrategies);
  }


}
