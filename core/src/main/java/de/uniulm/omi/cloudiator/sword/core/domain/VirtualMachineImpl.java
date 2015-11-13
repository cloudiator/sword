/*
 * Copyright (c) 2014-2015 University of Ulm
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.uniulm.omi.cloudiator.sword.core.domain;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Basic implementation of the {@link VirtualMachine} interface.
 */
public class VirtualMachineImpl extends ResourceImpl implements VirtualMachine {

    private final Set<String> publicIpAddresses;
    private final Set<String> privateIpAddresses;

    @Nullable private final LoginCredential loginCredential;

    VirtualMachineImpl(String id, String name, @Nullable Location location,
        Set<String> publicIpAddresses, Set<String> privateIpAddresses,
        @Nullable LoginCredential loginCredential) {
        super(id, name, location);

        checkNotNull(publicIpAddresses);
        checkNotNull(privateIpAddresses);

        this.publicIpAddresses = ImmutableSet.copyOf(publicIpAddresses);
        this.privateIpAddresses = ImmutableSet.copyOf(privateIpAddresses);
        this.loginCredential = loginCredential;
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id()).add("name", name()).toString();
    }

    @Override public Set<String> publicAddresses() {
        return publicIpAddresses;
    }

    @Override public Set<String> privateAddresses() {
        return privateIpAddresses;
    }

    @Override public Optional<LoginCredential> loginCredential() {
        return Optional.ofNullable(loginCredential);
    }
}
