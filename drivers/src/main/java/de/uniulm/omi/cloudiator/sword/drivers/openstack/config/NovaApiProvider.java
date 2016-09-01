/*
 * Copyright (c) 2014-2015 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.drivers.openstack.config;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.openstack.nova.v2_0.NovaApi;

/**
 * Created by daniel on 19.05.15.
 */
public class NovaApiProvider implements Provider<NovaApi> {

    private final ComputeServiceContext computeServiceContext;

    @Inject public NovaApiProvider(ComputeServiceContext computeServiceContext) {
        this.computeServiceContext = computeServiceContext;
    }

    @Override public NovaApi get() {
        return computeServiceContext.unwrapApi(NovaApi.class);
    }
}
