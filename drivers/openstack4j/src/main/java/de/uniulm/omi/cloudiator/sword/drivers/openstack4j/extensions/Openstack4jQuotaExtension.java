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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.extensions;

import static com.google.common.base.Preconditions.checkState;

import com.google.inject.Inject;
import com.google.inject.Provider;
import de.uniulm.omi.cloudiator.sword.domain.AttributeQuota;
import de.uniulm.omi.cloudiator.sword.domain.AttributeQuota.Attribute;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.domain.QuotaSet;
import de.uniulm.omi.cloudiator.sword.domain.Quotas;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.internal.RegionSupplier;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.internal.UserIdentity;
import de.uniulm.omi.cloudiator.sword.extensions.QuotaExtension;
import java.math.BigDecimal;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.compute.QuotaSetService;
import org.openstack4j.model.compute.SimpleTenantUsage;
import org.openstack4j.model.compute.SimpleTenantUsage.ServerUsage;

public class Openstack4jQuotaExtension implements QuotaExtension {


  private final Provider<OSClient> osClient;
  private final UserIdentity userIdentity;
  private final RegionSupplier regionSupplier;

  @Inject
  public Openstack4jQuotaExtension(Provider<OSClient> osClient,
      UserIdentity userIdentity,
      RegionSupplier regionSupplier) {
    this.osClient = osClient;
    this.userIdentity = userIdentity;
    this.regionSupplier = regionSupplier;
  }

  private QuotaSet perRegion(String tenant, Location location) {
    final QuotaSetService quotaSetService = osClient.get().useRegion(location.providerId()).compute()
        .quotaSets();
    final SimpleTenantUsage tenantUsage = quotaSetService.getTenantUsage(tenant);
    final org.openstack4j.model.compute.QuotaSet quotaSet = quotaSetService.get(tenant);

    int memUsage = 0;
    int cpuUsage = 0;
    int servers = 0;

    if (tenantUsage.getServerUsages() != null) {
      for (ServerUsage serverUsage : tenantUsage.getServerUsages()) {
        memUsage += serverUsage.getMemoryMb();
        cpuUsage += serverUsage.getVcpus();
        servers++;
      }
    }

    final AttributeQuota cores = Quotas
        .attributeQuota(Attribute.HARDWARE_CORES, BigDecimal.valueOf(quotaSet.getCores()),
            BigDecimal.valueOf(cpuUsage), location.id());

    final AttributeQuota ram = Quotas
        .attributeQuota(Attribute.HARDWARE_RAM, BigDecimal.valueOf(quotaSet.getRam()),
            BigDecimal.valueOf(memUsage), location.id());

    final AttributeQuota nodes = Quotas
        .attributeQuota(Attribute.NODES_SIZE, BigDecimal.valueOf(quotaSet.getInstances()),
            BigDecimal.valueOf(servers), location.id());

    return new QuotaSet().add(cores).add(ram).add(nodes);

  }

  @Override
  public QuotaSet quotas() {

    String tenant = userIdentity.getTenant();
    checkState(tenant != null, "user is null");
    QuotaSet quotaSet = new QuotaSet();

    for (Location location : regionSupplier.get()) {
      quotaSet.add(perRegion(tenant, location));
    }

    return quotaSet;
  }
}
