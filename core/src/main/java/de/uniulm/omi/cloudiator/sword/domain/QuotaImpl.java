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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;

public abstract class QuotaImpl implements Quota {

  @Nullable
  private String locationId;

  @Nullable
  private final BigDecimal limit;
  @Nullable
  private final BigDecimal usage;
  @Nullable
  private final BigDecimal remaining;

  protected QuotaImpl(@Nullable String locationId, @Nullable BigDecimal limit,
      @Nullable BigDecimal usage,
      @Nullable BigDecimal remaining) {

    this.locationId = locationId;
    this.limit = limit;
    this.usage = usage;
    this.remaining = remaining;

    checkArgument(remaining != null || (limit != null && usage != null),
        "Either remaining or limit and usage need to be set");
  }

  @Override
  @Nullable
  public BigDecimal limit() {
    return limit;
  }

  @Override
  @Nullable
  public BigDecimal usage() {
    return usage;
  }

  @Override
  public BigDecimal remaining() {
    if (remaining == null) {
      checkState(limit != null && usage != null);
      return limit.subtract(usage);
    }
    return remaining;
  }

  @Override
  public Optional<String> locationId() {
    return Optional.ofNullable(locationId);
  }

  @Override
  public Quota overrideLocation(String locationId) {
    this.locationId = locationId;
    return this;
  }

  protected ToStringHelper stringHelper() {
    return MoreObjects.toStringHelper(this)
        .add("location", locationId)
        .add("limit", limit)
        .add("usage", usage)
        .add("remaining", remaining);
  }

  @Override
  public String toString() {
    return stringHelper().toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuotaImpl quota = (QuotaImpl) o;
    return Objects.equals(locationId, quota.locationId) &&
        Objects.equals(limit, quota.limit) &&
        Objects.equals(usage, quota.usage) &&
        Objects.equals(remaining, quota.remaining);
  }

  @Override
  public int hashCode() {
    return Objects.hash(locationId, limit, usage, remaining);
  }


}
