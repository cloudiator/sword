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

import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.core.util.IdScopeByLocations;
import org.openstack4j.model.common.Link;
import org.openstack4j.model.compute.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 18.11.16.
 */
public class ServerInRegion implements Server, InRegion, ProviderIdentified {

    private final Server delegate;
    private final Location region;
    private final String regionScopedId;

    public ServerInRegion(Server delegate, Location region) {
        checkNotNull(delegate, "delegate is null.");
        checkNotNull(region, "region is null.");
        this.delegate = delegate;
        this.region = region;
        regionScopedId = IdScopeByLocations.from(region.id(), delegate.getId()).getIdWithLocation();
    }

    @Override public String getId() {
        return regionScopedId;
    }

    @Override public String getName() {
        return delegate.getName();
    }

    @Override public Addresses getAddresses() {
        return delegate.getAddresses();
    }

    @Override public List<? extends Link> getLinks() {
        return delegate.getLinks();
    }

    @Override public String getImageId() {
        return delegate.getImageId();
    }

    @Override public Image getImage() {
        return delegate.getImage();
    }

    @Override public String getFlavorId() {
        return delegate.getFlavorId();
    }

    @Override public Flavor getFlavor() {
        return delegate.getFlavor();
    }

    @Override public String getAccessIPv4() {
        return delegate.getAccessIPv4();
    }

    @Override public String getAccessIPv6() {
        return delegate.getAccessIPv6();
    }

    @Override public String getConfigDrive() {
        return delegate.getConfigDrive();
    }

    @Override public Status getStatus() {
        return delegate.getStatus();
    }

    @Override public int getProgress() {
        return delegate.getProgress();
    }

    @Override public Fault getFault() {
        return delegate.getFault();
    }

    @Override public String getTenantId() {
        return delegate.getTenantId();
    }

    @Override public String getUserId() {
        return delegate.getUserId();
    }

    @Override public String getKeyName() {
        return delegate.getKeyName();
    }

    @Override public String getHostId() {
        return delegate.getHostId();
    }

    @Override public Date getUpdated() {
        return delegate.getUpdated();
    }

    @Override public Date getCreated() {
        return delegate.getCreated();
    }

    @Override public Map<String, String> getMetadata() {
        return delegate.getMetadata();
    }

    @Override public String getTaskState() {
        return delegate.getTaskState();
    }

    @Override public String getPowerState() {
        return delegate.getPowerState();
    }

    @Override public String getVmState() {
        return delegate.getVmState();
    }

    @Override public String getHost() {
        return delegate.getHost();
    }

    @Override public String getInstanceName() {
        return delegate.getInstanceName();
    }

    @Override public String getHypervisorHostname() {
        return delegate.getHypervisorHostname();
    }

    @Override public DiskConfig getDiskConfig() {
        return delegate.getDiskConfig();
    }

    @Override public String getAvailabilityZone() {
        return delegate.getAvailabilityZone();
    }

    @Override public Date getLaunchedAt() {
        return delegate.getLaunchedAt();
    }

    @Override public Date getTerminatedAt() {
        return delegate.getTerminatedAt();
    }

    @Override public List<String> getOsExtendedVolumesAttached() {
        return delegate.getOsExtendedVolumesAttached();
    }

    @Override public String getUuid() {
        return delegate.getUuid();
    }

    @Override public String getAdminPass() {
        return delegate.getAdminPass();
    }

    @Override public List<? extends SecurityGroup> getSecurityGroups() {
        return delegate.getSecurityGroups();
    }

    @Override public String providerId() {
        return delegate.getId();
    }

    @Override public Location region() {
        return region;
    }
}
