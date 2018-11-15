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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.extensions;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Supplier;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.domain.SecurityGroup;
import de.uniulm.omi.cloudiator.sword.domain.SecurityGroupRule;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.strategy.AssignSecurityGroupRuleToSecurityGroupStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.strategy.CreateSecurityGroupStrategy;
import de.uniulm.omi.cloudiator.sword.extensions.SecurityGroupExtension;
import java.util.Set;

/**
 * Created by daniel on 29.11.16.
 */
public class Openstack4JSecurityGroupExtension implements SecurityGroupExtension {

  private final Supplier<Set<SecurityGroup>> securityGroupSupplier;
  private final AssignSecurityGroupRuleToSecurityGroupStrategy
      assignSecurityGroupRuleToSecurityGroupStrategy;
  private final CreateSecurityGroupStrategy createSecurityGroupStrategy;

  @Inject
  public Openstack4JSecurityGroupExtension(Supplier<Set<SecurityGroup>> securityGroupSupplier,
      AssignSecurityGroupRuleToSecurityGroupStrategy assignSecurityGroupRuleToSecurityGroupStrategy,
      CreateSecurityGroupStrategy createSecurityGroupStrategy) {
    checkNotNull(assignSecurityGroupRuleToSecurityGroupStrategy,
        "assignSecurityGroupRuleToSecurityGroupStrategy is null");
    this.assignSecurityGroupRuleToSecurityGroupStrategy =
        assignSecurityGroupRuleToSecurityGroupStrategy;
    checkNotNull(createSecurityGroupStrategy, "createSecurityGroupStrategy is null");
    this.createSecurityGroupStrategy = createSecurityGroupStrategy;
    checkNotNull(securityGroupSupplier, "securityGroupSupplier is null");
    this.securityGroupSupplier = securityGroupSupplier;
  }

  @Override
  public Set<SecurityGroup> listSecurityGroups() {
    return securityGroupSupplier.get();
  }

  @Override
  public SecurityGroup createSecurityGroup(String name, String locationId) {
    return createSecurityGroupStrategy.create(name, locationId);
  }

  @Override
  public SecurityGroup addRule(SecurityGroupRule rule, String securityGroupId) {
    return assignSecurityGroupRuleToSecurityGroupStrategy.assign(rule, securityGroupId);
  }
}
