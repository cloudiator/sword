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

import de.uniulm.omi.cloudiator.sword.service.ComputeService;
import de.uniulm.omi.cloudiator.sword.service.DiscoveryService;

/**
 * An example showing the discovery service.
 */
public class DiscoverOffersExample {

  private final ComputeService computeService;

  public DiscoverOffersExample(
      ComputeService computeService) {
    this.computeService = computeService;
  }

  public void discover() {
    DiscoveryService discoveryService =
        computeService.discoveryService();

    //print hardware offers
    discoveryService.listHardwareFlavors().forEach(System.out::println);

    //print locations
    discoveryService.listLocations().forEach(System.out::println);

    //print images
    discoveryService.listImages().forEach(System.out::println);

    //prints the virtual machines in the node group managed by sword
    discoveryService.listVirtualMachines().forEach(System.out::println);
  }
}
