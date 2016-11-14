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

package de.uniulm.omi.cloudiator.sword.core.strategy;

import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachineTemplate;
import de.uniulm.omi.cloudiator.sword.api.strategy.CreateVirtualMachineStrategy;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * Created by daniel on 14.11.16.
 */
public class FakeCreateVirtualMachineStrategy implements CreateVirtualMachineStrategy {

    @Nullable @Override public VirtualMachine apply(@Nullable VirtualMachineTemplate input) {
        System.out.println(String.format("Faking creation of virtual machine %s.", input));
        return new FakeVirtualMachine();
    }

    private static class FakeVirtualMachine implements VirtualMachine {

        @Override public String name() {
            return "Fake";
        }

        @Override public String id() {
            return "fakeId";
        }

        @Override public String providerId() {
            return "fakeProviderId";
        }

        @Override public Optional<Location> location() {
            return Optional.empty();
        }

        @Override public Set<String> publicAddresses() {
            return Collections.emptySet();
        }

        @Override public Set<String> privateAddresses() {
            return Collections.emptySet();
        }

        @Override public Optional<LoginCredential> loginCredential() {
            return Optional.empty();
        }
    }
}
