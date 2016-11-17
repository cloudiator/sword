/*
 * Copyright (c) 2014-2016 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j;

import com.google.inject.Inject;
import com.google.inject.Provider;
import de.uniulm.omi.cloudiator.sword.api.ServiceConfiguration;
import org.openstack4j.api.OSClient;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 14.11.16.
 */
public class Openstack4jClientProvider implements Provider<OSClient> {

    private final ServiceConfiguration serviceConfiguration;
    private final OsClientFactory osClientFactory;

    @Inject public Openstack4jClientProvider(ServiceConfiguration serviceConfiguration,
        OsClientFactory osClientFactory) {
        checkNotNull(serviceConfiguration, "serviceConfiguration is null.");
        checkNotNull(osClientFactory, "osClientFactory is null");
        this.serviceConfiguration = serviceConfiguration;
        this.osClientFactory = osClientFactory;
    }

    @Override public OSClient get() {
        return osClientFactory.create(serviceConfiguration);
    }
}
