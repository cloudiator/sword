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

package de.uniulm.omi.cloudiator.sword.drivers.azure.suppliers;

import com.google.common.base.Supplier;
import com.google.inject.Inject;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.compute.ImageReference;
import com.microsoft.azure.management.compute.KnownLinuxVirtualMachineImage;
import com.microsoft.azure.management.compute.KnownWindowsVirtualMachineImage;
import com.microsoft.azure.management.compute.VirtualMachineCustomImage;
import de.uniulm.omi.cloudiator.domain.OperatingSystems;
import de.uniulm.omi.cloudiator.sword.domain.Image;
import de.uniulm.omi.cloudiator.sword.domain.ImageBuilder;
import de.uniulm.omi.cloudiator.util.OneWayConverter;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by daniel on 16.05.17.
 */
public class ImageSupplier implements Supplier<Set<Image>> {

  private final Azure azure;
  private final OneWayConverter<VirtualMachineCustomImage, Image> customImageConverter;

  @Inject
  public ImageSupplier(Azure azure, OneWayConverter<VirtualMachineCustomImage, Image> customImageConverter) {
    this.azure = azure;
    this.customImageConverter = customImageConverter;
  }

  @Override
  public Set<Image> get() {
    Stream<Image> linux = Arrays.stream(KnownLinuxVirtualMachineImage.values())
        .map(ImageSupplier::convertLinuxImage);
    Stream<Image> windows = Arrays.stream(KnownWindowsVirtualMachineImage.values())
        .map(ImageSupplier::convertWindowsImage);
    Stream<Image> staticImages = Stream.concat(linux, windows);
    Stream<Image> customImages = azure.virtualMachineCustomImages().list().stream()
        .map(customImageConverter);

    return Stream.concat(staticImages, customImages).collect(Collectors.toSet());
  }

  private static Image convertLinuxImage(KnownLinuxVirtualMachineImage image) {
    return buildImage(image.imageReference(), image.name());
  }

  private static Image convertWindowsImage(KnownWindowsVirtualMachineImage image) {
    return buildImage(image.imageReference(), image.name());
  }

  private static Image buildImage(ImageReference imageReference, String id) {
    String name = MessageFormat.format("{0}-{1}-{2}",
        imageReference.publisher(), imageReference.offer(), imageReference.sku());
    return ImageBuilder.newBuilder()
        .id(id)
        .providerId(id)
        .name(name)
        .os(OperatingSystems.unknown())
        .build();
  }
}
