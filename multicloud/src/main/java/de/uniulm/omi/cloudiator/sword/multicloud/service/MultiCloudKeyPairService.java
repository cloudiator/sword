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

import com.google.common.base.Optional;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.api.domain.KeyPair;
import de.uniulm.omi.cloudiator.sword.api.extensions.KeyPairService;
import de.uniulm.omi.cloudiator.sword.multicloud.domain.KeyPairMultiCloudImpl;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.*;

/**
 * Created by daniel on 24.01.17.
 */
public class MultiCloudKeyPairService implements KeyPairService {

    private final ComputeServiceProvider computeServiceProvider;

    @Inject public MultiCloudKeyPairService(ComputeServiceProvider computeServiceProvider) {
        checkNotNull(computeServiceProvider, "computeServiceProvider is null");
        this.computeServiceProvider = computeServiceProvider;
    }

    private KeyPairService keyPairService(String scopedId) {
        final IdScopedByCloud ScopedIdByCloud = IdScopedByClouds.from(scopedId);
        final Optional<KeyPairService> keyPairServiceOptional =
            computeServiceProvider.forId(ScopedIdByCloud.cloudId()).keyPairService();
        checkState(keyPairServiceOptional.isPresent(), String
            .format("KeyPairService is not present for cloud %s.", ScopedIdByCloud.cloudId()));
        return keyPairServiceOptional.get();
    }

    @Override public KeyPair create(@Nullable String name, String locationId) {
        if (name != null) {
            checkArgument(!name.isEmpty(), "name is empty");
        }
        checkNotNull(locationId, "locationId is null");
        return new KeyPairMultiCloudImpl(
            keyPairService(locationId).create(name, IdScopedByClouds.from(locationId).id()),
            IdScopedByClouds.from(locationId).cloudId());
    }

    @Override public KeyPair create(@Nullable String name, String publicKey, String locationId) {
        if (name != null) {
            checkArgument(!name.isEmpty(), "name is empty");
        }
        checkNotNull(publicKey, "publicKey is null");
        checkArgument(!publicKey.isEmpty(), "publicKey is empty");
        checkNotNull(locationId, "locationId is null");
        checkArgument(!locationId.isEmpty(), "locationId is empty");
        return new KeyPairMultiCloudImpl(keyPairService(locationId)
            .create(name, publicKey, IdScopedByClouds.from(locationId).id()),
            IdScopedByClouds.from(locationId).cloudId());
    }

    @Override public boolean delete(String id) {
        checkNotNull(id, "id is null");
        checkArgument(!id.isEmpty(), "id is empty");

        IdScopedByCloud scopedKeyPairId = IdScopedByClouds.from(id);
        return keyPairService(id).delete(scopedKeyPairId.id());
    }

    @Nullable @Override public KeyPair get(String id) {
        checkNotNull(id, "id is null");
        checkArgument(!id.isEmpty(), "id is empty");

        IdScopedByCloud idScopedByCloud = IdScopedByClouds.from(id);

        return new KeyPairMultiCloudImpl(keyPairService(id).get(idScopedByCloud.id()),
            idScopedByCloud.cloudId());
    }
}
