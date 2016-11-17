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
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import de.uniulm.omi.cloudiator.sword.api.ServiceConfiguration;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Created by daniel on 17.11.16.
 */
public class OsClientFactoryProvider implements Provider<OsClientFactory> {

    private final ServiceConfiguration serviceConfiguration;
    private final Injector injector;

    @Override public OsClientFactory get() {
        if (keystoneVersion == null) {
            checkState(serviceConfiguration.getEndpoint().isPresent(), "Endpoint is mandatory.");
            keystoneVersion =
                KeyStoneVersion.fromEndpoint(serviceConfiguration.getEndpoint().get());
        }
        checkState(keystoneVersion != null, String.format(
            "Unable to resolve keystone version to use. Please configure %s to one of the following possible values: %s",
            Openstack4JConstants.KEYSTONE_VERSION, Arrays.toString(KeyStoneVersion.values())));
        return injector.getInstance(keystoneVersion.clientFactoryClass());
    }
    
    @Inject(optional = true) @Named(Openstack4JConstants.KEYSTONE_VERSION) private KeyStoneVersion
        keystoneVersion;

    @Inject
    public OsClientFactoryProvider(Injector injector, ServiceConfiguration serviceConfiguration) {
        checkNotNull(serviceConfiguration, "serviceConfiguration is null");
        checkNotNull(injector, "injector is null");
        this.serviceConfiguration = serviceConfiguration;
        this.injector = injector;
    }
}
