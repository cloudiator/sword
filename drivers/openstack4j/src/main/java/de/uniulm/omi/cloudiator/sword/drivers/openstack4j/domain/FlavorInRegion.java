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

import de.uniulm.omi.cloudiator.domain.Location;
import de.uniulm.omi.cloudiator.sword.core.util.IdScopeByLocations;
import org.openstack4j.model.common.Link;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.builder.FlavorBuilder;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 18.11.16.
 */
public class FlavorInRegion implements Flavor, InRegion, ProviderIdentified {

    private final Flavor delegate;
    private final Location region;
    private final String regionScopedId;

    public FlavorInRegion(Flavor delegate, Location region) {

        checkNotNull(delegate, "delegate is null");
        checkNotNull(region, "region is null");

        this.delegate = delegate;
        this.region = region;
        regionScopedId = IdScopeByLocations.from(region.id(), delegate.getId()).getIdWithLocation();
    }

    @Override public FlavorBuilder toBuilder() {
        return delegate.toBuilder();
    }

    @Override public String getId() {
        return regionScopedId;
    }

    @Override public String getName() {
        return delegate.getName();
    }

    @Override public int getRam() {
        return delegate.getRam();
    }

    @Override public int getVcpus() {
        return delegate.getVcpus();
    }

    @Override public int getDisk() {
        return delegate.getDisk();
    }

    @Override public int getSwap() {
        return delegate.getSwap();
    }

    @Override public float getRxtxFactor() {
        return delegate.getRxtxFactor();
    }

    @Override public int getEphemeral() {
        return delegate.getEphemeral();
    }

    @Override public int getRxtxQuota() {
        return delegate.getRxtxQuota();
    }

    @Override public int getRxtxCap() {
        return delegate.getRxtxCap();
    }

    @Override public Boolean isPublic() {
        return delegate.isPublic();
    }

    @Override public Boolean isDisabled() {
        return delegate.isDisabled();
    }

    @Override public List<? extends Link> getLinks() {
        return delegate.getLinks();
    }

    @Override public String providerId() {
        return delegate.getId();
    }

    @Override public Location region() {
        return region;
    }
}
