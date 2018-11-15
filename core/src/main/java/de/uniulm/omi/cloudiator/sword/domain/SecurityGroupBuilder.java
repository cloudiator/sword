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

package de.uniulm.omi.cloudiator.sword.domain;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Sets;
import java.util.Set;
import javax.annotation.Nullable;

/**
 * Created by daniel on 01.07.16.
 */
public class SecurityGroupBuilder {

  private String id;
  private String providerId;
  private String name;
  @Nullable
  private Location location;
  private Set<SecurityGroupRule> securityGroupRules = Sets.newHashSet();

  private SecurityGroupBuilder() {

  }

  private SecurityGroupBuilder(SecurityGroup securityGroup) {
    id = securityGroup.id();
    providerId = securityGroup.providerId();
    name = securityGroup.name();
    location = securityGroup.location().orElse(null);
    securityGroupRules = Sets.newHashSet(securityGroup.rules());
  }

  public static SecurityGroupBuilder newBuilder() {
    return new SecurityGroupBuilder();
  }

  public static SecurityGroupBuilder of(SecurityGroup securityGroup) {
    checkNotNull(securityGroup, "securityGroup is null");
    return new SecurityGroupBuilder(securityGroup);
  }

  public SecurityGroupBuilder id(String id) {
    this.id = id;
    return this;
  }

  public SecurityGroupBuilder name(String name) {
    this.name = name;
    return this;
  }

  public SecurityGroupBuilder providerId(String providerId) {
    this.providerId = providerId;
    return this;
  }

  public SecurityGroupBuilder location(@Nullable Location location) {
    this.location = location;
    return this;
  }

  public SecurityGroupBuilder addSecurityGroupRule(SecurityGroupRule securityGroupRule) {
    this.securityGroupRules.add(securityGroupRule);
    return this;
  }

  public SecurityGroupBuilder addSecurityGroupRules(Set<SecurityGroupRule> securityGroupRule) {
    this.securityGroupRules.addAll(securityGroupRule);
    return this;
  }

  public SecurityGroup build() {
    return new SecurityGroupImpl(id, providerId, name, location, securityGroupRules);
  }

}
