/*
 * Copyright (c) 2014-2017 University of Ulm
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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.domain.Cloud;
import de.uniulm.omi.cloudiator.sword.service.ComputeService;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

/**
 * Created by daniel on 19.01.17.
 */
public class CloudRegistryComputeServiceProvider implements CloudRegistry, ComputeServiceProvider {

  private static final CloudHolder CLOUD_HOLDER = new CloudHolder();
  private final ComputeServiceFactory computeServiceFactory;

  @Inject
  public CloudRegistryComputeServiceProvider(ComputeServiceFactory computeServiceFactory) {
    checkNotNull(computeServiceFactory, "computeServiceFactory is null");
    this.computeServiceFactory = computeServiceFactory;
  }

  @Override
  public CloudRegistry register(Cloud cloud) {
    checkNotNull(cloud, "cloud is null");
    CLOUD_HOLDER.add(cloud);
    return this;
  }

  @Override
  public boolean isRegistered(Cloud cloud) {
    return CLOUD_HOLDER.retrieve(cloud.id()) != null;
  }

  @Override
  public Collection<Cloud> list() {
    return CLOUD_HOLDER.all();
  }

  @Override
  @Nullable
  public Cloud getCloud(String cloudId) {
    return CLOUD_HOLDER.retrieve(cloudId);
  }

  @Override
  public CloudRegistry unregister(Cloud cloud) {
    CLOUD_HOLDER.remove(cloud);
    return this;
  }

  @Override
  public CloudRegistry unregister(String cloudId) {
    CLOUD_HOLDER.remove(cloudId);
    return this;
  }

  @Override
  public ComputeService forId(String cloudId) {
    Cloud cloud = CLOUD_HOLDER.retrieve(cloudId);
    if (cloud == null) {
      throw new NoSuchElementException(String.format(
          "Could not find a compute service for cloudId %s. No cloud with this id was registered.",
          cloudId));
    }
    return computeServiceFactory.computeService(cloud);
  }

  @Override
  public ComputeService forCloud(Cloud cloud) {
    checkNotNull(cloud, "cloud is null");
    return forId(cloud.id());
  }

  @Override
  public Map<Cloud, ComputeService> all() {
    return CLOUD_HOLDER.all().stream()
        .collect(Collectors.toMap(Function.identity(), computeServiceFactory::computeService));
  }

  private static class CloudHolder {

    Map<String, Cloud> cloudMap = new HashMap<>();

    void add(Cloud cloud) {
      checkNotNull(cloud, "cloud is null");
      cloudMap.put(cloud.id(), cloud);
    }

    void remove(Cloud cloud) {
      checkNotNull(cloud, "cloud is null");
      cloudMap.remove(cloud.id());
    }

    void remove(String cloudId) {
      checkNotNull(cloudId, "cloudId is null");
      checkArgument(!cloudId.isEmpty(), "cloudId is empty");
      cloudMap.remove(cloudId);
    }

    @Nullable
    Cloud retrieve(String cloudId) {
      checkNotNull(cloudId, "cloudId is null");
      checkArgument(!cloudId.isEmpty(), "cloudId is empty");
      return cloudMap.get(cloudId);
    }

    Collection<Cloud> all() {
      return cloudMap.values();
    }
  }


}
