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

import de.uniulm.omi.cloudiator.sword.core.util.IdScopeByLocations;
import org.openstack4j.model.common.Link;
import org.openstack4j.model.compute.Image;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 18.11.16.
 */
public class ImageInRegion implements Image, InRegion, ProviderIdentified {

    private final Image delegate;
    private final String region;
    private final String regionScopedId;

    public ImageInRegion(Image original, String region) {
        checkNotNull(original, "original is null.");
        checkNotNull(region, "region is null");
        checkArgument(!region.isEmpty(), "original is empty.");
        delegate = original;
        this.region = region;
        this.regionScopedId = IdScopeByLocations.from(region, delegate.getId()).getIdWithLocation();
    }

    @Override public String getId() {
        return regionScopedId;
    }

    @Override public String getName() {
        return delegate.getName();
    }

    @Override public long getSize() {
        return delegate.getSize();
    }

    @Override public int getMinDisk() {
        return delegate.getMinDisk();
    }

    @Override public int getMinRam() {
        return delegate.getMinRam();
    }

    @Override public int getProgress() {
        return delegate.getProgress();
    }

    @Override public Status getStatus() {
        return delegate.getStatus();
    }

    @Override public Date getCreated() {
        return delegate.getCreated();
    }

    @Override public Date getUpdated() {
        return delegate.getUpdated();
    }

    @Override public List<? extends Link> getLinks() {
        return delegate.getLinks();
    }

    @Override public Map<String, Object> getMetaData() {
        return delegate.getMetaData();
    }

    @Override public boolean isSnapshot() {
        return delegate.isSnapshot();
    }

    @Override public String region() {
        return region;
    }

    @Override public String providerId() {
        return delegate.getId();
    }
}
