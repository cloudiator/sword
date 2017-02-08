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

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.domain.SecurityGroup;
import de.uniulm.omi.cloudiator.domain.SecurityGroupRule;
import de.uniulm.omi.cloudiator.domain.SecurityGroupBuilder;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.domain.SecurityGroupInRegion;
import org.openstack4j.model.compute.SecGroupExtension;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 29.11.16.
 */
public class SecurityGroupInRegionToSecurityGroup
    implements OneWayConverter<SecurityGroupInRegion, SecurityGroup> {

    private final OneWayConverter<SecGroupExtension.Rule, SecurityGroupRule>
        securityGroupRuleConverter;

    @Inject public SecurityGroupInRegionToSecurityGroup(
        OneWayConverter<SecGroupExtension.Rule, SecurityGroupRule> securityGroupRuleConverter) {
        checkNotNull(securityGroupRuleConverter, "securityGroupRuleConverter is null");
        this.securityGroupRuleConverter = securityGroupRuleConverter;
    }

    @Override public SecurityGroup apply(SecurityGroupInRegion securityGroupInRegion) {
        final SecurityGroupBuilder securityGroupBuilder =
            SecurityGroupBuilder.newBuilder().id(securityGroupInRegion.id())
                .location(securityGroupInRegion.region()).name(securityGroupInRegion.getName())
                .providerId(securityGroupInRegion.providerId());
        securityGroupInRegion.rules().stream().map(securityGroupRuleConverter)
            .forEach(securityGroupBuilder::addSecurityGroupRule);
        return securityGroupBuilder.build();
    }
}
