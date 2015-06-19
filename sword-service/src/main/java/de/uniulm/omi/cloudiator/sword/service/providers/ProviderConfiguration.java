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

package de.uniulm.omi.cloudiator.sword.service.providers;

import com.google.inject.Module;
import de.uniulm.omi.cloudiator.sword.api.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.api.domain.Image;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.api.service.ComputeService;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 02.12.14.
 */
public class ProviderConfiguration {

    private final String name;
    private final Set<? extends Module> modules;
    private final Class<? extends ComputeService<HardwareFlavor, Image, Location, VirtualMachine>>
        computeService;

    public ProviderConfiguration(String name, Set<? extends Module> modules,
        Class<? extends ComputeService<HardwareFlavor, Image, Location, VirtualMachine>> computeService) {
        checkNotNull(name);
        checkNotNull(modules);
        checkNotNull(computeService);
        this.name = name;
        this.modules = modules;
        this.computeService = computeService;
    }

    public String getName() {
        return name;
    }

    public Set<? extends Module> getModules() {
        return modules;
    }

    public Class<? extends ComputeService<HardwareFlavor, Image, Location, VirtualMachine>> getComputeService() {
        return computeService;
    }
}
