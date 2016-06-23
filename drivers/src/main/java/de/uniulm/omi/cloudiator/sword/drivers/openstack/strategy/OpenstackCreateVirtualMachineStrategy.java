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

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.sword.api.ServiceConfiguration;
import de.uniulm.omi.cloudiator.sword.api.domain.*;
import de.uniulm.omi.cloudiator.sword.api.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.sword.core.domain.VirtualMachineTemplateBuilder;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.JCloudsComputeClient;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.strategy.JCloudsCreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.OpenstackConstants;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.openstack.nova.v2_0.compute.options.NovaTemplateOptions;
import org.jclouds.openstack.nova.v2_0.domain.regionscoped.RegionAndId;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * Create virtual machine strategy for Openstack.
 * <p/>
 * Uses the extension points of {@link JCloudsCreateVirtualMachineStrategy} to
 * support availability zones and hosts.
 * <p/>
 * For this purpose it modifies the virtual machine template by setting the region of the availability zone/host
 * so that jclouds validation does not fail.
 * <p/>
 * Afterwards it modifies the template options object and manually sets the availability zone there.
 */
public class OpenstackCreateVirtualMachineStrategy extends JCloudsCreateVirtualMachineStrategy {

    private final GetStrategy<String, Location> locationGetStrategy;
    @Inject(optional = true) @Named(OpenstackConstants.FLOATING_IP_POOL_PROPERTY) private String
        floatingIpPool = null;

    @Inject public OpenstackCreateVirtualMachineStrategy(JCloudsComputeClient jCloudsComputeClient,
        OneWayConverter<ComputeMetadata, VirtualMachine> computeMetadataVirtualMachineConverter,
        OneWayConverter<TemplateOptions, org.jclouds.compute.options.TemplateOptions> templateOptionsConverter,
        GetStrategy<String, Location> locationGetStrategy,
        ServiceConfiguration serviceConfiguration) {
        super(jCloudsComputeClient, computeMetadataVirtualMachineConverter,
            templateOptionsConverter, serviceConfiguration);
        this.locationGetStrategy = locationGetStrategy;
    }

    @Override protected VirtualMachineTemplate modifyVirtualMachineTemplate(
        VirtualMachineTemplate originalMachineTemplate) {
        Location location = locationGetStrategy.get(originalMachineTemplate.locationId());
        VirtualMachineTemplate replacedTemplate = originalMachineTemplate;

        //our location is an availability zone
        if (location != null && location.locationScope().equals(LocationScope.ZONE)) {

            checkState(location.parent().isPresent());
            checkState(location.parent().get().locationScope().equals(LocationScope.REGION));

            //replace it with the region...
            replacedTemplate = VirtualMachineTemplateBuilder.of(originalMachineTemplate)
                .location(location.parent().get().id()).build();
        }

        //our location is a host
        if (location != null && location.locationScope().equals(LocationScope.HOST)) {

            checkState(location.parent().isPresent());
            checkState(location.parent().get().locationScope().equals(LocationScope.ZONE));
            checkState(location.parent().get().parent().isPresent());
            checkState(location.parent().get().parent().get().locationScope()
                .equals(LocationScope.REGION));

            //replace it with the region
            replacedTemplate = VirtualMachineTemplateBuilder.of(originalMachineTemplate)
                .location(location.parent().get().parent().get().id()).build();
        }

        return replacedTemplate;
    }

    @Override protected org.jclouds.compute.options.TemplateOptions modifyTemplateOptions(
        final VirtualMachineTemplate originalVirtualMachineTemplate,
        final org.jclouds.compute.options.TemplateOptions originalTemplateOptions) {

        final org.jclouds.compute.options.TemplateOptions templateOptionsToModify =
            super.modifyTemplateOptions(originalVirtualMachineTemplate, originalTemplateOptions);

        checkArgument(templateOptionsToModify instanceof NovaTemplateOptions);

        Location location = locationGetStrategy.get(originalVirtualMachineTemplate.locationId());

        if (location != null && location.locationScope().equals(LocationScope.ZONE)) {
            ((NovaTemplateOptions) templateOptionsToModify)
                .availabilityZone(RegionAndId.fromSlashEncoded(location.id()).getId());
        } else if (location != null && location.locationScope().equals(LocationScope.HOST)) {
            //concat availability zone and host
            String host = RegionAndId.fromSlashEncoded(location.id()).getId();
            String availabilityZone =
                RegionAndId.fromSlashEncoded(location.parent().get().id()).getId();

            ((NovaTemplateOptions) templateOptionsToModify)
                .availabilityZone(availabilityZone + ":" + host);
        }

        //set floating ip to always true
        ((NovaTemplateOptions) templateOptionsToModify).autoAssignFloatingIp(true);
        if (floatingIpPool != null) {
            ((NovaTemplateOptions) templateOptionsToModify).floatingIpPoolNames(floatingIpPool);
        }

        return templateOptionsToModify;
    }

    @Override protected org.jclouds.compute.options.TemplateOptions newTemplateOptions() {
        return new NovaTemplateOptions();
    }
}
