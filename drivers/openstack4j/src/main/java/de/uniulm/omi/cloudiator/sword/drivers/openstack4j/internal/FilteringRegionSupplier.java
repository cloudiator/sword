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

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.properties.Constants;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by daniel on 02.12.16.
 */
public class FilteringRegionSupplier implements RegionSupplier {

  private final RegionSupplier delegate;
  @Inject(optional = true)
  @Named(Constants.SWORD_REGIONS)
  private String regionFilter = null;

  public FilteringRegionSupplier(RegionSupplier delegate) {
    checkNotNull(delegate, "delegate is null");
    this.delegate = delegate;
  }

  @Override
  public Set<Location> get() {
    if (regionFilter == null) {
      return delegate.get();
    }
    final Set<String> split = new HashSet<>(Arrays.asList(regionFilter.split(",")));
    return delegate.get().stream().filter(location -> split.contains(location.id()))
        .collect(Collectors.toSet());
  }
}
