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

package de.uniulm.omi.cloudiator.sword.drivers.jclouds.converters;

import static com.google.common.base.Preconditions.checkState;

import de.uniulm.omi.cloudiator.sword.domain.CidrImpl;
import de.uniulm.omi.cloudiator.sword.domain.IpProtocol;
import de.uniulm.omi.cloudiator.sword.domain.SecurityGroupRule;
import de.uniulm.omi.cloudiator.sword.domain.SecurityGroupRuleBuilder;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import org.jclouds.net.domain.IpPermission;

/**
 * Created by daniel on 01.07.16.
 */
public class JCloudsIpPermissionToSecurityGroupRule
    implements OneWayConverter<IpPermission, SecurityGroupRule> {

  private final OneWayConverter<org.jclouds.net.domain.IpProtocol, IpProtocol>
      ipProtocolConverter;

  public JCloudsIpPermissionToSecurityGroupRule() {
    ipProtocolConverter = new JCloudsIpProtocolToIpProtocol();
  }

  @Override
  public SecurityGroupRule apply(IpPermission ipPermission) {

    SecurityGroupRuleBuilder securityGroupRuleBuilder = SecurityGroupRuleBuilder.newBuilder()
        .ipProtocol(ipProtocolConverter.apply(ipPermission.getIpProtocol()))
        .fromPort(ipPermission.getFromPort()).toPort(ipPermission.getToPort());

    //todo: we only want to support cidr, can we ensure this?
    checkState(ipPermission.getCidrBlocks().size() == 1);
    securityGroupRuleBuilder.cidr(CidrImpl.of(ipPermission.getCidrBlocks().iterator().next()));
    checkState(ipPermission.getExclusionCidrBlocks().size() == 0);

    return securityGroupRuleBuilder.build();
  }

  private static class JCloudsIpProtocolToIpProtocol
      implements OneWayConverter<org.jclouds.net.domain.IpProtocol, IpProtocol> {

    @Override
    public IpProtocol apply(org.jclouds.net.domain.IpProtocol ipProtocol) {
      switch (ipProtocol) {
        case TCP:
          return IpProtocol.TCP;
        case ICMP:
          return IpProtocol.ICMP;
        case UDP:
          return IpProtocol.UDP;
        case ALL:
          return IpProtocol.ALL;
        case UNRECOGNIZED:
          throw new IllegalStateException("ip protocol is unrecognized.");
        default:
          throw new AssertionError(String.format("unknown ip protocol %s", ipProtocol));
      }
    }
  }

}
