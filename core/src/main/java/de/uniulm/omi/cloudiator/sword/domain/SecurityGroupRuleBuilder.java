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

/**
 * Created by daniel on 01.07.16.
 */
public class SecurityGroupRuleBuilder {

  private IpProtocol ipProtocol;
  private int fromPort;
  private int toPort;
  private Cidr cidr;

  private SecurityGroupRuleBuilder() {

  }

  private SecurityGroupRuleBuilder(SecurityGroupRule securityGroupRule) {
    ipProtocol = securityGroupRule.ipProtocol();
    fromPort = securityGroupRule.fromPort();
    toPort = securityGroupRule.toPort();
    cidr = securityGroupRule.cidr();
  }

  public static SecurityGroupRuleBuilder newBuilder() {
    return new SecurityGroupRuleBuilder();
  }

  private SecurityGroupRuleBuilder of(SecurityGroupRule securityGroupRule) {
    checkNotNull(securityGroupRule, "SecurityGroupRule is null");
    return new SecurityGroupRuleBuilder(securityGroupRule);
  }

  public SecurityGroupRuleBuilder ipProtocol(IpProtocol ipProtocol) {
    this.ipProtocol = ipProtocol;
    return this;
  }

  public SecurityGroupRuleBuilder fromPort(int fromPort) {
    this.fromPort = fromPort;
    return this;
  }

  public SecurityGroupRuleBuilder toPort(int toPort) {
    this.toPort = toPort;
    return this;
  }

  public SecurityGroupRuleBuilder cidr(Cidr cidr) {
    this.cidr = cidr;
    return this;
  }

  public SecurityGroupRule build() {
    return new SecurityGroupRuleImpl(ipProtocol, fromPort, toPort, cidr);
  }

}
