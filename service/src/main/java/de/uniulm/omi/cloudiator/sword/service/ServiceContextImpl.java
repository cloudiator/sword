/*
 * Copyright (c) 2014-2015 University of Ulm
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.uniulm.omi.cloudiator.sword.service;

import de.uniulm.omi.cloudiator.sword.api.ServiceContext;
import de.uniulm.omi.cloudiator.domain.Cloud;
import de.uniulm.omi.cloudiator.domain.Configuration;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 02.12.14.
 */
public class ServiceContextImpl implements ServiceContext {

    private final Cloud cloud;
    private final Configuration configuration;

    ServiceContextImpl(Cloud cloud, Configuration configuration) {

        checkNotNull(cloud, "cloud is null");
        this.cloud = cloud;
        checkNotNull(configuration, "configuration is null");
        this.configuration = configuration;
    }

    @Override public Cloud cloud() {
        return cloud;
    }

    @Override public Configuration configuration() {
        return configuration;
    }
}
