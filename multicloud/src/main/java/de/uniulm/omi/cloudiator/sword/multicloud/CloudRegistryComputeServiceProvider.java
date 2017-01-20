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

package de.uniulm.omi.cloudiator.sword.multicloud;

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.api.ServiceContext;
import de.uniulm.omi.cloudiator.sword.api.domain.Cloud;
import de.uniulm.omi.cloudiator.sword.api.domain.Configuration;
import de.uniulm.omi.cloudiator.sword.api.service.ComputeService;
import de.uniulm.omi.cloudiator.sword.service.ServiceContextBuilder;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 19.01.17.
 */
public class CloudRegistryComputeServiceProvider implements CloudRegistry, ComputeServiceProvider {

    private final ComputeServiceFactory computeServiceFactory;
    private static final ServiceContextHolder SERVICE_CONTEXT_HOLDER = new ServiceContextHolder();

    @Inject
    public CloudRegistryComputeServiceProvider(ComputeServiceFactory computeServiceFactory) {
        checkNotNull(computeServiceFactory, "computeServiceFactory is null");
        this.computeServiceFactory = computeServiceFactory;
    }

    @Override public void register(Cloud cloud, Configuration configuration) {
        checkNotNull(cloud, "cloud is null");
        checkNotNull(configuration, "configuration is null");
        SERVICE_CONTEXT_HOLDER.add(cloud, configuration);
    }

    @Override public void unregister(Cloud cloud) {
        SERVICE_CONTEXT_HOLDER.remove(cloud);
    }

    @Override public void unregister(String cloudId) {
        SERVICE_CONTEXT_HOLDER.remove(cloudId);
    }

    @Override public ComputeService forId(String cloudId) {
        final ServiceContext serviceContext = SERVICE_CONTEXT_HOLDER.retrieve(cloudId);
        if (serviceContext == null) {
            throw new NoSuchElementException(String.format(
                "Could not find a compute service for cloudId %s. No cloud with this id was registered.",
                cloudId));
        }
        return computeServiceFactory
            .computeService(serviceContext.cloud(), serviceContext.configuration());
    }

    @Override public ComputeService forCloud(Cloud cloud) {
        checkNotNull(cloud, "cloud is null");
        return forId(cloud.id());
    }

    @Override public Map<Cloud, ComputeService> all() {
        return SERVICE_CONTEXT_HOLDER.all().stream().collect(Collectors.toMap(ServiceContext::cloud,
            serviceContext -> computeServiceFactory
                .computeService(serviceContext.cloud(), serviceContext.configuration())));
    }

    private static class ServiceContextHolder {
        Map<Cloud, ServiceContext> serviceContextMap = new HashMap<>();
        Map<String, Cloud> idMap = new HashMap<>();

        void add(Cloud cloud, Configuration configuration) {
            checkNotNull(cloud, "cloud is null");
            checkNotNull(configuration, "configuration is null");
            serviceContextMap.put(cloud,
                ServiceContextBuilder.newBuilder().cloud(cloud).configuration(configuration)
                    .build());
            idMap.put(cloud.id(), cloud);
        }

        void remove(Cloud cloud) {
            checkNotNull(cloud, "cloud is null");
            serviceContextMap.remove(cloud);
            idMap.remove(cloud.id());
        }

        void remove(String cloudId) {
            checkNotNull(cloudId, "cloudId is null");
            checkArgument(!cloudId.isEmpty(), "cloudId is empty");
            final Cloud cloud = idMap.get(cloudId);
            if (cloud == null) {
                return;
            }
            serviceContextMap.remove(cloud);
        }

        @Nullable ServiceContext retrieve(Cloud cloud) {
            checkNotNull(cloud, "cloud is null");
            return serviceContextMap.get(cloud);
        }

        @Nullable ServiceContext retrieve(String cloudId) {
            checkNotNull(cloudId, "cloudId is null");
            checkArgument(!cloudId.isEmpty(), "cloudId is empty");
            final Cloud cloud = idMap.get(cloudId);
            if (cloud == null) {
                return null;
            }
            return serviceContextMap.get(cloud);
        }

        Collection<ServiceContext> all() {
            return serviceContextMap.values();
        }
    }


}
