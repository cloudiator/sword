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

package de.uniulm.omi.cloudiator.sword.multicloud.service;

import de.uniulm.omi.cloudiator.sword.domain.Cloud;
import de.uniulm.omi.cloudiator.sword.domain.OfferQuota;
import de.uniulm.omi.cloudiator.sword.domain.Quota;
import de.uniulm.omi.cloudiator.sword.domain.QuotaSet;
import de.uniulm.omi.cloudiator.sword.domain.Quotas;
import de.uniulm.omi.cloudiator.sword.extensions.QuotaExtension;
import de.uniulm.omi.cloudiator.sword.service.ComputeService;
import java.util.Map;

public class MultiCloudQuotaExtension implements QuotaExtension {

  private final ComputeServiceProvider computeServiceProvider;

  public MultiCloudQuotaExtension(
      ComputeServiceProvider computeServiceProvider) {
    this.computeServiceProvider = computeServiceProvider;
  }

  @Override
  public QuotaSet quotas() {

    QuotaSet quotaSet = new QuotaSet();

    for (Map.Entry<Cloud, ComputeService> entry : computeServiceProvider.all().entrySet()) {
      if (!entry.getValue().quotaExtension().isPresent()) {
        continue;
      }

      for (Quota quota : entry.getValue().quotaExtension().get().quotas().quotaSet()) {
        final String cloudId = entry.getKey().id();
        if (quota.locationId().isPresent()) {
          quota.overrideLocation(
              IdScopedByClouds.from(quota.locationId().get(), cloudId).scopedId());
        } else {
          quota.overrideLocation(cloudId);
        }
        if (quota instanceof OfferQuota) {
          final IdScopedByCloud idScopedByCloud = IdScopedByClouds
              .from(((OfferQuota) quota).id(), cloudId);
          quota = Quotas
              .offerQuota((idScopedByCloud.scopedId()), ((OfferQuota) quota).type(), quota.limit(),
                  quota.usage(), quota.remaining(), quota.locationId().orElse(null));
        }
        quotaSet.add(quota);
      }
    }

    return quotaSet;
  }
}
