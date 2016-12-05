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

import de.uniulm.omi.cloudiator.sword.drivers.jclouds.domain.AssignableLocation;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.domain.*;
import org.jclouds.compute.options.TemplateOptions;
import org.jclouds.domain.Location;

import java.util.Set;

/**
 * Created by daniel on 02.12.14.
 */
public interface JCloudsComputeClient {

    /**
     * @return images known to jclouds.
     * @see ComputeService#listImages()
     */
    Set<? extends Image> listImages();

    /**
     * @return hardware known to jclouds.
     * @see ComputeService#listHardwareProfiles()
     */
    Set<? extends Hardware> listHardwareProfiles();

    /**
     * Returns all locations known to jclouds.
     * <p/>
     * Includes all locations returned by {@link ComputeService#listAssignableLocations()}. Those
     * locations will be flagged by {@link AssignableLocation#isAssignable()}.
     * <p/>
     * In addition, it also includes all {@link Location#getParent()} locations.
     *
     * @return all assignable locations known to jclouds
     */
    Set<? extends AssignableLocation> listLocations();

    /**
     * @return all nodes known to jclouds
     * @see ComputeService#listNodes()
     */
    Set<? extends ComputeMetadata> listNodes();

    /**
     * Creates a node
     *
     * @param template the template to create the node from.
     * @return the started node
     * @see ComputeService#createNodesInGroup(String, int, TemplateOptions)
     */
    NodeMetadata createNode(Template template);

    /**
     * Deletes the node identified by the given id.
     *
     * @param id the unique identifier of the node.
     */
    void deleteNode(String id);

    /**
     * @return a new template builder for creating node templates.
     */
    TemplateBuilder templateBuilder();
}
