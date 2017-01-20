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

package de.uniulm.omi.cloudiator.sword.multicloud.domain;

import com.google.common.base.MoreObjects;
import de.uniulm.omi.cloudiator.sword.api.domain.Cloud;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.LocationScope;
import de.uniulm.omi.cloudiator.sword.multicloud.IdScopedByClouds;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 19.01.17.
 */
public class LocationMultiCloudImpl implements LocationMultiCloud {

    private final Location delegate;
    private final Cloud cloud;

    public LocationMultiCloudImpl(Location delegate, Cloud cloud) {
        checkNotNull(delegate, "delegate is null");
        this.delegate = delegate;
        checkNotNull(cloud, "cloud is null");
        this.cloud = cloud;
    }

    @Override public LocationScope locationScope() {
        return delegate.locationScope();
    }

    @Override public boolean isAssignable() {
        return delegate.isAssignable();
    }

    @Override public Optional<Location> parent() {
        if (!delegate.parent().isPresent()) {
            return delegate.parent();
        }
        return Optional.of(new LocationMultiCloudImpl(delegate.parent().get(), cloud));
    }

    @Override public String id() {
        return IdScopedByClouds.from(delegate.id(), cloud.id()).scopedId();
    }

    @Override public String providerId() {
        return delegate.providerId();
    }

    @Override public String name() {
        return delegate.name();
    }

    @Override public Cloud cloud() {
        return cloud;
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id()).add("delegate", delegate)
            .add("cloud", cloud).toString();
    }
}
