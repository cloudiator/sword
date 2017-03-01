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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.internal;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import de.uniulm.omi.cloudiator.sword.domain.Cloud;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Created by daniel on 17.11.16.
 */
public class KeyStoneVersionProvider implements Provider<KeyStoneVersion> {

    @Inject(optional = true) @Named(Openstack4JConstants.KEYSTONE_VERSION) private String
        keystoneVersionConfiguration;
    private final Cloud cloud;

    @Inject public KeyStoneVersionProvider(Cloud cloud) {
        checkNotNull(cloud);
        this.cloud = cloud;
    }

    @Override public KeyStoneVersion get() {

        if (keystoneVersionConfiguration != null) {
            try {
                return KeyStoneVersion.valueOf(keystoneVersionConfiguration);
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException(
                    "Illegal configuration for " + Openstack4JConstants.KEYSTONE_VERSION);
            }
        }

        checkState(cloud.endpoint().isPresent(), "Endpoint is mandatory.");
        return checkNotNull(KeyStoneVersion.fromEndpoint(cloud.endpoint().get()),
            "Unable to resolve keyStone version from endpoint.");
    }
}
