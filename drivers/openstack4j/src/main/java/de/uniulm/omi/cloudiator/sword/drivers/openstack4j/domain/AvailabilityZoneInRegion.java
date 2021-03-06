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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.domain;

import static com.google.common.base.Preconditions.checkNotNull;

import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.util.IdScopeByLocations;
import java.util.Map;
import org.openstack4j.model.compute.ext.AvailabilityZone;

/**
 * Created by daniel on 18.11.16.
 */
public class AvailabilityZoneInRegion implements AvailabilityZone, InRegion, ProviderIdentified {

  private final AvailabilityZone delegate;
  private final Location region;
  private final String regionScopedId;

  public AvailabilityZoneInRegion(AvailabilityZone delegate, Location region) {
    checkNotNull(delegate, "delegate is null");
    checkNotNull(region, "region is null");
    this.delegate = delegate;
    this.region = region;
    this.regionScopedId =
        IdScopeByLocations.from(region.id(), delegate.getZoneName()).getIdWithLocation();
  }

  public String getId() {
    return regionScopedId;
  }

  @Override
  public Location region() {
    return region;
  }

  @Override
  public String providerId() {
    return delegate.getZoneName();
  }

  @Override
  public ZoneState getZoneState() {
    return delegate.getZoneState();
  }

  @Override
  public Map<String, Map<String, ? extends NovaService>> getHosts() {
    return delegate.getHosts();
  }

  @Override
  public String getZoneName() {
    return delegate.getZoneName();
  }
}
