/*
 * Copyright (c) 2014-2015 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.drivers.openstack.strategy;

import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.LocationScope;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachineTemplate;
import de.uniulm.omi.cloudiator.sword.api.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.api.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.sword.core.domain.VirtualMachineTemplateBuilder;

import javax.annotation.Nullable;

/**
 * Create virtual machine strategy for Openstack.
 *
 *
 */
public class OpenstackCreateVirtualMachineStrategy implements CreateVirtualMachineStrategy {

    private final CreateVirtualMachineStrategy delegate;
    private final GetStrategy<String, Location> locationGetStrategy;

    public OpenstackCreateVirtualMachineStrategy(CreateVirtualMachineStrategy delegate,
        GetStrategy<String, Location> locationGetStrategy) {
        this.delegate = delegate;
        this.locationGetStrategy = locationGetStrategy;
    }

    @Nullable @Override
    public VirtualMachine apply(VirtualMachineTemplate originalVirtualMachineTemplate) {
        Location location = locationGetStrategy.get(originalVirtualMachineTemplate.locationId());
        VirtualMachineTemplate replacedTemplate = originalVirtualMachineTemplate;

        //our location is an availability zone
        if (location != null && location.locationScope().equals(LocationScope.ZONE) && location
            .parent().isPresent() && location.parent().get().locationScope()
            .equals(LocationScope.REGION)) {

            //replace it with the region...
            replacedTemplate = VirtualMachineTemplateBuilder.of(originalVirtualMachineTemplate)
                .location(location.parent().get().id()).build();
        }

        return delegate.apply(replacedTemplate);
    }
}
