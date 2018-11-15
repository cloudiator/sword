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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.strategy;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.Supplier;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.domain.LocationScope;
import de.uniulm.omi.cloudiator.sword.domain.IpProtocol;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.domain.SecurityGroup;
import de.uniulm.omi.cloudiator.sword.domain.SecurityGroupRule;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.domain.SecurityGroupInRegion;
import de.uniulm.omi.cloudiator.sword.util.IdScopeByLocations;
import de.uniulm.omi.cloudiator.sword.util.IdScopedByLocation;
import de.uniulm.omi.cloudiator.sword.util.LocationHierarchy;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import java.util.Optional;
import java.util.Set;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.IPProtocol;
import org.openstack4j.model.compute.SecGroupExtension;

/**
 * Created by daniel on 30.11.16.
 */
public class AssignSecurityGroupRuleToSecurityGroupStrategy {

  private final OSClient osClient;
  private final Supplier<Set<SecurityGroup>> securityGroupSupplier;
  private final OneWayConverter<SecurityGroupInRegion, SecurityGroup> securityGroupConverter;

  @Inject
  public AssignSecurityGroupRuleToSecurityGroupStrategy(OSClient osClient,
      Supplier<Set<SecurityGroup>> securityGroupSupplier,
      OneWayConverter<SecurityGroupInRegion, SecurityGroup> securityGroupConverter) {
    checkNotNull(securityGroupConverter, "securityGroupConverter is null");
    this.securityGroupConverter = securityGroupConverter;
    checkNotNull(securityGroupSupplier, "securityGroupSupplier");
    this.securityGroupSupplier = securityGroupSupplier;
    checkNotNull(osClient, "osClient is null");
    this.osClient = osClient;
  }

  public SecurityGroup assign(SecurityGroupRule securityGroupRule, String securityGroupId) {
    checkNotNull(securityGroupRule, "securityGroupRule is null");
    checkNotNull(securityGroupId, "securityGroupId is null");
    checkArgument(!securityGroupId.isEmpty(), "securityGroupId is empty");

    SecGroupExtension.Rule createdRule;
    if (securityGroupRule.ipProtocol().equals(IpProtocol.ALL)) {
      createdRule = createRule(securityGroupRule.cidr().toString(), IpProtocol.TCP.toString(),
          securityGroupRule.fromPort(), securityGroupRule.toPort(), securityGroupId);
      createdRule = createRule(securityGroupRule.cidr().toString(), IpProtocol.UDP.toString(),
          securityGroupRule.fromPort(), securityGroupRule.toPort(), securityGroupId);
    } else {
      createdRule = createRule(securityGroupRule.cidr().toString(),
          securityGroupRule.ipProtocol().toString(), securityGroupRule.fromPort(),
          securityGroupRule.toPort(), securityGroupId);
    }

    SecGroupExtension.Rule finalCreatedRule = createdRule;
    final Optional<SecurityGroup> any = securityGroupSupplier.get().stream().filter(
        securityGroupRemote -> securityGroupRemote.providerId()
            .equals(finalCreatedRule.getParentGroupId())).findAny();
    checkState(any.isPresent(),
        String.format("Could not find security group %s.", securityGroupId));
    return any.get();
  }

  private SecGroupExtension.Rule createRule(String cidr, String ipProtocol, int from, int to,
      String securityGroupId) {

    final IdScopedByLocation scopedByLocation = IdScopeByLocations.from(securityGroupId);

    SecurityGroup securityGroup = securityGroupSupplier.get().stream().filter(
        securityGroupCandidate -> securityGroupCandidate.providerId()
            .equals(scopedByLocation.getId()))
        .findAny().orElseThrow(() -> new IllegalStateException(
            String.format("Could not find security group with provider id %s.",
                scopedByLocation.getId())));

    Location region = LocationHierarchy.of(securityGroup.location().get())
        .firstParentLocationWithScope(LocationScope.REGION).orElseThrow(
            () -> new IllegalStateException(String
                .format("Could not find parent region of location %s",
                    securityGroup.location().get())));

    SecGroupExtension.Rule rule =
        Builders.secGroupRule().cidr(cidr).protocol(IPProtocol.valueOf(ipProtocol))
            .range(from, to).parentGroupId(securityGroup.providerId()).build();

    return osClient.useRegion(region.providerId()).compute().securityGroups().createRule(rule);

  }
}
