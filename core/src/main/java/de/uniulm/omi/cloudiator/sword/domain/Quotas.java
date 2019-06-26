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

import de.uniulm.omi.cloudiator.sword.domain.AttributeQuota.Attribute;
import de.uniulm.omi.cloudiator.sword.domain.OfferQuota.OfferType;
import java.math.BigDecimal;
import javax.annotation.Nullable;

public class Quotas {


  public static AttributeQuota attributeQuota(Attribute attribute, BigDecimal limit,
      BigDecimal usage, @Nullable String locationId) {
    return new AttributeQuotaImpl(locationId, limit, usage, null, attribute);
  }

  public static AttributeQuota attributeQuota(Attribute attribute, BigDecimal remaining,
      @Nullable String locationId) {
    return new AttributeQuotaImpl(locationId, null, null, remaining, attribute);
  }

  public static OfferQuota offerQuota(String id, OfferType offerType, BigDecimal limit,
      BigDecimal usage, BigDecimal remaining, @Nullable String locationId) {
    return new OfferQuotaImpl(locationId, limit, usage, remaining, id, offerType);
  }

  public static OfferQuota offerQuota(String id, OfferType offerType, BigDecimal limit,
      BigDecimal usage, @Nullable String locationId) {
    return new OfferQuotaImpl(locationId, limit, usage, null, id, offerType);
  }

  public static OfferQuota offerQuota(String id, OfferType offerType, BigDecimal remaining,
      @Nullable String locationId) {
    return new OfferQuotaImpl(locationId, null, null, remaining, id, offerType);
  }


}
