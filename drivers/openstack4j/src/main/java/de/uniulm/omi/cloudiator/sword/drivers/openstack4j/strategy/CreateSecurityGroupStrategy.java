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

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.LocationScope;
import de.uniulm.omi.cloudiator.sword.api.domain.SecurityGroup;
import de.uniulm.omi.cloudiator.sword.api.util.NamingStrategy;
import de.uniulm.omi.cloudiator.sword.core.util.LocationHierarchy;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.domain.SecurityGroupInRegion;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.SecGroupExtension;

import java.util.Collections;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 30.11.16.
 */
public class CreateSecurityGroupStrategy {

    private final OSClient osClient;
    private final NamingStrategy namingStrategy;
    private final OneWayConverter<SecurityGroupInRegion, SecurityGroup> securityGroupConverter;

    @Inject public CreateSecurityGroupStrategy(OSClient osClient, NamingStrategy namingStrategy,
        OneWayConverter<SecurityGroupInRegion, SecurityGroup> securityGroupConverter) {
        checkNotNull(securityGroupConverter, "securityGroupConverter is null");
        this.securityGroupConverter = securityGroupConverter;
        checkNotNull(namingStrategy, "namingStrategy is null");
        this.namingStrategy = namingStrategy;
        checkNotNull(osClient, "osClient is null");
        this.osClient = osClient;
    }

    public SecurityGroup create(final String name, final Location location) {
        checkNotNull(name, "name is null");
        checkArgument(!name.isEmpty(), "name is empty");
        checkNotNull(location, "location is empty");

        Location region =
            LocationHierarchy.of(location).firstParentLocationWithScope(LocationScope.REGION)
                .orElseThrow(() -> new IllegalStateException(
                    String.format("Could not find parent region of location %s", location)));

        final SecGroupExtension secGroupExtension =
            osClient.useRegion(region.id()).compute().securityGroups()
                .create(namingStrategy.generateNameBasedOnName(name), name);
        return securityGroupConverter
            .apply(new SecurityGroupInRegion(secGroupExtension, location, Collections.emptySet()));
    }
}
