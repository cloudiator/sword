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

package de.uniulm.omi.executionware.drivers.jclouds;


import com.google.inject.Inject;
import de.uniulm.omi.executionware.api.ServiceConfiguration;
import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.Image;
import org.jclouds.domain.Location;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 01.12.14.
 */
public class JCloudsComputeClient implements JCloudsComputeClientApi {

    private final ComputeServiceContext computeServiceContext;

    @Inject
    public JCloudsComputeClient(ServiceConfiguration serviceConfiguration) {

        checkNotNull(serviceConfiguration);

        this.computeServiceContext = ContextBuilder.newBuilder(serviceConfiguration.getProvider())
                .endpoint(serviceConfiguration.getEndpoint())
                .credentials(serviceConfiguration.getCredentials().getUser(), serviceConfiguration.getCredentials().getPassword())
                .buildView(ComputeServiceContext.class);
    }

    @Override
    public Set<? extends Image> listImages() {
        return this.computeServiceContext.getComputeService().listImages();
    }

    @Override
    public Set<? extends Hardware> listHardwareProfiles() {
        return this.computeServiceContext.getComputeService().listHardwareProfiles();
    }

    @Override
    public Set<? extends Location> listAssignableLocations() {
        return this.computeServiceContext.getComputeService().listAssignableLocations();
    }

    @Override
    public Set<? extends ComputeMetadata> listNodes() {
        return this.computeServiceContext.getComputeService().listNodes();
    }
}
