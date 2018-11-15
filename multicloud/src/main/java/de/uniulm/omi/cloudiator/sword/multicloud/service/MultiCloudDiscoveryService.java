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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.domain.Cloud;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.Image;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.multicloud.domain.HardwareFlavorMultiCloudImpl;
import de.uniulm.omi.cloudiator.sword.multicloud.domain.ImageMultiCloudImpl;
import de.uniulm.omi.cloudiator.sword.multicloud.domain.LocationMultiCloudImpl;
import de.uniulm.omi.cloudiator.sword.multicloud.domain.VirtualMachineMultiCloudImpl;
import de.uniulm.omi.cloudiator.sword.multicloud.exception.MultiCloudException;
import de.uniulm.omi.cloudiator.sword.service.ComputeService;
import de.uniulm.omi.cloudiator.sword.service.DiscoveryService;
import java.util.Map.Entry;
import javax.annotation.Nullable;

/**
 * Created by daniel on 17.04.15.
 */
public class MultiCloudDiscoveryService implements DiscoveryService {

  private final ComputeServiceProvider computeServiceProvider;

  @Inject
  public MultiCloudDiscoveryService(ComputeServiceProvider computeServiceProvider) {
    checkNotNull(computeServiceProvider, "computeServiceProvider is null");
    this.computeServiceProvider = computeServiceProvider;
  }

  @Nullable
  @Override
  public Image getImage(String id) {
    checkNotNull(id, "id is null");
    checkArgument(!id.isEmpty(), "id is empty");
    final IdScopedByCloud idScopedByCloud = IdScopedByClouds.from(id);
    return new ImageMultiCloudImpl(
        computeServiceProvider.forId(idScopedByCloud.cloudId()).discoveryService()
            .getImage(idScopedByCloud.id()), idScopedByCloud.cloudId());
  }

  @Nullable
  @Override
  public VirtualMachine getVirtualMachine(String id) {
    checkNotNull(id, "id is null");
    checkArgument(!id.isEmpty(), "id is empty");
    final IdScopedByCloud idScopedByCloud = IdScopedByClouds.from(id);
    return computeServiceProvider.forId(idScopedByCloud.cloudId()).discoveryService()
        .getVirtualMachine(idScopedByCloud.id());
  }

  @Override
  public Iterable<HardwareFlavor> listHardwareFlavors() {

    final Builder<HardwareFlavor> builder = ImmutableSet.builder();
    for (Entry<Cloud, ComputeService> entry : computeServiceProvider.all().entrySet()) {
      try {
        for (HardwareFlavor singleCloudHardware : entry.getValue().discoveryService()
            .listHardwareFlavors()) {
          builder
              .add(new HardwareFlavorMultiCloudImpl(singleCloudHardware, entry.getKey().id()));
        }
      } catch (Exception e) {
        throw MultiCloudException.of(entry.getKey().id(), e);
      }
    }
    return builder.build();
  }

  @Override
  public Iterable<Image> listImages() {

    final Builder<Image> builder = ImmutableSet.builder();
    for (Entry<Cloud, ComputeService> entry : computeServiceProvider.all().entrySet()) {
      try {
        for (Image singleCloudImage : entry.getValue().discoveryService()
            .listImages()) {
          builder
              .add(new ImageMultiCloudImpl(singleCloudImage, entry.getKey().id()));
        }
      } catch (Exception e) {
        throw MultiCloudException.of(entry.getKey().id(), e);
      }
    }
    return builder.build();
  }

  @Override
  public Iterable<Location> listLocations() {
    final Builder<Location> builder = ImmutableSet.builder();
    for (Entry<Cloud, ComputeService> entry : computeServiceProvider.all().entrySet()) {
      try {
        for (Location singleCloudLocation : entry.getValue().discoveryService()
            .listLocations()) {
          builder
              .add(new LocationMultiCloudImpl(singleCloudLocation, entry.getKey().id()));
        }
      } catch (Exception e) {
        throw MultiCloudException.of(entry.getKey().id(), e);
      }
    }
    return builder.build();
  }

  @Override
  public Iterable<VirtualMachine> listVirtualMachines() {
    final Builder<VirtualMachine> builder = ImmutableSet.builder();
    for (Entry<Cloud, ComputeService> entry : computeServiceProvider.all().entrySet()) {
      try {
        for (VirtualMachine singleCloudVirtualMachine : entry.getValue().discoveryService()
            .listVirtualMachines()) {
          builder
              .add(
                  new VirtualMachineMultiCloudImpl(singleCloudVirtualMachine, entry.getKey().id()));
        }
      } catch (Exception e) {
        throw MultiCloudException.of(entry.getKey().id(), e);
      }
    }
    return builder.build();
  }

  @Nullable
  @Override
  public Location getLocation(String id) {
    checkNotNull(id, "id is null");
    checkArgument(!id.isEmpty(), "id is empty");
    final IdScopedByCloud idScopedByCloud = IdScopedByClouds.from(id);
    return new LocationMultiCloudImpl(
        computeServiceProvider.forId(idScopedByCloud.cloudId()).discoveryService()
            .getLocation(idScopedByCloud.id()), idScopedByCloud.cloudId());
  }

  @Nullable
  @Override
  public HardwareFlavor getHardwareFlavor(String id) {
    checkNotNull(id, "id is null");
    checkArgument(!id.isEmpty(), "id is empty");
    final IdScopedByCloud idScopedByCloud = IdScopedByClouds.from(id);
    return new HardwareFlavorMultiCloudImpl(
        computeServiceProvider.forId(idScopedByCloud.cloudId()).discoveryService()
            .getHardwareFlavor(idScopedByCloud.id()), idScopedByCloud.cloudId());
  }

}
