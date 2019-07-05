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

package de.uniulm.omi.cloudiator.sword.drivers.openstack.extensions;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.Supplier;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.domain.LocationScope;
import de.uniulm.omi.cloudiator.sword.domain.AttributeQuota;
import de.uniulm.omi.cloudiator.sword.domain.AttributeQuota.Attribute;
import de.uniulm.omi.cloudiator.sword.domain.Cloud;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.domain.Properties;
import de.uniulm.omi.cloudiator.sword.domain.QuotaSet;
import de.uniulm.omi.cloudiator.sword.domain.Quotas;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.OpenstackConstants;
import de.uniulm.omi.cloudiator.sword.extensions.QuotaExtension;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import org.jclouds.openstack.keystone.config.KeystoneProperties;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.Quota;
import org.jclouds.openstack.nova.v2_0.domain.SimpleServerUsage;
import org.jclouds.openstack.nova.v2_0.extensions.QuotaApi;
import org.jclouds.openstack.nova.v2_0.extensions.SimpleTenantUsageApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenstackQuotaExtension implements QuotaExtension {

  private static final Logger LOGGER = LoggerFactory.getLogger(OpenstackQuotaExtension.class);
  private final NovaApi novaApi;
  private final Properties properties;
  private final Cloud cloud;
  private final Supplier<Set<Location>> locationSupplier;

  @Inject
  public OpenstackQuotaExtension(NovaApi novaApi,
      Properties properties, Cloud cloud,
      Supplier<Set<Location>> locationSupplier) {
    this.novaApi = novaApi;
    this.properties = properties;
    this.cloud = cloud;
    this.locationSupplier = locationSupplier;
  }

  private String deriveTenant() throws TenantException {

    final String tenantID = this.properties
        .getProperty(OpenstackConstants.TENANT_ID, KeystoneProperties.TENANT_ID);

    if (tenantID == null) {
      throw new TenantException(String.format("Property %s needs to be set to derive the tenant.",
          OpenstackConstants.TENANT_ID));
    }

    return tenantID;
  }

  @Deprecated
  private String deriveTenantV2() throws TenantException {

    final String tenantName = this.properties.getProperty(KeystoneProperties.TENANT_NAME);
    final String tenantID = this.properties.getProperty(KeystoneProperties.TENANT_ID);

    if (tenantName != null) {
      return tenantName;
    }

    if (tenantID != null) {
      return tenantID;
    }

    final String[] split = cloud.credential().user().split(":");
    if (split.length == 2) {
      return split[0];
    }

    throw new TenantException("Failed to derive tenant");

  }

  @Deprecated
  private String deriveTenantV3() throws TenantException {

    final String scope = this.properties.getProperty(KeystoneProperties.SCOPE);

    if (scope == null) {
      throw new TenantException("Keystone scope is not set. Can not derive tenant");
    }

    final String[] split = scope.split(":");

    if (split.length != 2 || !split[0].equals("project")) {
      throw new TenantException("Expected keystone scope of format project:tenant");
    }

    return split[1];

  }


  private QuotaSet forRegion(Location location) {
    checkState(location.locationScope().equals(LocationScope.REGION));

    if (!novaApi.getQuotaApi(location.providerId()).isPresent()) {
      return QuotaSet.EMPTY;
    }

    if (!novaApi.getSimpleTenantUsageApi(location.providerId()).isPresent()) {
      return QuotaSet.EMPTY;
    }

    final QuotaApi quotaApi = novaApi.getQuotaApi(location.providerId()).get();
    final SimpleTenantUsageApi simpleTenantUsageApi = novaApi
        .getSimpleTenantUsageApi(location.providerId()).get();

    String tenant;
    try {
      tenant = deriveTenant();
    } catch (TenantException e) {
      LOGGER.warn("Problem deriving tenant, returning empty quota set", e);
      return QuotaSet.EMPTY;
    }

    final Quota byTenant = quotaApi.getByTenant(tenant);

    //calculate the usage
    int memUsage = 0;
    int cpuUsage = 0;
    int servers = 0;

    if (simpleTenantUsageApi.get(tenant) != null) {

      for (SimpleServerUsage simpleServerUsage : simpleTenantUsageApi.get(tenant)
          .getServerUsages()) {
        memUsage += simpleServerUsage.getFlavorMemoryMb();
        cpuUsage += simpleServerUsage.getFlavorVcpus();
        servers++;

      }
    }

    final AttributeQuota cores = Quotas
        .attributeQuota(Attribute.HARDWARE_CORES, BigDecimal.valueOf(byTenant.getCores()),
            BigDecimal.valueOf(cpuUsage), location.id());

    final AttributeQuota ram = Quotas
        .attributeQuota(Attribute.HARDWARE_RAM, BigDecimal.valueOf(byTenant.getRam()),
            BigDecimal.valueOf(memUsage), location.id());

    final AttributeQuota nodes = Quotas
        .attributeQuota(Attribute.NODES_SIZE, BigDecimal.valueOf(byTenant.getInstances()),
            BigDecimal.valueOf(servers), location.id());

    return new QuotaSet().add(cores).add(ram).add(nodes);

  }

  @Override
  public QuotaSet quotas() {

    QuotaSet quotaSet = new QuotaSet();

    for (Location location : locationSupplier.get().stream()
        .filter(l -> l.locationScope().equals(LocationScope.REGION)).collect(
            Collectors.toSet())) {
      quotaSet.add(forRegion(location));
    }

    return quotaSet;

  }

  private static class TenantException extends Exception {

    public TenantException(String s) {
      super(s);
    }
  }
}
