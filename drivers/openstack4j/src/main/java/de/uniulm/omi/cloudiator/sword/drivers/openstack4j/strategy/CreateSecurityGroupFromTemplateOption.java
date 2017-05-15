/*
 * Copyright (c) 2014-2016 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.strategy;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.domain.LocationScope;
import de.uniulm.omi.cloudiator.sword.domain.CidrImpl;
import de.uniulm.omi.cloudiator.sword.domain.IpProtocol;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.domain.SecurityGroup;
import de.uniulm.omi.cloudiator.sword.domain.SecurityGroupRuleBuilder;
import de.uniulm.omi.cloudiator.sword.domain.TemplateOptions;
import de.uniulm.omi.cloudiator.sword.extensions.SecurityGroupExtension;
import de.uniulm.omi.cloudiator.sword.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.sword.util.LocationHierarchy;
import de.uniulm.omi.cloudiator.sword.util.NamingStrategy;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by daniel on 30.11.16.
 */
public class CreateSecurityGroupFromTemplateOption {

  public static final String DEFAULT_SEC_GROUP_NAME = "default";
  private final SecurityGroupExtension securityGroupExtension;
  private final NamingStrategy namingStrategy;
  private final GetStrategy<String, Location> locationGetStrategy;

  @Inject
  public CreateSecurityGroupFromTemplateOption(
      Optional<SecurityGroupExtension> securityGroupExtension, NamingStrategy namingStrategy,
      GetStrategy<String, Location> locationGetStrategy) {
    checkNotNull(locationGetStrategy, "locationGetStrategy is null");
    this.locationGetStrategy = locationGetStrategy;
    checkNotNull(namingStrategy, "namingStrategy is null");
    this.namingStrategy = namingStrategy;
    checkNotNull(securityGroupExtension, "securityGroupExtension is null");
    checkState(securityGroupExtension.isPresent(),
        "securityGroupExtension needs to be present.");
    this.securityGroupExtension = securityGroupExtension.get();
  }

  public String create(TemplateOptions templateOptions, String locationId) {
    checkNotNull(templateOptions, "templateOptions is null");
    checkNotNull(locationId, "locationId is null");
    final Location location = locationGetStrategy.get(locationId);
    checkState(location != null, "Could not retrieve location with id " + locationId);

    Location region =
        LocationHierarchy.of(location).firstParentLocationWithScope(LocationScope.REGION)
            .orElseThrow(() -> new IllegalStateException(
                String.format("Could not find parent region of location %s", location)));

    //check if already exists
    final Set<SecurityGroup> collect = securityGroupExtension.listSecurityGroups().stream()
        .filter(securityGroup -> securityGroup.name()
            .equals(namingStrategy.generateNameBasedOnName(DEFAULT_SEC_GROUP_NAME)))
        .filter(securityGroup -> {
          checkState(securityGroup.location().isPresent(),
              String.format("SecurityGroup %s has no location.", securityGroup));
          return securityGroup.location().get().id().equals(region.id());
        }).collect(Collectors.toSet());
    if (collect.size() > 1) {
      throw new IllegalStateException(String.format(
          "Found multiple (%s) security groups matching the name %s. Not sure what to use. Please delete unneeded ones.",
          collect.size(), namingStrategy.generateNameBasedOnName(DEFAULT_SEC_GROUP_NAME)));
    } else if (collect.size() == 1) {
      return collect.iterator().next().name();
    }

    //create
    SecurityGroup securityGroup =
        securityGroupExtension.createSecurityGroup(DEFAULT_SEC_GROUP_NAME, location.id());

    //add rules
    templateOptions.inboundPorts().forEach(integer -> {
      securityGroupExtension.addRule(
          SecurityGroupRuleBuilder.newBuilder().ipProtocol(IpProtocol.ALL).fromPort(integer)
              .toPort(integer).cidr(CidrImpl.ALL).build(), securityGroup.id());
    });
    return securityGroup.name();
  }

}
