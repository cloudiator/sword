/*
 * Copyright (c) 2014-2016 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.types.ServiceType;
import org.openstack4j.model.identity.v2.Access;
import org.openstack4j.model.identity.v2.Endpoint;

/**
 * Created by daniel on 18.11.16.
 */
public class OsClientV2RegionSupplier implements RegionSupplier {

  private final OSClient.OSClientV2 osClientV2;
  private final OneWayConverter<String, Location> regionConverter;

  @Inject
  public OsClientV2RegionSupplier(OSClient osClient,
      OneWayConverter<String, Location> regionConverter) {
    checkNotNull(regionConverter, "regionConverter is null");
    this.regionConverter = regionConverter;
    checkNotNull(osClient, "osClient is null");
    checkState(osClient instanceof OSClient.OSClientV2,
        "Illegal version of OSClient supplied.");
    this.osClientV2 = (OSClient.OSClientV2) osClient;
  }

  @Override
  public Set<Location> get() {
    return new AccessToRegionSet().apply(osClientV2.getAccess()).stream().map(regionConverter)
        .collect(Collectors.toSet());
  }

  /**
   * @todo returns all regions based on compute service. Probably needs to be refined per service
   * type.
   */
  private static class AccessToRegionSet implements Function<Access, Set<String>> {

    @Override
    public Set<String> apply(Access access) {
      return access.getServiceCatalog().stream().filter(
          (Predicate<Access.Service>) service -> service.getServiceType()
              .equals(ServiceType.COMPUTE)).flatMap(
          (Function<Access.Service, Stream<? extends Endpoint>>) service -> service
              .getEndpoints().stream()).map(Endpoint::getRegion).collect(Collectors.toSet());
    }
  }
}
