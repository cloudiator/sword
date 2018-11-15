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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.config;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.collect.Sets;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import de.uniulm.omi.cloudiator.sword.config.AbstractComputeModule;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.Image;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.domain.SecurityGroup;
import de.uniulm.omi.cloudiator.sword.domain.SecurityGroupRule;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.converters.AvailabilityZoneInRegionToLocation;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.converters.FlavorInRegionToHardwareFlavor;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.converters.ImageInRegionToImage;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.converters.RegionToLocation;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.converters.RuleToSecurityGroupRuleConverter;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.converters.SecurityGroupInRegionToSecurityGroup;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.converters.ServerInRegionToVirtualMachine;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.domain.AvailabilityZoneInRegion;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.domain.FlavorInRegion;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.domain.ImageInRegion;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.domain.SecurityGroupInRegion;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.domain.ServerInRegion;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.extensions.Openstack4JKeyPairExtension;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.extensions.Openstack4JPublicIpExtension;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.extensions.Openstack4JSecurityGroupExtension;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.internal.KeyStoneVersion;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.internal.KeyStoneVersionProvider;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.internal.Openstack4jClientProvider;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.internal.OsClientFactory;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.internal.OsClientFactoryProvider;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.internal.RegionSupplier;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.internal.RegionSupplierProvider;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.strategy.AssignSecurityGroupRuleToSecurityGroupStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.strategy.CompositeFloatingIpPoolStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.strategy.ConfigurationFloatingIpPoolStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.strategy.CreateSecurityGroupFromTemplateOption;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.strategy.CreateSecurityGroupStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.strategy.FloatingIpPoolStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.strategy.OneFloatingIpPoolStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.strategy.Openstack4jCreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.strategy.Openstack4jDeleteVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.strategy.OpenstackConfiguredNetworkStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.strategy.OpenstackNetworkStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.suppliers.HardwareFlavorSupplier;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.suppliers.ImageSupplier;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.suppliers.LocationSupplier;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.suppliers.SecurityGroupSupplier;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.suppliers.VirtualMachineSupplier;
import de.uniulm.omi.cloudiator.sword.extensions.KeyPairExtension;
import de.uniulm.omi.cloudiator.sword.extensions.PublicIpExtension;
import de.uniulm.omi.cloudiator.sword.extensions.SecurityGroupExtension;
import de.uniulm.omi.cloudiator.sword.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.strategy.DeleteVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import java.util.Set;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.SecGroupExtension;


/**
 * Created by daniel on 02.12.14.
 */
public class Openstack4jComputeModule extends AbstractComputeModule {

  @Override
  protected Supplier<Set<Image>> imageSupplier(Injector injector) {
    return injector.getInstance(ImageSupplier.class);
  }

  @Override
  protected Supplier<Set<Location>> locationSupplier(Injector injector) {
    return injector.getInstance(LocationSupplier.class);
  }

  @Override
  protected Supplier<Set<HardwareFlavor>> hardwareFlavorSupplier(Injector injector) {
    return injector.getInstance(HardwareFlavorSupplier.class);
  }

  @Override
  protected Supplier<Set<VirtualMachine>> virtualMachineSupplier(Injector injector) {
    return injector.getInstance(VirtualMachineSupplier.class);
  }

  @Override
  protected CreateVirtualMachineStrategy createVirtualMachineStrategy(Injector injector) {
    return injector.getInstance(Openstack4jCreateVirtualMachineStrategy.class);
  }

  @Override
  protected DeleteVirtualMachineStrategy deleteVirtualMachineStrategy(Injector injector) {
    return injector.getInstance(Openstack4jDeleteVirtualMachineStrategy.class);
  }

  @Override
  protected Optional<PublicIpExtension> publicIpService(Injector injector) {
    return Optional.of(injector.getInstance(Openstack4JPublicIpExtension.class));
  }

  @Override
  protected Optional<SecurityGroupExtension> securityGroupService(Injector injector) {
    return Optional.of(injector.getInstance(Openstack4JSecurityGroupExtension.class));
  }

  @Override
  protected Optional<KeyPairExtension> keyPairService(Injector injector) {
    return Optional.of(injector.getInstance(Openstack4JKeyPairExtension.class));
  }

  @Override
  protected void configure() {
    super.configure();
    bind(OSClient.class).toProvider(Openstack4jClientProvider.class).in(Singleton.class);
    bind(KeyStoneVersion.class).toProvider(KeyStoneVersionProvider.class).in(Singleton.class);
    bind(OsClientFactory.class).toProvider(OsClientFactoryProvider.class).in(Singleton.class);
    bind(RegionSupplier.class).toProvider(RegionSupplierProvider.class).in(Singleton.class);
    bind(OpenstackNetworkStrategy.class).to(OpenstackConfiguredNetworkStrategy.class)
        .in(Singleton.class);
    bind(new TypeLiteral<OneWayConverter<FlavorInRegion, HardwareFlavor>>() {
    }).to(FlavorInRegionToHardwareFlavor.class).in(Singleton.class);
    bind(new TypeLiteral<OneWayConverter<ImageInRegion, Image>>() {
    }).to(ImageInRegionToImage.class).in(Singleton.class);
    bind(new TypeLiteral<OneWayConverter<AvailabilityZoneInRegion, Location>>() {
    }).to(AvailabilityZoneInRegionToLocation.class).in(Singleton.class);
    bind(new TypeLiteral<OneWayConverter<String, Location>>() {
    }).to(RegionToLocation.class).in(Singleton.class);
    bind(new TypeLiteral<OneWayConverter<ServerInRegion, VirtualMachine>>() {
    }).to(ServerInRegionToVirtualMachine.class).in(Singleton.class);
    bind(new TypeLiteral<OneWayConverter<SecGroupExtension.Rule, SecurityGroupRule>>() {
    }).to(RuleToSecurityGroupRuleConverter.class).in(Singleton.class);
    bind(new TypeLiteral<OneWayConverter<SecurityGroupInRegion, SecurityGroup>>() {
    }).to(SecurityGroupInRegionToSecurityGroup.class).in(Singleton.class);
    bind(new TypeLiteral<Supplier<Set<SecurityGroup>>>() {
    }).to(SecurityGroupSupplier.class).in(Singleton.class);
    bind(CreateSecurityGroupStrategy.class).in(Singleton.class);
    bind(AssignSecurityGroupRuleToSecurityGroupStrategy.class).in(Singleton.class);
    bind(CreateSecurityGroupFromTemplateOption.class).in(Singleton.class);
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
