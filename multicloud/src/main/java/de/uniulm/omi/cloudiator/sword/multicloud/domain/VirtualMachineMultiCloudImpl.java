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
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.multicloud.service.IdScopedByClouds;

import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 19.01.17.
 */
public class VirtualMachineMultiCloudImpl implements VirtualMachine {

    private final VirtualMachine delegate;
    private final String cloudId;

    public VirtualMachineMultiCloudImpl(VirtualMachine delegate, String cloudId) {
        checkNotNull(delegate, "delegate is null");
        this.delegate = delegate;
        checkNotNull(cloudId, "cloudId is null");
        this.cloudId = cloudId;
    }

    @Override public String id() {
        return IdScopedByClouds.from(delegate.id(), cloudId).scopedId();
    }

    @Override public String providerId() {
        return delegate.providerId();
    }

    @Override public Optional<Location> location() {
        if (!delegate.location().isPresent()) {
            return delegate.location();
        }
        return Optional.of(new LocationMultiCloudImpl(delegate.location().get(), cloudId));
    }

    @Override public String name() {
        return delegate.name();
    }

    @Override public Set<String> publicAddresses() {
        return delegate.publicAddresses();
    }

    @Override public Set<String> privateAddresses() {
        return delegate.privateAddresses();
    }

    @Override public Optional<LoginCredential> loginCredential() {
        return delegate.loginCredential();
    }

    public String cloudId() {
        return cloudId;
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id()).add("delegate", delegate)
            .add("cloudId", cloudId).toString();
    }
}
