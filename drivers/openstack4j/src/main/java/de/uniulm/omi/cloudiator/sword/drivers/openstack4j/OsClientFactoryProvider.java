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
                KEYSTONE_VERSION.fromEndpoint(serviceConfiguration.getEndpoint().get());
        }
        checkState(keystoneVersion != null, String.format(
            "Unable to resolve keystone version to use. Please configure %s to one of the following possible values: %s",
            Openstack4JConstants.KEYSTONE_VERSION, Arrays.toString(KEYSTONE_VERSION.values())));
        return injector.getInstance(keystoneVersion.clientFactoryClass());
    }

    private enum KEYSTONE_VERSION {
        V2("v2", OsClientV2Factory.class), V3("v3", OsClientV3Factory.class);

        private final String keyWord;
        private final Class<? extends OsClientFactory> clientFactoryClass;

        KEYSTONE_VERSION(String keyWord, Class<? extends OsClientFactory> clientFactoryClass) {
            this.keyWord = keyWord;
            this.clientFactoryClass = clientFactoryClass;
        }

        public static KEYSTONE_VERSION fromEndpoint(String endpoint) {
            checkNotNull(endpoint, "endpoint is null.");
            for (KEYSTONE_VERSION keystoneVersion : values()) {
                if (endpoint.contains(keystoneVersion.keyWord)) {
                    return keystoneVersion;
                }
            }
            return null;
        }

        public Class<? extends OsClientFactory> clientFactoryClass() {
            return clientFactoryClass;
        }
    }


    @Inject(optional = true) @Named(Openstack4JConstants.KEYSTONE_VERSION) private KEYSTONE_VERSION
        keystoneVersion;

    @Inject
    public OsClientFactoryProvider(Injector injector, ServiceConfiguration serviceConfiguration) {
        checkNotNull(serviceConfiguration, "serviceConfiguration is null");
        checkNotNull(injector, "injector is null");
        this.serviceConfiguration = serviceConfiguration;
        this.injector = injector;
    }
}
