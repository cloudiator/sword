/*
 * Copyright (c) 2014-2015 University of Ulm
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.uniulm.omi.cloudiator.sword.drivers.flexiant;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.uniulm.omi.cloudiator.flexiant.client.api.FlexiantException;
import de.uniulm.omi.cloudiator.flexiant.client.api.ResourceInLocation;
import de.uniulm.omi.cloudiator.flexiant.client.domain.Hardware;
import de.uniulm.omi.cloudiator.flexiant.client.domain.Image;
import de.uniulm.omi.cloudiator.flexiant.client.domain.Location;
import de.uniulm.omi.cloudiator.flexiant.client.domain.LocationScope;
import de.uniulm.omi.cloudiator.flexiant.client.domain.Server;
import de.uniulm.omi.cloudiator.flexiant.client.domain.ServerTemplate;
import de.uniulm.omi.cloudiator.flexiant.client.domain.generic.ResourceImpl;
import de.uniulm.omi.cloudiator.sword.domain.Cloud;
import de.uniulm.omi.cloudiator.sword.properties.Constants;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.inject.Named;


/**
 * Created by daniel on 05.12.14.
 */
@Singleton
public class FlexiantComputeClientImpl implements FlexiantComputeClient {

  private final de.uniulm.omi.cloudiator.flexiant.client.compute.FlexiantComputeClient
      flexiantComputeClient;
  private final Cloud cloud;
  private final Supplier<Set<String>> validClusterIdSupplier;
  @Inject(optional = true)
  @Named(Constants.SWORD_REGIONS)
  String clusterFilter;

  @Inject
  public FlexiantComputeClientImpl(Cloud cloud) {

    checkNotNull(cloud, "cloud is null");

    checkArgument(cloud.endpoint().isPresent(), "No endpoint configured for FCO.");

    flexiantComputeClient =
        new de.uniulm.omi.cloudiator.flexiant.client.compute.FlexiantComputeClient(
            cloud.endpoint().get(), cloud.credential().user(), cloud.credential().password());
    this.cloud = cloud;
    this.validClusterIdSupplier =
        Suppliers.memoizeWithExpiration(new ValidClusterSupplier(), 10, TimeUnit.MINUTES);
  }

  /**
   * todo: find a better way to cache those ids.
   */
  private Set<String> validClusterIds() {
    return validClusterIdSupplier.get();
  }

  private Predicate<? super ResourceInLocation> resourceFilter() {
    return resourceInLocation -> validClusterIds()
        .contains(resourceInLocation.getLocationUUID());
  }

  private Predicate<Location> locationFilter() {
    return location -> {
      if (clusterFilter == null) {
        return true;
      }
      switch (location.getLocationScope()) {
        case CLUSTER:
          return validClusterIds().contains(location.getId());
        case VDC:
          checkState(location.getParent() != null);
          return validClusterIds().contains(location.getParent().getId());
        default:
          throw new AssertionError(String.format(
              "Could not filter flexiant locations, only expecting %s and %s as location scope",
              LocationScope.CLUSTER, LocationScope.VDC));
      }
    };
  }

  private Set<Location> listClusters() throws FlexiantException {
    return this.flexiantComputeClient.getLocations().stream()
        .filter(location -> location.getLocationScope().equals(LocationScope.CLUSTER))
        .collect(Collectors.toSet());
  }

  @Override
  public Set<Image> listImages() {
    try {
      return this.flexiantComputeClient.getImages(null).stream().filter(resourceFilter())
          .collect(Collectors.toSet());
    } catch (FlexiantException e) {
      throw new RuntimeException("Could not retrieve images", e);
    }
  }

  @Override
  public Set<Hardware> listHardware() {
    try {
      return this.flexiantComputeClient.getHardwareFlavors(null).stream()
          .filter(resourceFilter()).collect(Collectors.toSet());
    } catch (FlexiantException e) {
      throw new RuntimeException("Could not retrieve hardware", e);
    }
  }

  @Override
  public Set<Location> listLocations() {
    try {
      return this.flexiantComputeClient.getLocations().stream().filter(locationFilter())
          .collect(Collectors.toSet());
    } catch (FlexiantException e) {
      throw new RuntimeException("Could not retrieve images", e);
    }
  }

  @Override
  public Set<Server> listServers() {
    try {
      return this.flexiantComputeClient.getServers(cloud.configuration().nodeGroup(), null)
          .stream().filter(resourceFilter()).collect(Collectors.toSet());
    } catch (FlexiantException e) {
      throw new RuntimeException("Could not retrieve servers.", e);
    }
  }

  @Override
  public Server createServer(ServerTemplate serverTemplate) {
    try {
      return this.flexiantComputeClient.createServer(serverTemplate);
    } catch (FlexiantException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteServer(String serverUUID) {
    try {
      this.flexiantComputeClient.deleteServer(serverUUID);
    } catch (FlexiantException e) {
      throw new RuntimeException(e);
    }
  }

  private class ValidClusterSupplier implements Supplier<Set<String>> {

    @Override
    public Set<String> get() {
      try {
        Set<String> allClusterIds =
            listClusters().stream().map(ResourceImpl::getId).collect(Collectors.toSet());
        Set<String> clustersToUse = Sets.newHashSetWithExpectedSize(allClusterIds.size());
        if (clusterFilter == null) {
          return allClusterIds;
        }
        Sets.newHashSet(Arrays.asList(clusterFilter.split(","))).forEach(clusterUUID -> {
          if (!allClusterIds.contains(clusterUUID)) {
            throw new IllegalArgumentException(String.format(
                "Configuration option %s wants to whitelist cluster %s, but this cluster does not exist",
                Constants.SWORD_REGIONS, clusterUUID));
          }
          clustersToUse.add(clusterUUID);
        });
        return clustersToUse;

      } catch (FlexiantException e) {
        throw new IllegalStateException(e);
      }
    }
  }


}
