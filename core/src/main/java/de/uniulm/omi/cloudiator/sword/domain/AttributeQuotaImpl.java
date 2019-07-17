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

public class AttributeQuotaImpl extends QuotaImpl implements AttributeQuota {

  private final Attribute attribute;

  protected AttributeQuotaImpl(
      @Nullable String locationId,
      @Nullable BigDecimal limit,
      @Nullable BigDecimal usage,
      @Nullable BigDecimal remaining, Attribute attribute) {
    super(locationId, limit, usage, remaining);

    checkNotNull(attribute, "attribute is null");
    this.attribute = attribute;

  }

  @Override
  public Attribute attribute() {
    return attribute;
  }

  @Override
  protected ToStringHelper stringHelper() {
    return super.stringHelper().add("attribute", attribute);
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
    AttributeQuotaImpl that = (AttributeQuotaImpl) o;
    return attribute == that.attribute;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), attribute);
  }
}
