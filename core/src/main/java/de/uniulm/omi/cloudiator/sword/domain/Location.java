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

package de.uniulm.omi.cloudiator.sword.domain;

import de.uniulm.omi.cloudiator.domain.LocationScope;
import de.uniulm.omi.cloudiator.domain.Named;
import java.util.Optional;

/**
 * Represents a location offered by
 * the provider.
 */
public interface Location extends ProviderIdentifiable, Named, Tagged {

  /**
   * The scope of the location, e.g. REGION or ZONE.
   */
  LocationScope locationScope();

  /**
   * A location is assignable, if it can be used
   * for starting virtual machines at the provider.
   *
   * @return true if assignable, no if not.
   */
  boolean isAssignable();

  /**
   * The {@link Optional} parent location, e.g. availability zone - region.
   * Is absent of top level.
   *
   * @return the parent location, absent of top level.
   */
  Optional<Location> parent();

  /**
   * The {@link Optional} geographical location.
   *
   * @return the geographical location or absent
   */
  Optional<GeoLocation> geoLocation();
}
