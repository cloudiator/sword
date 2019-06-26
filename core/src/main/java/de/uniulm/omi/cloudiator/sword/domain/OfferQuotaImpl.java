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

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.MoreObjects.ToStringHelper;
import java.math.BigDecimal;
import java.util.Objects;
import javax.annotation.Nullable;

public class OfferQuotaImpl extends QuotaImpl implements OfferQuota {

  private final String id;
  private final OfferType offerType;

  protected OfferQuotaImpl(
      @Nullable String locationId,
      @Nullable BigDecimal limit,
      @Nullable BigDecimal usage,
      @Nullable BigDecimal remaining, String id,
      OfferType offerType) {
    super(locationId, limit, usage, remaining);

    checkNotNull(id, "id is null");
    checkNotNull(offerType, "offerType is null");

    this.id = id;
    this.offerType = offerType;
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public OfferType type() {
    return offerType;
  }

  @Override
  public ToStringHelper stringHelper() {
    return super.stringHelper().add("id", id).add("type", offerType);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    OfferQuotaImpl that = (OfferQuotaImpl) o;
    return id.equals(that.id) &&
        offerType == that.offerType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id, offerType);
  }
}
