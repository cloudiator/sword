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
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.domain.LocationScoped;
import de.uniulm.omi.cloudiator.sword.domain.ProviderIdentifiable;
import de.uniulm.omi.cloudiator.sword.properties.Constants;
import de.uniulm.omi.cloudiator.sword.strategy.GetStrategy;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Named;

public class FilteringFactory {

  private final static String DELIMITER = ",";
  private final Set<String> whiteList;
  private final Set<String> blackList;

  @Inject
  FilteringFactory(WhileListHolder whileListHolder, BlackListHolder blackListHolder) {

    if (whileListHolder.value != null) {
      this.whiteList = handleDelimiterString(whileListHolder.value);
    } else {
      this.whiteList = Collections.emptySet();
    }

    if (blackListHolder.value != null) {
      this.blackList = handleDelimiterString(blackListHolder.value);
    } else {
      this.blackList = Collections.emptySet();
    }
  }

  private static Set<String> handleDelimiterString(String s) {
    final String[] splits = s.split(DELIMITER);
    final Set<String> result = new HashSet<>(splits.length);

    for (String split : splits) {
      result.add(split.trim());
    }

    return result;
  }

  public <T extends ProviderIdentifiable> Supplier<Set<T>> filter(
      Supplier<Set<T>> toBeFiltered) {
    return new FilteringProviderIdentifiableSupplier<T>(toBeFiltered, whiteList, blackList);
  }

  public <T extends LocationScoped> Supplier<Set<T>> filterLocationScoped(
      Supplier<Set<T>> toBeFiltered) {
    return new FilteringLocationScopedSupplier<T>(toBeFiltered, whiteList, blackList);
  }

  public <T extends ProviderIdentifiable, S> GetStrategy<S, T> filter(
      GetStrategy<S, T> getStrategy) {
    return new FilteringProviderIdentifiableGetStrategy<>(getStrategy, whiteList, blackList);
  }

  public <T extends LocationScoped, S> GetStrategy<S, T> filterLocationScoped(
      GetStrategy<S, T> getStrategy) {
    return new FilteringLocationScopedGetStrategy<>(getStrategy, whiteList, blackList);
  }

  static class WhileListHolder {

    @Named(Constants.PROVIDERID_WHITELIST)
    @Inject(optional = true)
    String value = null;
  }

  static class BlackListHolder {

    @Named(Constants.PROVIDERID_BLACKLIST)
    @Inject(optional = true)
    String value = null;
  }

}
