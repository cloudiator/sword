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

package de.uniulm.omi.cloudiator.sword.drivers.jclouds.converters;

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.SecurityGroup;
import de.uniulm.omi.cloudiator.sword.api.domain.SecurityGroupRule;
import de.uniulm.omi.cloudiator.sword.core.domain.SecurityGroupBuilder;
import org.jclouds.net.domain.IpPermission;

import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 01.07.16.
 */
public class JCloudsSecurityGroupToSecurityGroup
    implements OneWayConverter<org.jclouds.compute.domain.SecurityGroup, SecurityGroup> {

    private final OneWayConverter<org.jclouds.domain.Location, Location> locationConverter;
    private final OneWayConverter<IpPermission, SecurityGroupRule> securityGroupRuleConverter;

    @Inject public JCloudsSecurityGroupToSecurityGroup(
        OneWayConverter<org.jclouds.domain.Location, Location> locationConverter,
        OneWayConverter<IpPermission, SecurityGroupRule> securityGroupRuleConverter) {
        checkNotNull(securityGroupRuleConverter);
        this.securityGroupRuleConverter = securityGroupRuleConverter;
        checkNotNull(locationConverter);
        this.locationConverter = locationConverter;
    }

    @Override public SecurityGroup apply(org.jclouds.compute.domain.SecurityGroup securityGroup) {

        final SecurityGroupBuilder securityGroupBuilder =
            SecurityGroupBuilder.newBuilder().id(securityGroup.getId())
                .providerId(securityGroup.getProviderId())
                .location(locationConverter.apply(securityGroup.getLocation()))
                .name(securityGroup.getName()).addSecurityGroupRules(
                securityGroup.getIpPermissions().stream().map(securityGroupRuleConverter::apply)
                    .collect(Collectors.toSet()));
        return securityGroupBuilder.build();
    }
}
