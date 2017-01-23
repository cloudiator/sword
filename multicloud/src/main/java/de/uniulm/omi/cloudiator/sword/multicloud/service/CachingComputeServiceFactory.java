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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.api.annotations.Base;
import de.uniulm.omi.cloudiator.sword.api.domain.Cloud;
import de.uniulm.omi.cloudiator.sword.api.domain.Configuration;
import de.uniulm.omi.cloudiator.sword.api.service.ComputeService;

import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 18.01.17.
 */
public class CachingComputeServiceFactory implements ComputeServiceFactory {

    private final ComputeServiceCache computeServiceCache;

    @Inject public CachingComputeServiceFactory(@Base ComputeServiceFactory delegate) {
        checkNotNull(delegate, "delegate is null");
        this.computeServiceCache = new ComputeServiceCache(delegate);
    }

    @Override public ComputeService computeService(Cloud cloud, Configuration configuration) {
        return computeServiceCache.retrieve(cloud, configuration);
    }

    private static class ComputeServiceCache {

        private final ComputeServiceFactory computeServiceFactory;

        private Cache<Cloud, CacheEntry> cache =
            CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES).build();

        private ComputeServiceCache(ComputeServiceFactory computeServiceFactory) {
            checkNotNull(computeServiceFactory, "computeServiceFactory is null");
            this.computeServiceFactory = computeServiceFactory;
        }

        public ComputeService retrieve(Cloud cloud, Configuration configuration) {

            CacheEntry cacheEntry = cache.getIfPresent(cloud);
            if (cacheEntry != null && !cacheEntry.configuration().equals(configuration)) {
                cache.invalidate(cloud);
                cacheEntry = null;
            }
            if (cacheEntry == null) {
                cacheEntry =
                    new CacheEntry(computeServiceFactory.computeService(cloud, configuration),
                        configuration);
                cache.put(cloud, cacheEntry);
            }
            return cacheEntry.computeService();
        }

        private static class CacheEntry {

            private final ComputeService computeService;
            private final Configuration configuration;

            private CacheEntry(ComputeService computeService, Configuration configuration) {
                checkNotNull(computeService, "computeService is null");
                checkNotNull(configuration, "configuration is null");
                this.computeService = computeService;
                this.configuration = configuration;
            }

            ComputeService computeService() {
                return computeService;
            }

            Configuration configuration() {
                return configuration;
            }
        }

    }


}
