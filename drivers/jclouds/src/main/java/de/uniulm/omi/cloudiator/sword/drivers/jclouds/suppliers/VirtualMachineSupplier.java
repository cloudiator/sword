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

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Supplier;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.JCloudsComputeClient;
import de.uniulm.omi.cloudiator.sword.util.NamingStrategy;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import java.util.Set;
import java.util.stream.Collectors;
import org.jclouds.compute.domain.ComputeMetadata;

/**
 * Created by daniel on 09.12.14.
 */
public class VirtualMachineSupplier implements Supplier<Set<VirtualMachine>> {

  private final JCloudsComputeClient jCloudsComputeClient;
  private final OneWayConverter<ComputeMetadata, VirtualMachine>
      jCloudsComputeMetadataToVirtualMachine;
  private final NamingStrategy namingStrategy;

  @Inject
  public VirtualMachineSupplier(JCloudsComputeClient jCloudsComputeClient,
      OneWayConverter<ComputeMetadata, VirtualMachine> jCloudsComputeMetadataToVirtualMachine,
      NamingStrategy namingStrategy) {

    checkNotNull(jCloudsComputeClient, "jCloudsComputeClient is null.");
    checkNotNull(jCloudsComputeMetadataToVirtualMachine,
        "jCloudsComputeMetadataToVirtualMachine is null.");
    checkNotNull(namingStrategy, "namingStrategy is null.");

    this.jCloudsComputeClient = jCloudsComputeClient;
    this.jCloudsComputeMetadataToVirtualMachine = jCloudsComputeMetadataToVirtualMachine;
    this.namingStrategy = namingStrategy;
  }

  @Override
  public Set<VirtualMachine> get() {
    return jCloudsComputeClient.listNodes().stream()
        .map(jCloudsComputeMetadataToVirtualMachine::apply)
        .filter(virtualMachine -> namingStrategy.belongsToNamingGroup().test(virtualMachine.name()))
        .collect(Collectors.toSet());
  }
}
