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

import com.google.common.collect.Sets;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 09.12.14.
 */
public class VirtualMachineBuilder {

    private Set<String> publicIpAddresses = new HashSet<>();
    private Set<String> privateIpAddresses = new HashSet<>();
    @Nullable private String id;
    private String providerId;
    @Nullable private String name;
    @Nullable private Location location;
    @Nullable private LoginCredential loginCredential;

    private VirtualMachineBuilder() {

    }

    private VirtualMachineBuilder(VirtualMachine virtualMachine) {
        publicIpAddresses = Sets.newHashSet(virtualMachine.publicAddresses());
        privateIpAddresses = Sets.newHashSet(virtualMachine.privateAddresses());
        id = virtualMachine.id();
        providerId = virtualMachine.providerId();
        name = virtualMachine.name();
        location = virtualMachine.location().orElse(null);
        loginCredential = virtualMachine.loginCredential().orElse(null);
    }

    public static VirtualMachineBuilder newBuilder() {
        return new VirtualMachineBuilder();
    }

    public static VirtualMachineBuilder of(VirtualMachine virtualMachine) {
        checkNotNull(virtualMachine, "virtualMachine is null");
        return new VirtualMachineBuilder(virtualMachine);
    }

    public VirtualMachineBuilder id(String id) {
        this.id = id;
        return this;
    }

    public VirtualMachineBuilder providerId(String providerId) {
        this.providerId = providerId;
        return this;
    }

    public VirtualMachineBuilder name(String name) {
        this.name = name;
        return this;
    }

    public VirtualMachineBuilder location(Location location) {
        this.location = location;
        return this;
    }

    public VirtualMachineBuilder addPublicIpAddress(String publicIpAddress) {
        this.publicIpAddresses.add(publicIpAddress);
        return this;
    }

    public VirtualMachineBuilder addPublicIpAddresses(Collection<String> publicIpAddresses) {
        this.publicIpAddresses.addAll(publicIpAddresses);
        return this;
    }

    public VirtualMachineBuilder addPrivateIpAddress(String privateIpAddress) {
        this.privateIpAddresses.add(privateIpAddress);
        return this;
    }

    public VirtualMachineBuilder addPrivateIpAddresses(Collection<String> privateIpAddresses) {
        this.privateIpAddresses.addAll(privateIpAddresses);
        return this;
    }

    public VirtualMachineBuilder loginCredential(final LoginCredential loginCredential) {
        this.loginCredential = loginCredential;
        return this;
    }

    public VirtualMachine build() {
        return new VirtualMachineImpl(id, providerId, name, location, publicIpAddresses,
            privateIpAddresses, loginCredential);
    }
}
