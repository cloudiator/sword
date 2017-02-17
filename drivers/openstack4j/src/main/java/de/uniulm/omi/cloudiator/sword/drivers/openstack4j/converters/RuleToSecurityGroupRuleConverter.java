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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.converters;

import de.uniulm.omi.cloudiator.util.OneWayConverter;
import de.uniulm.omi.cloudiator.domain.IpProtocol;
import de.uniulm.omi.cloudiator.domain.SecurityGroupRule;
import de.uniulm.omi.cloudiator.domain.CidrImpl;
import de.uniulm.omi.cloudiator.domain.SecurityGroupRuleBuilder;
import org.openstack4j.model.compute.IPProtocol;
import org.openstack4j.model.compute.SecGroupExtension;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 30.11.16.
 */
public class RuleToSecurityGroupRuleConverter
    implements OneWayConverter<SecGroupExtension.Rule, SecurityGroupRule> {

    private final OneWayConverter<IPProtocol, IpProtocol> ipProtocolConverter =
        new IpProtocolConverter();

    @Override public SecurityGroupRule apply(SecGroupExtension.Rule rule) {
        return SecurityGroupRuleBuilder.newBuilder().cidr(CidrImpl.of(rule.getRange().getCidr()))
            .ipProtocol(ipProtocolConverter.apply(rule.getIPProtocol()))
            .fromPort(rule.getFromPort()).toPort(rule.getToPort()).build();
    }

    private static final class IpProtocolConverter
        implements OneWayConverter<IPProtocol, IpProtocol> {

        @Override public IpProtocol apply(IPProtocol ipProtocol) {
            checkNotNull(ipProtocol, "ipProtocol is null.");
            switch (ipProtocol) {
                case ICMP:
                    return IpProtocol.ICMP;
                case TCP:
                    return IpProtocol.TCP;
                case UDP:
                    return IpProtocol.UDP;
                default:
                    throw new IllegalStateException(ipProtocol + "is unknown.");
            }
        }
    }

}
