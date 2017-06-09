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

import de.uniulm.omi.cloudiator.sword.domain.Cloud;
import java.util.Collection;
import javax.annotation.Nullable;

/**
 * Created by daniel on 19.01.17.
 */
public interface CloudRegistry {

  /**
   * Registers a new {@link Cloud} at the registry.
   *
   * @param cloud the cloud to register.
   */
  CloudRegistry register(Cloud cloud);

  /**
   * Checks of the cloud is already registered.
   *
   * @param cloud the cloud
   * @return true if already registered, false if not.
   */
  boolean isRegistered(Cloud cloud);

  /**
   * Lists all clouds stored in the registry.
   *
   * @return a collection of all clouds
   */
  Collection<Cloud> list();

  /**
   * Retrieve cloud.
   *
   * @param cloudId the cloudId of the cloud
   * @return the cloud with the given cloudId
   */
  @Nullable
  Cloud getCloud(String cloudId);

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
