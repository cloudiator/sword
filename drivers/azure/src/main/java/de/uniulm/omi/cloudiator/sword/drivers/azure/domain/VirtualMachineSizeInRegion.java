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

package de.uniulm.omi.cloudiator.sword.drivers.azure.domain;

import static com.google.common.base.Preconditions.checkNotNull;

import com.microsoft.azure.management.compute.VirtualMachineSize;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.util.IdScopeByLocations;

/**
 * Created by daniel on 22.05.17.
 */
public class VirtualMachineSizeInRegion implements VirtualMachineSize, InRegion,
    ProviderIdentified {

  private final VirtualMachineSize delegate;
  private final Location region;
  private final String regionScopedId;

  public VirtualMachineSizeInRegion(
      VirtualMachineSize delegate, Location region) {
    checkNotNull(region, "region is null");
    this.region = region;
    checkNotNull(delegate, "delegate is null");
    this.delegate = delegate;
    regionScopedId = IdScopeByLocations.from(region.id(), delegate.name()).getIdWithLocation();
  }

  @Override
  public int numberOfCores() {
    return delegate.numberOfCores();
  }

  @Override
  public int osDiskSizeInMB() {
    return delegate.osDiskSizeInMB();
  }

  @Override
  public int resourceDiskSizeInMB() {
    return delegate.resourceDiskSizeInMB();
  }

  @Override
  public int memoryInMB() {
    return delegate.memoryInMB();
  }

  @Override
  public int maxDataDiskCount() {
    return delegate.maxDataDiskCount();
  }

  @Override
  public String name() {
    return regionScopedId;
  }

  @Override
  public String providerId() {
    return delegate.name();
  }

  @Override
  public Location region() {
    return region;
  }
}
