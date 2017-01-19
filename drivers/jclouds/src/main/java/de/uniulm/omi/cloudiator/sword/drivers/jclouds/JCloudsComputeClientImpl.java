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
import de.uniulm.omi.cloudiator.sword.api.ServiceContext;
import de.uniulm.omi.cloudiator.sword.api.exceptions.DriverException;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.domain.AssignableLocation;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.domain.AssignableLocationImpl;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.*;
import org.jclouds.domain.Location;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.*;

/**
 * Created by daniel on 01.12.14.
 */
public class JCloudsComputeClientImpl implements JCloudsComputeClient {

    private final ComputeServiceContext computeServiceContext;
    private final ServiceContext serviceContext;

    @Inject public JCloudsComputeClientImpl(ComputeServiceContext computeServiceContext,
        ServiceContext serviceContext) {

        checkNotNull(computeServiceContext);
        checkNotNull(serviceContext);

        this.computeServiceContext = computeServiceContext;
        this.serviceContext = serviceContext;

    }

    @Override public Set<? extends Image> listImages() {
        return this.computeServiceContext.getComputeService().listImages();
    }

    @Override public Set<? extends Hardware> listHardwareProfiles() {
        return this.computeServiceContext.getComputeService().listHardwareProfiles();
    }

    @Override public Set<? extends AssignableLocation> listLocations() {

        Set<AssignableLocation> jCloudLocations = new HashSet<>(
            computeServiceContext.getComputeService().listAssignableLocations().size());
        jCloudLocations.addAll(
            computeServiceContext.getComputeService().listAssignableLocations().stream().map(
                (Function<Location, AssignableLocation>) location -> new AssignableLocationImpl(
                    location, true)).collect(Collectors.toList()));

        //todo code is rather clumsy. realizes on hashset not adding the new data, and the equals
        //method not picking up the isAssignable...
        Set<AssignableLocation> parentLocations = new HashSet<>();
        for (Location location : jCloudLocations) {
            for (Location parent = location.getParent();
                 parent != null; parent = parent.getParent()) {
                parentLocations.add(new AssignableLocationImpl(parent, false));
            }
        }
        jCloudLocations.addAll(parentLocations);
        return ImmutableSet.copyOf(jCloudLocations);
    }

    @Override public Set<? extends ComputeMetadata> listNodes() {
        return this.computeServiceContext.getComputeService()
            .listNodesDetailsMatching(input -> true);
    }

    @Override public NodeMetadata createNode(Template template) {
        try {
            Set<? extends NodeMetadata> nodesInGroup =
                this.computeServiceContext.getComputeService()
                    .createNodesInGroup(this.serviceContext.configuration().nodeGroup(), 1,
                        template);
            checkElementIndex(0, nodesInGroup.size());
            checkState(nodesInGroup.size() == 1);
            return nodesInGroup.iterator().next();
        } catch (RunNodesException e) {
            throw new DriverException(e);
        }
    }

    @Override public void deleteNode(String id) {
        //todo check if node belongs to correct node group, otherwise do not delete it.
        this.computeServiceContext.getComputeService().destroyNode(id);
    }

    @Override public TemplateBuilder templateBuilder() {
        return this.computeServiceContext.getComputeService().templateBuilder();
    }

}
