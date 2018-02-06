/*
 * Copyright (c) 2014-2018 University of Ulm
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

package org.cloudiator.meta.cloudharmony;

import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.domain.ProviderIdentifiable;
import java.util.function.Function;

public class CachedCloudHarmonyMetaService extends CachedMetaService {

  public CachedCloudHarmonyMetaService(
      CloudHarmonyMetaService cloudHarmonyMetaService) {
    super(cloudHarmonyMetaService);
  }


  @Override
  protected Function<HardwareFlavor, String> hardwareIdentity() {
    return ProviderIdentifiable::providerId;
  }

  @Override
  protected Function<Location, String> locationIdentity() {
    return ProviderIdentifiable::providerId;
  }
}
