/*
 * Copyright (c) 2014-2017 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.multicloud.service;

import com.google.common.base.Optional;
import de.uniulm.omi.cloudiator.sword.domain.SecurityGroup;
import de.uniulm.omi.cloudiator.sword.domain.SecurityGroupRule;
import de.uniulm.omi.cloudiator.sword.extensions.SecurityGroupExtension;
import de.uniulm.omi.cloudiator.sword.multicloud.domain.SecurityGroupMultiCloudImpl;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Created by daniel on 25.01.17.
 */
public class MultiCloudSecurityGroupExtension implements SecurityGroupExtension {

    private final ComputeServiceProvider computeServiceProvider;

    public MultiCloudSecurityGroupExtension(ComputeServiceProvider computeServiceProvider) {
        checkNotNull(computeServiceProvider, "computeServiceProvider is null");
        this.computeServiceProvider = computeServiceProvider;
    }


    @Override public Set<SecurityGroup> listSecurityGroups() {
        return computeServiceProvider.all().entrySet().stream()
            .flatMap(cloudComputeServiceEntry -> {
                final Optional<SecurityGroupExtension> securityGroupExtensionOptional =
                    cloudComputeServiceEntry.getValue().securityGroupExtension();
                checkState(securityGroupExtensionOptional.isPresent(), String
                    .format("SecurityGroupExtension is not available for cloud %s",
                        cloudComputeServiceEntry.getKey()));
                return securityGroupExtensionOptional.get().listSecurityGroups().stream().map(
                    (Function<SecurityGroup, SecurityGroup>) securityGroup -> new SecurityGroupMultiCloudImpl(
                        securityGroup, cloudComputeServiceEntry.getKey().id()));
            }).collect(Collectors.toSet());
    }

    @Override public SecurityGroup createSecurityGroup(String name, String locationId) {
        final IdScopedByCloud scopedLocationId = IdScopedByClouds.from(locationId);
        final Optional<SecurityGroupExtension> securityGroupExtensionOptional =
            computeServiceProvider.forId(scopedLocationId.cloudId()).securityGroupExtension();
        checkState(securityGroupExtensionOptional.isPresent(), String
            .format("SecurityGroupExtension is not available for cloud %s",
                scopedLocationId.cloudId()));
        return new SecurityGroupMultiCloudImpl(
            securityGroupExtensionOptional.get().createSecurityGroup(name, scopedLocationId.id()),
            scopedLocationId.cloudId());

    }

    @Override public SecurityGroup addRule(SecurityGroupRule rule, String securityGroupId) {
        final IdScopedByCloud scopedGroupId = IdScopedByClouds.from(securityGroupId);
        final Optional<SecurityGroupExtension> securityGroupExtensionOptional =
            computeServiceProvider.forId(scopedGroupId.cloudId()).securityGroupExtension();
        checkState(securityGroupExtensionOptional.isPresent(), String
            .format("SecurityGroupExtension is not available for cloud %s",
                scopedGroupId.cloudId()));

        return new SecurityGroupMultiCloudImpl(
            securityGroupExtensionOptional.get().addRule(rule, scopedGroupId.id()),
            scopedGroupId.cloudId());
    }
}
