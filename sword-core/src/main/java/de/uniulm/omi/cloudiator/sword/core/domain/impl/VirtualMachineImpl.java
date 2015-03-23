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

package de.uniulm.omi.cloudiator.sword.core.domain.impl;

import com.google.common.base.Optional;
import de.uniulm.omi.cloudiator.sword.api.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 09.12.14.
 */
public class VirtualMachineImpl extends ResourceImpl implements VirtualMachine {

    //TODO: make immutable
    private final Set<String> publicIpAddresses;

    //TODO: make immutable
    private final Set<String> privateIpAddresses;

    private final Optional<LoginCredential> loginCredential;

    public VirtualMachineImpl(String id, String name, Set<String> publicIpAddresses, Set<String> privateIpAddresses, Optional<LoginCredential> loginCredential) {
        super(id, name);


        checkNotNull(publicIpAddresses);
        checkNotNull(privateIpAddresses);
        checkNotNull(loginCredential);

        this.publicIpAddresses = publicIpAddresses;
        this.privateIpAddresses = privateIpAddresses;
        this.loginCredential = loginCredential;
    }

    @Override
    public String toString() {
        return String.format(
                "VirtualMachine(id: %s, description: %s)",
                this.id, this.name);
    }

    @Override
    public Set<String> publicAddresses() {
        return this.publicIpAddresses;
    }

    @Override
    public Set<String> privateAddresses() {
        return this.privateIpAddresses;
    }

    @Override
    public Optional<LoginCredential> loginCredential() {
        return this.loginCredential;
    }
}
