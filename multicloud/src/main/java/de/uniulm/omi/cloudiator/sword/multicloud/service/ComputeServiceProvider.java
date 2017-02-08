/*
 * Copyright (c) 2014-2017 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.multicloud.service;

import de.uniulm.omi.cloudiator.domain.Cloud;
import de.uniulm.omi.cloudiator.sword.api.service.ComputeService;

import java.util.Map;

/**
 * Created by daniel on 19.01.17.
 */
public interface ComputeServiceProvider {

    /**
     * Retrieves a {@link ComputeService} for the given cloudId.
     *
     * @param cloudId the identifier of the cloud
     * @return a {@link ComputeService} for the cloud if it exists.
     * @throws java.util.NoSuchElementException if a compute service for this cloud id does not exist.
     */
    ComputeService forId(String cloudId);

    /**
     * Retrieves a {@link ComputeService} for the given {@link Cloud}.
     *
     * @param cloud the {@link Cloud} to retrieve the {@link ComputeService} for.
     * @return a {@link ComputeService} for the {@link Cloud} if it exists.
     * @throws java.util.NoSuchElementException if it does not exist.
     */
    ComputeService forCloud(Cloud cloud);

    /**
     * Returns an immutable {@link Map} of all compute services known for this cloud.
     *
     * @return a map containing all compute services known.
     */
    Map<Cloud, ComputeService> all();

}
