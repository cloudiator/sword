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

package de.uniulm.omi.cloudiator.sword.multicloud.service;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachineTemplate;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachineTemplateBuilder;
import de.uniulm.omi.cloudiator.sword.extensions.KeyPairExtension;
import de.uniulm.omi.cloudiator.sword.extensions.PublicIpExtension;
import de.uniulm.omi.cloudiator.sword.extensions.QuotaExtension;
import de.uniulm.omi.cloudiator.sword.extensions.SecurityGroupExtension;
import de.uniulm.omi.cloudiator.sword.multicloud.domain.VirtualMachineMultiCloudImpl;
import de.uniulm.omi.cloudiator.sword.service.ComputeService;
import de.uniulm.omi.cloudiator.sword.service.ConnectionService;
import de.uniulm.omi.cloudiator.sword.service.DiscoveryService;

/**
 * Created by daniel on 20.01.17.
 */
public class MultiCloudComputeService implements ComputeService {

  private final ComputeServiceProvider computeServiceProvider;

  @Inject
  public MultiCloudComputeService(ComputeServiceProvider computeServiceProvider) {
    checkNotNull(computeServiceProvider, "computeServiceProvider is null");
    this.computeServiceProvider = computeServiceProvider;
  }

  @Override
  public DiscoveryService discoveryService() {
    return new MultiCloudDiscoveryService(computeServiceProvider);
  }

  @Override
  public void deleteVirtualMachine(String virtualMachineId) {
    final IdScopedByCloud scopedVmId = IdScopedByClouds.from(virtualMachineId);
    this.computeServiceProvider.forId(scopedVmId.cloudId())
        .deleteVirtualMachine(scopedVmId.id());
  }

  @Override
  public VirtualMachine createVirtualMachine(
      final VirtualMachineTemplate virtualMachineTemplate) {
    final IdScopedByCloud scopedImageId =
        IdScopedByClouds.from(virtualMachineTemplate.imageId());
    final IdScopedByCloud scopedLocationId =
        IdScopedByClouds.from(virtualMachineTemplate.locationId());
    final IdScopedByCloud scopedHardwareId =
        IdScopedByClouds.from(virtualMachineTemplate.hardwareFlavorId());

    checkState(
        scopedImageId.cloudId().equals(scopedLocationId.cloudId()) && scopedLocationId.cloudId()
            .equals(scopedHardwareId.cloudId()), String.format(
            "VirtualMachineTemplate %s has different cloud targets. Image targets cloud %s, Location targets cloud %s and Hardware targets cloud %s.",
            virtualMachineTemplate, scopedImageId.cloudId(), scopedLocationId.cloudId(),
            scopedHardwareId.cloudId()));

    final VirtualMachineTemplate singleCloudTemplate =
        VirtualMachineTemplateBuilder.of(virtualMachineTemplate).image(scopedImageId.id())
            .location(scopedLocationId.id()).hardwareFlavor(scopedHardwareId.id()).build();

    //we chose the cloudId of the image, but we asserted above that it is the same for hardware
    //and location;
    String cloudId = scopedImageId.cloudId();

    final VirtualMachine virtualMachine =
        this.computeServiceProvider.forId(scopedLocationId.cloudId())
            .createVirtualMachine(singleCloudTemplate);

    return new VirtualMachineMultiCloudImpl(virtualMachine, cloudId);
  }

  @Override
  public ConnectionService connectionService() {
    return new MultiCloudConnectionService(computeServiceProvider);
  }

  @Override
  public Optional<PublicIpExtension> publicIpExtension() {
    return Optional.of(new MultiCloudPublicIpExtension(computeServiceProvider));
  }

  @Override
  @Deprecated
  public Optional<KeyPairExtension> keyPairExtension() {
    return Optional.of(new MultiCloudKeyPairExtension(computeServiceProvider));
  }

  @Override
  public Optional<SecurityGroupExtension> securityGroupExtension() {
    return Optional.of(new MultiCloudSecurityGroupExtension(computeServiceProvider));
  }

  @Override
  public Optional<QuotaExtension> quotaExtension() {
    return Optional.of(new MultiCloudQuotaExtension(computeServiceProvider));
  }
}
