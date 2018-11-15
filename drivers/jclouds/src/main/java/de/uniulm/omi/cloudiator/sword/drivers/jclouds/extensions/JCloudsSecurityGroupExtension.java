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

package de.uniulm.omi.cloudiator.sword.drivers.jclouds.extensions;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.domain.SecurityGroup;
import de.uniulm.omi.cloudiator.sword.domain.SecurityGroupRule;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.JCloudsViewFactory;
import de.uniulm.omi.cloudiator.sword.extensions.SecurityGroupExtension;
import de.uniulm.omi.cloudiator.sword.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import java.util.Set;
import java.util.stream.Collectors;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.net.domain.IpPermission;

/**
 * Created by daniel on 01.07.16.
 */
public class JCloudsSecurityGroupExtension implements SecurityGroupExtension {

  private final OneWayConverter<org.jclouds.compute.domain.SecurityGroup, SecurityGroup>
      securityGroupConverter;
  private final OneWayConverter<Location, org.jclouds.domain.Location> locationConverter;
  private final GetStrategy<String, Location> locationGetStrategy;
  private final JCloudsViewFactory jCloudsViewFactory;

  @Inject
  public JCloudsSecurityGroupExtension(JCloudsViewFactory jCloudsViewFactory,
      OneWayConverter<org.jclouds.compute.domain.SecurityGroup, SecurityGroup> securityGroupConverter,
      OneWayConverter<Location, org.jclouds.domain.Location> locationConverter,
      GetStrategy<String, Location> locationGetStrategy) {

    checkNotNull(locationGetStrategy, "locationGetStrategy is null");
    this.locationGetStrategy = locationGetStrategy;

    checkNotNull(locationConverter);
    this.locationConverter = locationConverter;

    checkNotNull(securityGroupConverter);
    this.securityGroupConverter = securityGroupConverter;

    checkNotNull(jCloudsViewFactory);
    this.jCloudsViewFactory = jCloudsViewFactory;

  }

  private org.jclouds.compute.extensions.SecurityGroupExtension loadJcloudsSecurityExtension() {
    final ComputeServiceContext computeServiceContext =
        jCloudsViewFactory.buildJCloudsView(ComputeServiceContext.class);
    final Optional<org.jclouds.compute.extensions.SecurityGroupExtension> optional =
        computeServiceContext.getComputeService().getSecurityGroupExtension();
    checkState(optional.isPresent(), "security group extension not present.");
    return optional.get();
  }

  @Override
  public Set<SecurityGroup> listSecurityGroups() {
    return loadJcloudsSecurityExtension().listSecurityGroups().stream()
        .filter(securityGroup -> securityGroup.getName().startsWith("jclouds-"))
        .map(securityGroupConverter).collect(Collectors.toSet());
  }

  @Override
  public SecurityGroup createSecurityGroup(final String name, final String locationId) {
    checkNotNull(name, "name is null");
    checkArgument(!name.isEmpty(), "name is empty");
    checkNotNull(locationId, "locationId is null");
    checkArgument(!locationId.isEmpty(), "locationId is empty");

    Location location = locationGetStrategy.get(locationId);

    checkNotNull(location, String.format("Could not retrieve location with id %s", locationId));

    org.jclouds.domain.Location jcloudsLocation = locationConverter.apply(location);
    return securityGroupConverter
        .apply(this.loadJcloudsSecurityExtension().createSecurityGroup(name, jcloudsLocation));
  }

  @Override
  public SecurityGroup addRule(SecurityGroupRule rule, String securityGroupId) {
    checkNotNull(rule, "rule is null");
    checkNotNull(securityGroupId, "securityGroupId is null");
    checkArgument(!securityGroupId.isEmpty(), "securityGroupId is empty");

    org.jclouds.net.domain.IpProtocol ipProtocol;
    switch (rule.ipProtocol()) {
      case ALL:
        ipProtocol = org.jclouds.net.domain.IpProtocol.ALL;
        break;
      case ICMP:
        ipProtocol = org.jclouds.net.domain.IpProtocol.ICMP;
        break;
      case TCP:
        ipProtocol = org.jclouds.net.domain.IpProtocol.TCP;
        break;
      case UDP:
        ipProtocol = org.jclouds.net.domain.IpProtocol.UDP;
        break;
      default:
        throw new AssertionError("unknown ipProtocol" + rule.ipProtocol());
    }
    org.jclouds.compute.domain.SecurityGroup jcloudsSecurityGroup =
        loadJcloudsSecurityExtension().getSecurityGroupById(securityGroupId);

    IpPermission ipPermission =
        new IpPermission.Builder().cidrBlock(rule.cidr().toString()).fromPort(rule.fromPort())
            .toPort(rule.toPort()).ipProtocol(ipProtocol).build();

    return securityGroupConverter
        .apply(loadJcloudsSecurityExtension().addIpPermission(ipPermission, jcloudsSecurityGroup));
  }


}
