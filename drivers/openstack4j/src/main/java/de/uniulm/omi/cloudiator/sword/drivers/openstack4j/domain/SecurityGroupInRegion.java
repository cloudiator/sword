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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.domain;

import com.google.common.collect.ImmutableSet;
import de.uniulm.omi.cloudiator.domain.Identifiable;
import de.uniulm.omi.cloudiator.domain.Location;
import de.uniulm.omi.cloudiator.sword.core.util.IdScopeByLocations;
import org.openstack4j.model.compute.SecGroupExtension;
import org.openstack4j.openstack.compute.domain.NovaSecGroupExtension;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 29.11.16.
 */
public class SecurityGroupInRegion implements InRegion, ProviderIdentified, Identifiable {

    private final SecGroupExtension delegate;
    private final Location region;
    private final String regionScopedId;
    private final Set<SecGroupExtension.Rule> rules;

    public SecurityGroupInRegion(SecGroupExtension original, Location region,
        Set<NovaSecGroupExtension.Rule> rules) {

        checkNotNull(original, "original is null.");
        checkNotNull(region, "region is null");
        checkNotNull(rules, "rules are null");
        delegate = original;
        this.region = region;
        this.regionScopedId =
            IdScopeByLocations.from(region.id(), delegate.getId()).getIdWithLocation();
        this.rules = rules;
    }

    public String getName() {
        return delegate.getName();
    }

    @Override public String id() {
        return regionScopedId;
    }

    @Override public String providerId() {
        return delegate.getId();
    }

    @Override public Location region() {
        return region;
    }

    public Set<NovaSecGroupExtension.Rule> rules() {
        return ImmutableSet.copyOf(rules);
    }
}
