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
import de.uniulm.omi.cloudiator.sword.service.ComputeService;
import de.uniulm.omi.cloudiator.sword.multicloud.service.CloudRegistry;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 23.01.17.
 */
public class MultiCloudServiceImpl implements MultiCloudService {

    private final CloudRegistry cloudRegistry;
    private final ComputeService computeService;

    @Inject
    public MultiCloudServiceImpl(CloudRegistry cloudRegistry, ComputeService computeService) {
        checkNotNull(cloudRegistry, "cloudRegistry is null");
        checkNotNull(computeService, "computeService is null");
        this.cloudRegistry = cloudRegistry;
        this.computeService = computeService;
    }


    @Override public CloudRegistry cloudRegistry() {
        return cloudRegistry;
    }

    @Override public ComputeService computeService() {
        return computeService;
    }
}
