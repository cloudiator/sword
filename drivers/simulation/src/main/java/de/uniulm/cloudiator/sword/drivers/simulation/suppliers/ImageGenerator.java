/*
 * Copyright (c) 2014-2019 University of Ulm
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

package de.uniulm.cloudiator.sword.drivers.simulation.suppliers;

import com.google.common.base.Supplier;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.domain.OperatingSystem;
import de.uniulm.omi.cloudiator.domain.OperatingSystemArchitecture;
import de.uniulm.omi.cloudiator.domain.OperatingSystemBuilder;
import de.uniulm.omi.cloudiator.domain.OperatingSystemFamily;
import de.uniulm.omi.cloudiator.domain.OperatingSystemVersion;
import de.uniulm.omi.cloudiator.sword.domain.Image;
import de.uniulm.omi.cloudiator.sword.domain.ImageBuilder;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import java.util.Set;

public class ImageGenerator implements Supplier<Set<Image>> {

  private final Supplier<Set<Location>> locationSupplier;

  @Inject
  public ImageGenerator(
      Supplier<Set<Location>> locationSupplier) {
    this.locationSupplier = locationSupplier;
  }

  @Override
  public Set<Image> get() {

    Set<Image> images = Sets.newHashSet();

    for (Location location : locationSupplier.get()) {
      for (OperatingSystemFamily operatingSystemFamily : OperatingSystemFamily.values()) {
        for (OperatingSystemVersion version : operatingSystemFamily.operatingSystemVersionFormat()
            .allVersions()) {

          final OperatingSystem operatingSystem = OperatingSystemBuilder.newBuilder().architecture(
              OperatingSystemArchitecture.AMD64).family(operatingSystemFamily)
              .version(version).build();

          final String generateId = generateId(operatingSystem);
          final Image image = ImageBuilder.newBuilder().id(location.id() + "/" + generateId)
              .location(location).name(generateId)
              .os(operatingSystem).providerId(generateId).build();

          images.add(image);
        }
      }
    }

    return images;
  }

  private static String generateId(OperatingSystem os) {
    //noinspection OptionalGetWithoutIsPresent
    return os.operatingSystemFamily() + "-" + os.operatingSystemArchitecture() + "-" + os
        .operatingSystemVersion().name().get();
  }

}
