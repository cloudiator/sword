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

import com.google.common.base.Supplier;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.domain.LocationScoped;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * todo: should not only consider the parent location but all parent locations
 */
public class FilteringLocationScopedSupplier<T extends LocationScoped> implements Supplier<Set<T>> {

  private final Supplier<Set<T>> toBeFiltered;
  private final Set<String> whiteList;
  private final Set<String> blackList;

  public FilteringLocationScopedSupplier(Supplier<Set<T>> toBeFiltered,
      Set<String> whiteList, Set<String> blackList) {

    this.toBeFiltered = toBeFiltered;
    this.whiteList = whiteList;
    this.blackList = blackList;
  }

  @Override
  public Set<T> get() {
    return toBeFiltered.get().stream().filter(new Predicate<T>() {
      @Override
      public boolean test(T t) {
        Optional<Location> locationOptional = t.location();
        if (!locationOptional.isPresent()) {
          return true;
        }

        if (blackList.contains(locationOptional.get().providerId())) {
          return false;
        }

        if (!whiteList.isEmpty() && !whiteList.contains(locationOptional.get().providerId())) {
          return false;
        }

        return true;
      }
    }).collect(Collectors.toSet());
  }
}
