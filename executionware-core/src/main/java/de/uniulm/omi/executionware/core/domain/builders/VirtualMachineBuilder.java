/*
 * Copyright (c) 2014 University of Ulm
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

package de.uniulm.omi.executionware.core.domain.builders;

import com.google.common.base.Optional;
import de.uniulm.omi.executionware.api.domain.LoginCredential;
import de.uniulm.omi.executionware.api.domain.VirtualMachine;
import de.uniulm.omi.executionware.core.domain.impl.VirtualMachineImpl;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by daniel on 09.12.14.
 */
public class VirtualMachineBuilder extends DescribedResourceBuilder {

    private final Set<String> publicIpAddresses;
    private final Set<String> privateIpAddresses;
    @Nullable
    private LoginCredential loginCredential;

    private VirtualMachineBuilder() {
        publicIpAddresses = new HashSet<>();
        privateIpAddresses = new HashSet<>();
    }

    public static VirtualMachineBuilder newBuilder() {
        return new VirtualMachineBuilder();
    }

    @Override
    public VirtualMachineBuilder id(final String id) {
        super.id(id);
        return this;
    }

    @Override
    public VirtualMachineBuilder description(final String description) {
        super.description(description);
        return this;
    }

    public VirtualMachineBuilder addPublicIpAddress(String publicIpAddress) {
        this.publicIpAddresses.add(publicIpAddress);
        return this;
    }

    public VirtualMachineBuilder addPrivateIpAddress(String privateIpAddress) {
        this.privateIpAddresses.add(privateIpAddress);
        return this;
    }

    public VirtualMachineBuilder loginCredential(final LoginCredential loginCredential) {
        this.loginCredential = loginCredential;
        return this;
    }

    @Override
    public VirtualMachine build() {
        return new VirtualMachineImpl(this.id, this.description, publicIpAddresses, privateIpAddresses, Optional.fromNullable(loginCredential));
    }
}
