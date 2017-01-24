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

package de.uniulm.omi.cloudiator.sword.drivers.openstack.converters;


import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.sword.core.domain.KeyPairBuilder;
import de.uniulm.omi.cloudiator.sword.core.util.IdScopeByLocations;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.domain.KeyPairInRegion;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkState;

/**
 * Created by daniel on 19.05.15.
 */
public class NovaKeyPairToKeypair
    implements OneWayConverter<KeyPairInRegion, de.uniulm.omi.cloudiator.sword.api.domain.KeyPair> {

    @Nullable @Override public de.uniulm.omi.cloudiator.sword.api.domain.KeyPair apply(
        @Nullable KeyPairInRegion keyPair) {
        if (keyPair == null) {
            return null;
        }
        checkState(keyPair.location().isPresent(), String
            .format("Expected keyPair %s to have a location, but location is not present.",
                keyPair));
        return KeyPairBuilder.newBuilder().location(keyPair.location().orElse(null))
            .id(IdScopeByLocations.from(keyPair.location().get().id(), keyPair.getName())
                .getIdWithLocation()).providerId(keyPair.getName()).name(keyPair.getName())
            .privateKey(keyPair.getPrivateKey()).publicKey(keyPair.getPublicKey()).build();
    }
}
