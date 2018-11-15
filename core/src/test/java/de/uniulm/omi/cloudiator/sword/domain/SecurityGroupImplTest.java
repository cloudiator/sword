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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

/**
 * Created by daniel on 17.01.17.
 */
public class SecurityGroupImplTest {

  @Test(expected = NullPointerException.class)
  public void nullSecurityGroupRulesThrowsNullPointerException() {
    new SecurityGroupImpl("id", "providerId", "name", null, null);
  }

  @Test
  public void rulesTest() {
    final SecurityGroupRule all =
        SecurityGroupRuleBuilder.newBuilder().cidr(CidrImpl.ALL).fromPort(0).toPort(5)
            .ipProtocol(IpProtocol.ALL).build();
    final SecurityGroupRule tcp =
        SecurityGroupRuleBuilder.newBuilder().cidr(CidrImpl.ALL).fromPort(20).toPort(100)
            .ipProtocol(IpProtocol.TCP).build();

    final SecurityGroup securityGroup =
        SecurityGroupBuilder.newBuilder().id("432794732").location(null)
            .providerId("providerId").name("name").addSecurityGroupRule(SecurityGroupRules.ALL)
            .addSecurityGroupRules(new HashSet<SecurityGroupRule>() {{
              add(all);
              add(tcp);
            }}).build();

    final Set<SecurityGroupRule> rules = securityGroup.rules();
    assertThat(rules, hasItem(SecurityGroupRules.ALL));
    assertThat(rules, hasItem(all));
    assertThat(rules, hasItem(tcp));
  }
}
