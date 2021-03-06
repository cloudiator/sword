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

package de.uniulm.omi.cloudiator.sword.drivers.jclouds.suppliers;

import com.google.common.base.Supplier;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.JCloudsComputeClient;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import java.util.Set;
import java.util.stream.Collectors;
import org.jclouds.compute.domain.Hardware;

/**
 * Created by daniel on 03.12.14.
 */
public class HardwareSupplier implements Supplier<Set<HardwareFlavor>> {

  private final JCloudsComputeClient jCloudsComputeClient;
  private final OneWayConverter<Hardware, HardwareFlavor> jCloudsHardwareToHardwareFlavor;

  @Inject
  public HardwareSupplier(JCloudsComputeClient jCloudsComputeClient,
      OneWayConverter<Hardware, HardwareFlavor> jCloudsHardwareToHardwareFlavor) {
    this.jCloudsComputeClient = jCloudsComputeClient;
    this.jCloudsHardwareToHardwareFlavor = jCloudsHardwareToHardwareFlavor;
  }

  @Override
  public Set<HardwareFlavor> get() {
    return jCloudsComputeClient.listHardwareProfiles().stream()
        .map(jCloudsHardwareToHardwareFlavor::apply).collect(Collectors.toSet());
  }
}
