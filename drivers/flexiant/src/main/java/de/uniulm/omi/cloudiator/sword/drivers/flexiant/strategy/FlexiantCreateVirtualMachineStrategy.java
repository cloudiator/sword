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

package de.uniulm.omi.cloudiator.sword.drivers.flexiant.strategy;

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import de.uniulm.omi.cloudiator.flexiant.client.domain.Server;
import de.uniulm.omi.cloudiator.flexiant.client.domain.ServerTemplate;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachineTemplate;
import de.uniulm.omi.cloudiator.sword.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.util.NamingStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.flexiant.FlexiantComputeClient;
import de.uniulm.omi.cloudiator.sword.drivers.flexiant.util.FlexiantUtil;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 12.01.15.
 */
public class FlexiantCreateVirtualMachineStrategy implements CreateVirtualMachineStrategy {

    private final FlexiantComputeClient flexiantComputeClient;
    private final OneWayConverter<Server, VirtualMachine> serverVirtualMachineConverter;
    private final NamingStrategy namingStrategy;

    @Inject public FlexiantCreateVirtualMachineStrategy(FlexiantComputeClient flexiantComputeClient,
        OneWayConverter<Server, VirtualMachine> serverVirtualMachineConverter,
        NamingStrategy namingStrategy) {

        checkNotNull(flexiantComputeClient);
        checkNotNull(serverVirtualMachineConverter);
        checkNotNull(namingStrategy);

        this.flexiantComputeClient = flexiantComputeClient;
        this.serverVirtualMachineConverter = serverVirtualMachineConverter;
        this.namingStrategy = namingStrategy;
    }

    @Override public VirtualMachine apply(VirtualMachineTemplate virtualMachineTemplate) {

        final ServerTemplate.FlexiantServerTemplateBuilder flexiantServerTemplateBuilder =
            new ServerTemplate.FlexiantServerTemplateBuilder();
        final ServerTemplate serverTemplate = flexiantServerTemplateBuilder
            .hardwareId(FlexiantUtil.stripLocation(virtualMachineTemplate.hardwareFlavorId()))
            .image(FlexiantUtil.stripLocation(virtualMachineTemplate.imageId()))
            .vdc(virtualMachineTemplate.locationId())
            .serverName(nameWithNodeGroup(virtualMachineTemplate.name())).build();

        return this.serverVirtualMachineConverter
            .apply(this.flexiantComputeClient.createServer(serverTemplate));
    }

    private String nameWithNodeGroup(String name) {
        return namingStrategy.generateUniqueNameBasedOnName(name);
    }


}
