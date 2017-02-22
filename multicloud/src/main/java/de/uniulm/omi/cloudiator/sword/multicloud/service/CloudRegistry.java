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

import de.uniulm.omi.cloudiator.sword.ServiceContext;
import de.uniulm.omi.cloudiator.domain.Cloud;
import de.uniulm.omi.cloudiator.domain.Configuration;

/**
 * Created by daniel on 19.01.17.
 */
public interface CloudRegistry {

    /**
     * Registers a new {@link Cloud} at the registry.
     *
     * @param cloud         the cloud to register.
     * @param configuration the configuration for that cloud.
     */
    CloudRegistry register(Cloud cloud, Configuration configuration);

    /**
     * Registers a new {@link ServiceContext} at the registry.
     *
     * @param serviceContext the service context to register.
     */
    CloudRegistry register(ServiceContext serviceContext);

    /**
     * Unregisters an existing {@link Cloud} at the registry if its present.
     *
     * @param cloud the {@link Cloud} to remove.
     */
    CloudRegistry unregister(Cloud cloud);

    /**
     * Unregisters the cloud identified by the id if present.
     *
     * @param cloudId the id of the cloud to remove.
     */
    CloudRegistry unregister(String cloudId);
}
