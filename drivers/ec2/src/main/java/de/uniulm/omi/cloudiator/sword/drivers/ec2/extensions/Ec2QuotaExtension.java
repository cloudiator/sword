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

package de.uniulm.omi.cloudiator.sword.drivers.ec2.extensions;

import com.google.common.base.Supplier;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.domain.LocationScope;
import de.uniulm.omi.cloudiator.sword.domain.AttributeQuota.Attribute;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.domain.OfferQuota;
import de.uniulm.omi.cloudiator.sword.domain.OfferQuota.OfferType;
import de.uniulm.omi.cloudiator.sword.domain.Properties;
import de.uniulm.omi.cloudiator.sword.domain.PropertiesBuilder;
import de.uniulm.omi.cloudiator.sword.domain.Quota;
import de.uniulm.omi.cloudiator.sword.domain.QuotaSet;
import de.uniulm.omi.cloudiator.sword.domain.Quotas;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.drivers.ec2.EC2Constants;
import de.uniulm.omi.cloudiator.sword.extensions.QuotaExtension;
import de.uniulm.omi.cloudiator.sword.strategy.GetStrategy;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Ec2QuotaExtension implements QuotaExtension {

  private final Properties properties;
  private final GetStrategy<String, HardwareFlavor> hardwareFlavorGetStrategy;
  private final Supplier<Set<Location>> locationSupplier;
  private final Supplier<Set<VirtualMachine>> virtualMachineSupplier;

  public static final Map<String, Integer> DEFAULT_QUOTAS = new HashMap<String, Integer>() {{
    put("a1.2xlarge", 3);
    put("a1.4xlarge", 3);
    put("a1.large", 3);
    put("a1.medium", 3);
    put("a1.xlarge", 3);
    put("c4.2xlarge", 5);
    put("c4.4xlarge", 1);
    put("c4.8xlarge", 1);
    put("c4.large", 5);
    put("c4.xlarge", 5);
    put("c5.18xlarge", 0);
    put("c5.2xlarge", 0);
    put("c5.4xlarge", 0);
    put("c5.9xlarge", 0);
    put("c5.large", 5);
    put("c5.xlarge", 2);
    put("c5d.18xlarge", 0);
    put("c5d.2xlarge", 1);
    put("c5d.4xlarge", 0);
    put("c5d.9xlarge", 0);
    put("c5d.large", 5);
    put("c5d.xlarge", 2);
    put("c5n.18xlarge", 0);
    put("c5n.2xlarge", 0);
    put("c5n.4xlarge", 0);
    put("c5n.9xlarge", 0);
    put("c5n.large", 5);
    put("c5n.xlarge", 2);
    put("d2.2xlarge", 0);
    put("d2.4xlarge", 0);
    put("d2.8xlarge", 0);
    put("d2.xlarge", 1);
    put("g3.16xlarge", 0);
    put("g3.4xlarge", 0);
    put("g3.8xlarge", 0);
    put("g3s.xlarge", 0);
    put("h1.16xlarge", 0);
    put("h1.2xlarge", 0);
    put("h1.4xlarge", 0);
    put("h1.8xlarge", 0);
    put("i2.2xlarge", 2);
    put("i2.4xlarge", 1);
    put("i2.8xlarge", 1);
    put("i2.xlarge", 4);
    put("i3.16xlarge", 0);
    put("i3.2xlarge", 1);
    put("i3.4xlarge", 1);
    put("i3.8xlarge", 0);
    put("i3.large", 1);
    put("i3.metal", 0);
    put("i3.xlarge", 1);
    put("i3en.12xlarge", 0);
    put("i3en.24xlarge", 0);
    put("i3en.2xlarge", 1);
    put("i3en.3xlarge", 1);
    put("i3en.6xlarge", 0);
    put("i3en.large", 3);
    put("i3en.xlarge", 3);
    put("m4.10xlarge", 0);
    put("m4.16xlarge", 0);
    put("m4.2xlarge", 5);
    put("m4.4xlarge", 0);
    put("m4.large", 20);
    put("m4.xlarge", 10);
    put("m5.12xlarge", 0);
    put("m5.16xlarge", 0);
    put("m5.24xlarge", 0);
    put("m5.2xlarge", 5);
    put("m5.4xlarge", 0);
    put("m5.8xlarge", 0);
    put("m5.large", 5);
    put("m5.metal", 0);
    put("m5.xlarge", 5);
    put("m5a.12xlarge", 0);
    put("m5a.16xlarge", 0);
    put("m5a.24xlarge", 0);
    put("m5a.2xlarge", 5);
    put("m5a.4xlarge", 0);
    put("m5a.8xlarge", 0);
    put("m5a.large", 5);
    put("m5a.xlarge", 5);
    put("m5ad.12xlarge", 0);
    put("m5ad.24xlarge", 0);
    put("m5ad.2xlarge", 1);
    put("m5ad.4xlarge", 0);
    put("m5ad.large", 5);
    put("m5ad.xlarge", 2);
    put("m5d.12xlarge", 0);
    put("m5d.16xlarge", 0);
    put("m5d.24xlarge", 0);
    put("m5d.2xlarge", 1);
    put("m5d.4xlarge", 0);
    put("m5d.8xlarge", 0);
    put("m5d.large", 5);
    put("m5d.metal", 0);
    put("m5d.xlarge", 2);
    put("p2.16xlarge", 0);
    put("p2.8xlarge", 0);
    put("p2.xlarge", 0);
    put("p3.16xlarge", 0);
    put("p3.2xlarge", 0);
    put("p3.8xlarge", 0);
    put("r3.2xlarge", 5);
    put("r3.4xlarge", 1);
    put("r3.8xlarge", 1);
    put("r3.large", 5);
    put("r3.xlarge", 5);
    put("r4.16xlarge", 0);
    put("r4.2xlarge", 1);
    put("r4.4xlarge", 1);
    put("r4.8xlarge", 1);
    put("r4.large", 1);
    put("r4.xlarge", 1);
    put("r5.12xlarge", 0);
    put("r5.16xlarge", 0);
    put("r5.24xlarge", 0);
    put("r5.2xlarge", 1);
    put("r5.4xlarge", 0);
    put("r5.8xlarge", 0);
    put("r5.large", 3);
    put("r5.metal", 0);
    put("r5.xlarge", 3);
    put("r5a.12xlarge", 0);
    put("r5a.16xlarge", 0);
    put("r5a.24xlarge", 0);
    put("r5a.2xlarge", 1);
    put("r5a.4xlarge", 0);
    put("r5a.8xlarge", 0);
    put("r5a.large", 3);
    put("r5a.xlarge", 3);
    put("r5ad.12xlarge", 0);
    put("r5ad.24xlarge", 0);
    put("r5ad.2xlarge", 1);
    put("r5ad.4xlarge", 0);
    put("r5ad.large", 3);
    put("r5ad.xlarge", 3);
    put("r5d.12xlarge", 0);
    put("r5d.16xlarge", 0);
    put("r5d.24xlarge", 0);
    put("r5d.2xlarge", 1);
    put("r5d.4xlarge", 0);
    put("r5d.8xlarge", 0);
    put("r5d.large", 3);
    put("r5d.metal", 0);
    put("r5d.xlarge", 3);
    put("t2.2xlarge", 5);
    put("t2.large", 20);
    put("t2.medium", 20);
    put("t2.micro", 20);
    put("t2.nano", 20);
    put("t2.small", 20);
    put("t2.xlarge", 5);
    put("t3.2xlarge", 1);
    put("t3.large", 3);
    put("t3.medium", 4);
    put("t3.micro", 10);
    put("t3.nano", 10);
    put("t3.small", 6);
    put("t3.xlarge", 2);
    put("t3a.2xlarge", 1);
    put("t3a.large", 3);
    put("t3a.medium", 4);
    put("t3a.micro", 10);
    put("t3a.nano", 10);
    put("t3a.small", 6);
    put("t3a.xlarge", 2);
    put("x1.16xlarge", 0);
    put("x1.32xlarge", 0);
    put("x1e.16xlarge", 0);
    put("x1e.2xlarge", 0);
    put("x1e.32xlarge", 0);
    put("x1e.4xlarge", 0);
    put("x1e.8xlarge", 0);
    put("x1e.xlarge", 0);
    put("nodeSize", 20);
  }};

  @Inject
  public Ec2QuotaExtension(Properties properties,
      GetStrategy<String, HardwareFlavor> hardwareFlavorGetStrategy,
      Supplier<Set<Location>> locationSupplier,
      Supplier<Set<VirtualMachine>> virtualMachineSupplier) {
    this.properties = filterProperties(properties);
    this.hardwareFlavorGetStrategy = hardwareFlavorGetStrategy;
    this.locationSupplier = locationSupplier;
    this.virtualMachineSupplier = virtualMachineSupplier;
  }

  private static Properties filterProperties(Properties properties) {
    Map<String, Integer> filtered = new HashMap<>();
    for (Entry<String, String> entry : properties.getProperties().entrySet()) {
      if (entry.getKey().startsWith(EC2Constants.PROPERTY_EC2_QUOTA_PREFIX)) {
        filtered.put(entry.getKey().replace(EC2Constants.PROPERTY_EC2_QUOTA_PREFIX, ""),
            Integer.valueOf(entry.getValue()));
      }
    }
    return PropertiesBuilder.newBuilder().putProperties(filtered).build();
  }

  private int override(Location location, String id, int defaultValue) {
    String key = location.providerId() + "." + id;
    //noinspection ConstantConditions
    return Integer.parseInt(properties.getProperty(key, Integer.toString(defaultValue)));
  }

  private static int calculateUsed(Set<VirtualMachine> vms, Location location, String id) {
    int count = 0;
    for (VirtualMachine virtualMachine : vms) {
      if (virtualMachine.providerId().equals(location.providerId())) {
        if (virtualMachine.hardware().isPresent() && virtualMachine.hardware().get().id()
            .equals(id)) {
          count++;
        }
      }
    }
    return count;
  }

  private static long countInLocation(Set<VirtualMachine> vms, Location location) {
    return vms.stream().filter(new Predicate<VirtualMachine>() {
      @Override
      public boolean test(VirtualMachine virtualMachine) {
        return virtualMachine.location().isPresent() && virtualMachine.location().get().id()
            .equals(location.id());
      }
    }).count();
  }


  @Override
  public QuotaSet quotas() {

    final Set<VirtualMachine> virtualMachines = virtualMachineSupplier.get();

    Set<Quota> quotas = new HashSet<>();
    for (Location location : locationSupplier.get().stream().filter(l -> l.locationScope().equals(
        LocationScope.REGION)).collect(Collectors.toSet())) {
      for (Map.Entry<String, Integer> entry : DEFAULT_QUOTAS.entrySet()) {

        if (entry.getKey().equals("nodeSize")) {
          final long usage = countInLocation(virtualMachines, location);
          Quotas.attributeQuota(Attribute.NODES_SIZE, BigDecimal.valueOf(entry.getValue()),
              BigDecimal.valueOf(usage), location.id());
        } else {
          int value = override(location, entry.getKey(), entry.getValue());
          final OfferQuota offerQuota = Quotas
              .offerQuota(entry.getKey(), OfferType.HARDWARE, BigDecimal.valueOf(value),
                  BigDecimal.valueOf(calculateUsed(virtualMachines, location, entry.getKey())),
                  location.id());
          quotas.add(offerQuota);
        }
      }
    }

    return new QuotaSet(quotas);
  }


}
