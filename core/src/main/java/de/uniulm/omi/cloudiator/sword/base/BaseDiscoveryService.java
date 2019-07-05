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

package de.uniulm.omi.cloudiator.sword.base;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Supplier;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.uniulm.omi.cloudiator.domain.OperatingSystem;
import de.uniulm.omi.cloudiator.domain.OperatingSystemBuilder;
import de.uniulm.omi.cloudiator.sword.annotations.Memoized;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavorBuilder;
import de.uniulm.omi.cloudiator.sword.domain.Image;
import de.uniulm.omi.cloudiator.sword.domain.ImageBuilder;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.domain.LocationBuilder;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.properties.Constants;
import de.uniulm.omi.cloudiator.sword.service.DiscoveryService;
import de.uniulm.omi.cloudiator.sword.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.sword.strategy.OperatingSystemDetectionStrategy;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

/**
 * Created by daniel on 25.09.15.
 */
public class BaseDiscoveryService implements DiscoveryService {

  @Inject(optional = true)
  @Named(Constants.IMAGE_GROUPING)
  private boolean imageGrouping = false;


  private final Supplier<Set<Image>> imageSupplier;
  private final Supplier<Set<Location>> locationSupplier;
  private final Supplier<Set<HardwareFlavor>> hardwareFlavorSupplier;
  private final Supplier<Set<VirtualMachine>> virtualMachineSupplier;
  private final GetStrategy<String, Image> imageGetStrategy;
  private final GetStrategy<String, Location> locationGetStrategy;
  private final GetStrategy<String, HardwareFlavor> hardwareFlavorGetStrategy;
  private final GetStrategy<String, VirtualMachine> virtualMachineGetStrategy;
  @Nullable
  private final MetaService metaService;
  private final OperatingSystemDetectionStrategy operatingSystemDetectionStrategy;

  @Inject
  public BaseDiscoveryService(@Memoized Supplier<Set<Image>> imageSupplier,
      @Memoized Supplier<Set<Location>> locationSupplier,
      @Memoized Supplier<Set<HardwareFlavor>> hardwareFlavorSupplier,
      Supplier<Set<VirtualMachine>> virtualMachineSupplier,
      GetStrategy<String, Image> imageGetStrategy,
      GetStrategy<String, Location> locationGetStrategy,
      GetStrategy<String, HardwareFlavor> hardwareFlavorGetStrategy,
      GetStrategy<String, VirtualMachine> virtualMachineGetStrategy, MetaService metaService,
      OperatingSystemDetectionStrategy operatingSystemDetectionStrategy) {

    checkNotNull(imageSupplier);
    checkNotNull(locationSupplier);
    checkNotNull(hardwareFlavorSupplier);
    checkNotNull(virtualMachineSupplier);
    checkNotNull(imageGetStrategy);
    checkNotNull(locationGetStrategy);
    checkNotNull(hardwareFlavorGetStrategy);
    checkNotNull(virtualMachineGetStrategy);
    checkNotNull(operatingSystemDetectionStrategy);

    this.imageSupplier = imageSupplier;
    this.locationSupplier = locationSupplier;
    this.hardwareFlavorSupplier = hardwareFlavorSupplier;
    this.virtualMachineSupplier = virtualMachineSupplier;
    this.imageGetStrategy = imageGetStrategy;
    this.locationGetStrategy = locationGetStrategy;
    this.hardwareFlavorGetStrategy = hardwareFlavorGetStrategy;
    this.virtualMachineGetStrategy = virtualMachineGetStrategy;
    this.metaService = metaService;
    this.operatingSystemDetectionStrategy = operatingSystemDetectionStrategy;
  }

  @Override
  @Nullable
  public Image getImage(String id) {
    checkNotNull(id);
    checkArgument(!id.isEmpty());
    final Image image = imageGetStrategy.get(id);

    if (image == null) {
      return null;
    }

    final OperatingSystem detectedOs =
        operatingSystemDetectionStrategy.detectOperatingSystem(image);
    return ImageBuilder.of(image)
        .os(OperatingSystemBuilder.of(image.operatingSystem()).merge(detectedOs).build())
        .location(annotateMetaData(image.location().orElse(null)))
        .build();
  }

  @Override
  @Nullable
  public VirtualMachine getVirtualMachine(String id) {
    checkNotNull(id);
    checkArgument(!id.isEmpty());
    return this.virtualMachineGetStrategy.get(id);
  }

  private Location annotateMetaData(Location location) {
    if (location == null) {
      return null;
    }
    if (location.parent().isPresent()) {
      location = LocationBuilder.of(location).parent(annotateMetaData((location).parent().get()))
          .build();
    }
    return LocationBuilder.of(location)
        .geoLocation(metaService.geoLocation(location).orElse(location.geoLocation().orElse(null)))
        .build();
  }

  @Override
  @Nullable
  public Location getLocation(String id) {
    checkNotNull(id);
    checkArgument(!id.isEmpty());
    final Location location = locationGetStrategy.get(id);
    return annotateMetaData(location);
  }

  @Override
  @Nullable
  public HardwareFlavor getHardwareFlavor(String id) {
    checkNotNull(id);
    checkArgument(!id.isEmpty());
    final HardwareFlavor hardwareFlavor = hardwareFlavorGetStrategy.get(id);
    if (hardwareFlavor == null) {
      return null;
    }
    return HardwareFlavorBuilder.of(hardwareFlavor)
        .location(annotateMetaData(hardwareFlavor.location().orElse(null))).build();

  }

  @Override
  public Iterable<HardwareFlavor> listHardwareFlavors() {
    return hardwareFlavorSupplier.get().stream()
        .map((Function<HardwareFlavor, HardwareFlavor>) hardwareFlavor -> HardwareFlavorBuilder
            .of(hardwareFlavor)
            .location(annotateMetaData(hardwareFlavor.location().orElse(null))).build())
        .collect(Collectors.toSet());
  }

  @Override
  public Iterable<Image> listImages() {
    final Set<Image> images = imageSupplier.get().stream().map((Function<Image, Image>) image -> {
      final OperatingSystem detectedOs =
          operatingSystemDetectionStrategy.detectOperatingSystem(image);
      return ImageBuilder.of(image)
          .os(OperatingSystemBuilder.of(image.operatingSystem()).merge(detectedOs).build())
          .location(annotateMetaData(image.location().orElse(null)))
          .build();
    }).collect(Collectors.toSet());

    if (imageGrouping) {
      final Map<ImageGroupBy, Image> grouped = images.stream().collect(Collectors.groupingBy(
          image -> new ImageGroupBy(image.operatingSystem(), image.locationId().orElse(null)),
          Collectors.collectingAndThen(
              Collectors
                  .reducing((Image d1, Image d2) -> d1.name().compareTo(d2.name()) >= 0 ? d1 : d2),
              Optional::get
          )
      ));
      return grouped.values();
    } else {
      return images;
    }
  }

  @Override
  public Iterable<Location> listLocations() {
    return locationSupplier.get().stream().map(this::annotateMetaData)
        .collect(Collectors.toSet());
  }

  @Override
  public Iterable<VirtualMachine> listVirtualMachines() {
    return virtualMachineSupplier.get();
  }

  private static class ImageGroupBy {

    OperatingSystem os;
    String location;

    public ImageGroupBy(OperatingSystem os, String location) {
      this.os = os;
      this.location = location;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      ImageGroupBy groupBy = (ImageGroupBy) o;
      return os.equals(groupBy.os) &&
          Objects.equals(location, groupBy.location);
    }

    @Override
    public int hashCode() {
      return Objects.hash(os, location);
    }
  }
}
