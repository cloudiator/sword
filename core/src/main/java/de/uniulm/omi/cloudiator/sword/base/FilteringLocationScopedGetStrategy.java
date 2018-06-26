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

import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.domain.LocationScoped;
import de.uniulm.omi.cloudiator.sword.strategy.GetStrategy;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;

/**
 * todo: should not only consider the parent location but all parent locations
 */
public class FilteringLocationScopedGetStrategy<S, T extends LocationScoped> implements
    GetStrategy<S, T> {


  private final GetStrategy<S, T> delegate;
  private final Set<String> whiteList;
  private final Set<String> blackList;

  public FilteringLocationScopedGetStrategy(
      GetStrategy<S, T> delegate, Set<String> whiteList, Set<String> blackList) {

    this.delegate = delegate;
    this.whiteList = whiteList;
    this.blackList = blackList;
  }

  @Nullable
  @Override
  public T get(S s) {

    final T t = delegate.get(s);

    if (t == null) {
      return null;
    }

    Optional<Location> locationOptional = t.location();

    if (!locationOptional.isPresent()) {
      return t;
    }

    if (blackList.contains(locationOptional.get().providerId())) {
      return null;
    }

    if (!whiteList.isEmpty() && !whiteList.contains(locationOptional.get().providerId())) {
      return null;
    }

    return t;
  }
}
