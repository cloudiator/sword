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

import com.google.common.base.Optional;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.api.domain.*;
import de.uniulm.omi.cloudiator.sword.api.extensions.SecurityGroupService;
import de.uniulm.omi.cloudiator.sword.api.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.sword.api.util.NamingStrategy;
import de.uniulm.omi.cloudiator.sword.core.domain.CidrImpl;
import de.uniulm.omi.cloudiator.sword.core.domain.SecurityGroupRuleBuilder;

import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Created by daniel on 30.11.16.
 */
public class CreateSecurityGroupFromTemplateOption {

    public static final String DEFAULT_SEC_GROUP_NAME = "default";
    private final SecurityGroupService securityGroupService;
    private final NamingStrategy namingStrategy;
    private final GetStrategy<String, Location> locationGetStrategy;

    @Inject public CreateSecurityGroupFromTemplateOption(
        Optional<SecurityGroupService> securityGroupService, NamingStrategy namingStrategy,
        GetStrategy<String, Location> locationGetStrategy) {
        checkNotNull(locationGetStrategy, "locationGetStrategy is null");
        this.locationGetStrategy = locationGetStrategy;
        checkNotNull(namingStrategy, "namingStrategy is null");
        this.namingStrategy = namingStrategy;
        checkNotNull(securityGroupService, "securityGroupService is null");
        checkState(securityGroupService.isPresent(), "securityGroupService needs to be present.");
        this.securityGroupService = securityGroupService.get();
    }

    public String create(TemplateOptions templateOptions, String locationId) {
        checkNotNull(templateOptions, "templateOptions is null");
        checkNotNull(locationId, "locationId is null");
        Location location = locationGetStrategy.get(locationId);
        checkState(location != null, "Could not retrieve location with id " + locationId);

        //todo refactor this code
        while (location.parent().isPresent()) {
            location = location.parent().get();
        }
        checkState(location.locationScope().equals(LocationScope.REGION),
            "Could not find region parent of location" + location);
        final Location region = location;

        //check if already exists

        final Set<SecurityGroup> collect = securityGroupService.listSecurityGroups().stream()
            .filter(securityGroup -> securityGroup.name()
                .equals(namingStrategy.generateNameInGroup(DEFAULT_SEC_GROUP_NAME)))
            .filter(securityGroup -> {
                checkState(securityGroup.location().isPresent(),
                    String.format("SecurityGroup %s has no location.", securityGroup));
                return securityGroup.location().get().id().equals(region.id());
            }).collect(Collectors.toSet());
        if (collect.size() > 1) {
            throw new IllegalStateException(String.format(
                "Found %s security groups matching the name %s. Not sure what to use. Please delete unneeded ones.",
                collect.size(), namingStrategy.generateNameInGroup(DEFAULT_SEC_GROUP_NAME)));
        } else if (collect.size() == 1) {
            return collect.iterator().next().name();
        }

        //create
        SecurityGroup securityGroup =
            securityGroupService.createSecurityGroup(DEFAULT_SEC_GROUP_NAME, location);

        //add rules
        templateOptions.inboundPorts().forEach(integer -> {
            securityGroupService.addRule(
                SecurityGroupRuleBuilder.newBuilder().ipProtocol(IpProtocol.ALL).fromPort(integer)
                    .toPort(integer).cidr(CidrImpl.ALL).build(), securityGroup);
        });
        return securityGroup.name();
    }

}
