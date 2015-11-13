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

package de.uniulm.omi.cloudiator.sword.drivers.jclouds;


import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.api.ServiceConfiguration;
import de.uniulm.omi.cloudiator.sword.api.exceptions.DriverException;
import de.uniulm.omi.cloudiator.sword.api.logging.LoggerFactory;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.logging.JCloudsLoggingModule;
import org.jclouds.ContextBuilder;
import org.jclouds.aws.ec2.reference.AWSEC2Constants;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.*;
import org.jclouds.domain.Location;
import org.jclouds.ec2.reference.EC2Constants;

import java.util.Properties;
import java.util.Set;

import static com.google.common.base.Preconditions.*;

/**
 * Created by daniel on 01.12.14.
 */
public class JCloudsComputeClientImpl implements JCloudsComputeClient {

    private final ComputeServiceContext computeServiceContext;
    private final ServiceConfiguration serviceConfiguration;

    @Inject public JCloudsComputeClientImpl(ServiceConfiguration serviceConfiguration,
        LoggerFactory loggerFactory) {

        checkNotNull(serviceConfiguration);
        checkNotNull(loggerFactory);

        this.serviceConfiguration = serviceConfiguration;

        //todo ugly hack
        final Properties properties = new Properties();
        properties.setProperty(AWSEC2Constants.PROPERTY_EC2_AMI_QUERY, "owner-id=amazon,self;state=available;image-type=machine");

        //todo duplicates code from NovaApiProvider
        ContextBuilder contextBuilder =
            ContextBuilder.newBuilder(serviceConfiguration.getProvider());
        contextBuilder.credentials(serviceConfiguration.getCredentials().user(),
            serviceConfiguration.getCredentials().password())
            .modules(ImmutableSet.of(new JCloudsLoggingModule(loggerFactory)))
            .overrides(properties);


        // setting optional endpoint, check for present first
        // as builder does not allow null values...
        if (serviceConfiguration.getEndpoint().isPresent()) {
            contextBuilder.endpoint(serviceConfiguration.getEndpoint().get());
        }

        this.computeServiceContext = contextBuilder.buildView(ComputeServiceContext.class);
    }

    @Override public Set<? extends Image> listImages() {
        return this.computeServiceContext.getComputeService().listImages();
    }

    @Override public Set<? extends Hardware> listHardwareProfiles() {



        return this.computeServiceContext.getComputeService().listHardwareProfiles();
    }

    @Override public Set<? extends Location> listAssignableLocations() {
        return this.computeServiceContext.getComputeService().listAssignableLocations();
    }

    @Override public Set<? extends ComputeMetadata> listNodes() {
        return this.computeServiceContext.getComputeService().listNodesDetailsMatching(
            input -> input.getName().startsWith(serviceConfiguration.getNodeGroup()));
    }

    @Override public NodeMetadata createNode(Template template) {
        try {
            Set<? extends NodeMetadata> nodesInGroup =
                this.computeServiceContext.getComputeService()
                    .createNodesInGroup(this.serviceConfiguration.getNodeGroup(), 1, template);
            checkElementIndex(0, nodesInGroup.size());
            checkState(nodesInGroup.size() == 1);
            return nodesInGroup.iterator().next();
        } catch (RunNodesException e) {
            throw new DriverException(e);
        }
    }

    @Override public void deleteNode(String id) {
        this.computeServiceContext.getComputeService().destroyNode(id);
    }

    @Override public TemplateBuilder templateBuilder() {
        return this.computeServiceContext.getComputeService().templateBuilder();
    }

}
