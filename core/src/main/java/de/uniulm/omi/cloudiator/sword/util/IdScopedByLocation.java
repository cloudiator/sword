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

package de.uniulm.omi.cloudiator.sword.util;

import javax.annotation.Nullable;

/**
 * Represents an id which has a location scope,
 * e.g. regionOne/1 where regionOne is the location scope
 * and 1 is the real id.
 * <p/>
 * Use {@link IdScopeByLocations} to create objects.
 */
public interface IdScopedByLocation {

  /**
   * @return the id part of the scoped id.
   */
  String getId();

  /**
   * @return the location part of the scoped id, null if no location scope exists.
   */
  @Nullable
  String getLocationId();

  /**
   * @return the scoped id as string.
   */
  String getIdWithLocation();

}
