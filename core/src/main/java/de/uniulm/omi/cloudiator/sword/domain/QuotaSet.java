/*
 * Copyright (c) 2014-2019 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class QuotaSet {

  public static QuotaSet EMPTY = new QuotaSet();

  private final Set<Quota> quotaSet;


  public QuotaSet() {
    this.quotaSet = new HashSet<>();
  }

  public QuotaSet(Collection<? extends Quota> quotaSet) {
    this.quotaSet = new HashSet<>(quotaSet);
  }

  public QuotaSet(QuotaSet quotaSet) {
    this.quotaSet = new HashSet<>(quotaSet.quotaSet());
  }

  public QuotaSet add(Quota quota) {
    this.quotaSet.add(quota);
    return this;
  }

  public QuotaSet add(QuotaSet quotaSet) {
    this.quotaSet.addAll(quotaSet.quotaSet());
    return this;
  }

  public QuotaSet addAll(Collection<? extends Quota> quotas) {
    this.quotaSet.addAll(quotas);
    return this;
  }

  private QuotaSet locationFilter(Predicate<String> locationPredicate) {
    return new QuotaSet(quotaSet.stream().filter(
        quota -> locationPredicate.test(quota.locationId().orElse(null)))
        .collect(Collectors.toSet()));
  }

  public Collection<Quota> quotaSet() {
    return quotaSet;
  }

  @Override
  public String toString() {
    final ToStringHelper toStringHelper = MoreObjects.toStringHelper(this)
        .add("size", quotaSet.size());
    if (quotaSet.size() <= 10) {
      toStringHelper.add("quotaSet", quotaSet);
    }
    return toStringHelper.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuotaSet quotaSet1 = (QuotaSet) o;
    return quotaSet.equals(quotaSet1.quotaSet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(quotaSet);
  }
}
