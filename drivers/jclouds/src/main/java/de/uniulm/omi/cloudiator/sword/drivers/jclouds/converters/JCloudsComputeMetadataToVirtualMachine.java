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

package de.uniulm.omi.cloudiator.sword.drivers.jclouds.converters;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.Image;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachineBuilder;
import de.uniulm.omi.cloudiator.sword.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.domain.LoginCredentials;

/**
 * Converts a jclouds {@link ComputeMetadata} object into a {@link VirtualMachine} object. <p>
 * Requires the compute metadata object to be of type {@link NodeMetadata}, as otherwise not all
 * required information is available.
 */
public class JCloudsComputeMetadataToVirtualMachine
    implements OneWayConverter<ComputeMetadata, VirtualMachine> {

  private final OneWayConverter<LoginCredentials, LoginCredential> loginCredentialConverter;
  private final OneWayConverter<Hardware, HardwareFlavor> hardwareConverter;
  private final GetStrategy<String, Image> imageGetStrategy;
  private final OneWayConverter<org.jclouds.domain.Location, Location> locationConverter;

  /**
   * Constructor.
   *
   * @param loginCredentialConverter a converter for the login credentials (non null)
   * @param hardwareConverter a converter for the hardware
   * @param imageGetStrategy strategy to retrieve images from an ID
   * @param locationConverter a converter for the location
   */
  @Inject
  public JCloudsComputeMetadataToVirtualMachine(
      OneWayConverter<LoginCredentials, LoginCredential> loginCredentialConverter,
      OneWayConverter<Hardware, HardwareFlavor> hardwareConverter,
      GetStrategy<String, Image> imageGetStrategy,
      OneWayConverter<org.jclouds.domain.Location, Location> locationConverter) {

    checkNotNull(loginCredentialConverter);
    checkNotNull(hardwareConverter);
    checkNotNull(imageGetStrategy);

    this.loginCredentialConverter = loginCredentialConverter;
    this.hardwareConverter = hardwareConverter;
    this.imageGetStrategy = imageGetStrategy;
    this.locationConverter = locationConverter;
  }

  @Override
  public VirtualMachine apply(final ComputeMetadata computeMetadata) {

    checkArgument(computeMetadata instanceof NodeMetadata);

    VirtualMachineBuilder virtualMachineBuilder = VirtualMachineBuilder.newBuilder();
    virtualMachineBuilder.id(computeMetadata.getId())
        .providerId(computeMetadata.getProviderId()).name(forceName(computeMetadata));

    ((NodeMetadata) computeMetadata).getPrivateAddresses()
        .forEach(virtualMachineBuilder::addIpString);
    ((NodeMetadata) computeMetadata).getPublicAddresses()
        .forEach(virtualMachineBuilder::addIpString);
    if (((NodeMetadata) computeMetadata).getCredentials() != null) {
      virtualMachineBuilder.loginCredential(this.loginCredentialConverter
          .apply(((NodeMetadata) computeMetadata).getCredentials()));
    }

    virtualMachineBuilder
        .hardware(hardwareConverter.apply(((NodeMetadata) computeMetadata).getHardware()));

    virtualMachineBuilder.location(locationConverter.apply(computeMetadata.getLocation()));

    String imageId = ((NodeMetadata) computeMetadata).getImageId();
    if (imageId != null) {
      virtualMachineBuilder.image(imageGetStrategy.get(imageId));
    }

    return virtualMachineBuilder.build();
  }

  private String forceName(ComputeMetadata computeMetadata) {
    if (computeMetadata.getName() == null) {
      return computeMetadata.getId();
    }
    return computeMetadata.getName();
  }


}
