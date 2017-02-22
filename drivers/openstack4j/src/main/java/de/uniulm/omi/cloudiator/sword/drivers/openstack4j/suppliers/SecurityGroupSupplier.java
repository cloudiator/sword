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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.suppliers;

import com.google.common.base.Supplier;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import de.uniulm.omi.cloudiator.domain.Location;
import de.uniulm.omi.cloudiator.domain.SecurityGroup;
import de.uniulm.omi.cloudiator.sword.util.NamingStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.domain.SecurityGroupInRegion;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.internal.RegionSupplier;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.SecGroupExtension;
import org.openstack4j.openstack.compute.domain.NovaSecGroupExtension;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 30.11.16.
 */
public class SecurityGroupSupplier implements Supplier<Set<SecurityGroup>> {

    private final OSClient osClient;
    private final RegionSupplier regionSupplier;
    private final OneWayConverter<SecurityGroupInRegion, SecurityGroup> converter;
    private final NamingStrategy namingStrategy;

    @Inject public SecurityGroupSupplier(OSClient osClient, RegionSupplier regionSupplier,
        OneWayConverter<SecurityGroupInRegion, SecurityGroup> converter,
        NamingStrategy namingStrategy) {

        checkNotNull(osClient, "osClient is null");
        checkNotNull(regionSupplier, "regionSupplier is null");
        checkNotNull(converter, "converter is null");
        checkNotNull(namingStrategy, "namingStrategy is null");

        this.osClient = osClient;
        this.regionSupplier = regionSupplier;
        this.converter = converter;
        this.namingStrategy = namingStrategy;
    }

    @Override public Set<SecurityGroup> get() {

        Set<SecurityGroupInRegion> securityGroups = new HashSet<>();
        for (Location location : regionSupplier.get()) {
            Set<NovaSecGroupExtension.Rule> rules = new HashSet<>();
            osClient.useRegion(location.id()).compute().securityGroups().list().stream().filter(
                (Predicate<SecGroupExtension>) secGroupExtension -> namingStrategy.belongsToNamingGroup()
                    .test(secGroupExtension.getName()))
                .forEach((Consumer<SecGroupExtension>) secGroupExtension -> {
                    rules.addAll(secGroupExtension.getRules());
                    securityGroups
                        .add(new SecurityGroupInRegion(secGroupExtension, location, rules));
                });
        }
        return securityGroups.stream().map(converter).collect(Collectors.toSet());
    }
}
