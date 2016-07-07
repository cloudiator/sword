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

package de.uniulm.omi.cloudiator.sword.drivers.jclouds;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.sword.api.ServiceConfiguration;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.SecurityGroup;
import de.uniulm.omi.cloudiator.sword.api.domain.SecurityGroupRule;
import de.uniulm.omi.cloudiator.sword.api.extensions.SecurityGroupService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.extensions.SecurityGroupExtension;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.*;

/**
 * Created by daniel on 01.07.16.
 */
public class JCloudsSecurityGroupService implements SecurityGroupService {

    private final SecurityGroupExtension securityGroupExtension;
    private final ServiceConfiguration serviceConfiguration;
    private final OneWayConverter<org.jclouds.compute.domain.SecurityGroup, SecurityGroup>
        securityGroupConverter;
    private final OneWayConverter<Location, org.jclouds.domain.Location> locationConverter;

    @Inject public JCloudsSecurityGroupService(JCloudsViewFactory jCloudsViewFactory,
        ServiceConfiguration serviceConfiguration,
        OneWayConverter<org.jclouds.compute.domain.SecurityGroup, SecurityGroup> securityGroupConverter,
        OneWayConverter<Location, org.jclouds.domain.Location> locationConverter) {

        checkNotNull(locationConverter);
        this.locationConverter = locationConverter;

        checkNotNull(securityGroupConverter);
        this.securityGroupConverter = securityGroupConverter;

        checkNotNull(jCloudsViewFactory);
        checkNotNull(serviceConfiguration);

        final ComputeServiceContext computeServiceContext =
            jCloudsViewFactory.buildJCloudsView(ComputeServiceContext.class);
        final Optional<SecurityGroupExtension> optional =
            computeServiceContext.getComputeService().getSecurityGroupExtension();
        checkState(optional.isPresent(), "security group extension not present.");

        this.securityGroupExtension = optional.get();
        this.serviceConfiguration = serviceConfiguration;

    }

    @Override public Set<SecurityGroup> listSecurityGroups() {
        return securityGroupExtension.listSecurityGroups().stream()
            .filter(securityGroup -> securityGroup.getName().startsWith("jclouds-"))
            .map(securityGroupConverter).collect(Collectors.toSet());

    }

    @Override public SecurityGroup createSecurityGroup(String name, Location location) {
        checkNotNull(name);
        checkArgument(!name.isEmpty());
        checkNotNull(location);

        org.jclouds.domain.Location jcloudsLocation = locationConverter.apply(location);
        return securityGroupConverter
            .apply(this.securityGroupExtension.createSecurityGroup(name, jcloudsLocation));
    }

    @Override public SecurityGroup addRule(SecurityGroupRule rule, SecurityGroup securityGroup) {
        checkNotNull(rule);
        checkNotNull(securityGroup);

        org.jclouds.net.domain.IpProtocol ipProtocol;
        switch (rule.ipProtocol()) {
            case ALL:
                ipProtocol = org.jclouds.net.domain.IpProtocol.ALL;
                break;
            case ICMP:
                ipProtocol = org.jclouds.net.domain.IpProtocol.ICMP;
                break;
            case TCP:
                ipProtocol = org.jclouds.net.domain.IpProtocol.TCP;
                break;
            case UDP:
                ipProtocol = org.jclouds.net.domain.IpProtocol.UDP;
                break;
            default:
                throw new AssertionError("unknown ipProtocol" + rule.ipProtocol());
        }
        org.jclouds.compute.domain.SecurityGroup jcloudsSecurityGroup =
            securityGroupExtension.getSecurityGroupById(securityGroup.id());

        return securityGroupConverter.apply(securityGroupExtension
            .addIpPermission(ipProtocol, rule.fromPort(), rule.toPort(), null,
                Collections.singleton(rule.cidr().toString()), null, jcloudsSecurityGroup));
    }


}
