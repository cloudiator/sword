/*
 * Copyright (c) 2015 University of Ulm
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

package de.uniulm.omi.executionware.drivers.flexiant.strategy;

import com.google.inject.Inject;
import de.uniulm.omi.executionware.api.ServiceConfiguration;
import de.uniulm.omi.executionware.api.converters.Converter;
import de.uniulm.omi.executionware.api.domain.VirtualMachine;
import de.uniulm.omi.executionware.api.domain.VirtualMachineTemplate;
import de.uniulm.omi.executionware.api.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.executionware.drivers.flexiant.FlexiantComputeClientApi;
import de.uniulm.omi.flexiant.domain.impl.Server;
import de.uniulm.omi.flexiant.domain.impl.ServerTemplate;

import java.util.Random;

/**
 * Created by daniel on 12.01.15.
 */
public class FlexiantCreateVirtualMachineStrategy implements CreateVirtualMachineStrategy {

    private final FlexiantComputeClientApi flexiantComputeClient;
    private final static Random random = new Random();
    private final ServiceConfiguration serviceConfiguration;
    private final Converter<Server, VirtualMachine> serverVirtualMachineConverter;

    @Inject
    public FlexiantCreateVirtualMachineStrategy(FlexiantComputeClientApi flexiantComputeClient, ServiceConfiguration serviceConfiguration, Converter<Server, VirtualMachine> serverVirtualMachineConverter) {
        this.flexiantComputeClient = flexiantComputeClient;
        this.serviceConfiguration = serviceConfiguration;
        this.serverVirtualMachineConverter = serverVirtualMachineConverter;
    }

    @Override
    public VirtualMachine apply(VirtualMachineTemplate virtualMachineTemplate) {

        final ServerTemplate.FlexiantServerTemplateBuilder flexiantServerTemplateBuilder = new ServerTemplate.FlexiantServerTemplateBuilder();
        final ServerTemplate serverTemplate = flexiantServerTemplateBuilder
                .hardwareId(virtualMachineTemplate.getHardwareFlavorId())
                .image(virtualMachineTemplate.getImageId())
                .vdc(virtualMachineTemplate.getLocationId())
                .serverName(generateRandomNameWithNodeGroup())
                .build();

        return this.serverVirtualMachineConverter.apply(this.flexiantComputeClient.createServer(serverTemplate));
    }

    protected String generateRandomNameWithNodeGroup() {
        String name = String.valueOf(FlexiantCreateVirtualMachineStrategy.random.nextInt(1000));
        return serviceConfiguration.getNodeGroup() + "-" + name;
    }
}
