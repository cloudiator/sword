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

package de.uniulm.omi.cloudiator.sword.drivers.openstack.extensions;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.sword.api.domain.KeyPair;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.LocationScope;
import de.uniulm.omi.cloudiator.sword.api.extensions.KeyPairService;
import de.uniulm.omi.cloudiator.sword.api.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.sword.api.util.NamingStrategy;
import de.uniulm.omi.cloudiator.sword.core.util.LocationHierarchy;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.domain.KeyPairInRegion;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.extensions.KeyPairApi;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.*;

/**
 * Implementation of the {@link KeyPairService} interface
 * for Openstack.
 */
public class OpenstackKeyPairService implements KeyPairService {

    private final OneWayConverter<KeyPairInRegion, de.uniulm.omi.cloudiator.sword.api.domain.KeyPair>
        keyPairConverter;
    private final NamingStrategy namingStrategy;
    private final NovaApi novaApi;
    private final GetStrategy<String, Location> locationGetStrategy;


    /**
     * Constructor.
     *
     * @param keyPairConverter    a converter for converting {@link org.jclouds.openstack.nova.v2_0.domain.KeyPair}
     *                            objects to {@link KeyPair} objects (non null).
     * @param namingStrategy      the naming strategy used for creating the key pair names.
     * @param novaApi
     * @param locationGetStrategy
     * @throws NullPointerException if any of the supplied arguments is null.
     */
    @Inject public OpenstackKeyPairService(
        OneWayConverter<KeyPairInRegion, KeyPair> keyPairConverter, NamingStrategy namingStrategy,
        NovaApi novaApi, GetStrategy<String, Location> locationGetStrategy) {

        checkNotNull(locationGetStrategy, "locationGetStrategy is null");
        this.locationGetStrategy = locationGetStrategy;

        checkNotNull(novaApi, "novaApi is null");
        this.novaApi = novaApi;

        checkNotNull(namingStrategy, "namingStrategy is null");
        checkNotNull(keyPairConverter, "keyPairConverter is null");

        this.keyPairConverter = keyPairConverter;
        this.namingStrategy = namingStrategy;
    }

    private KeyPairApi getKeyPairApi(final String locationId) {
        final Location location = locationGetStrategy.get(locationId);
        checkState(location != null, "Did not find location with id" + locationId);

        Location region =
            LocationHierarchy.of(location).firstParentLocationWithScope(LocationScope.REGION).orElseThrow(
                () -> new IllegalStateException(
                    String.format("Could not find parent region of location %s", location)));

        final Optional<KeyPairApi> keyPairApi = novaApi.getKeyPairApi(region.providerId());
        checkState(keyPairApi.isPresent(),
            String.format("KeyPairApi not present in region %s", region));
        return keyPairApi.get();
    }

    @Override public KeyPair create(@Nullable String name, String locationId) {
        if (name != null) {
            checkArgument(!name.isEmpty());
        }
        return keyPairConverter.apply(new KeyPairInRegion(
            getKeyPairApi(locationId).create(namingStrategy.generateUniqueNameBasedOnName(name)),
            locationGetStrategy.get(locationId)));
    }

    @Override public KeyPair create(@Nullable String name, String publicKey, String locationId) {
        if (name != null) {
            checkArgument(!name.isEmpty());
        }
        checkNotNull(publicKey);
        checkArgument(!publicKey.isEmpty());
        return keyPairConverter.apply(new KeyPairInRegion(getKeyPairApi(locationId)
            .createWithPublicKey(namingStrategy.generateUniqueNameBasedOnName(name), publicKey),
            locationGetStrategy.get(locationId)));
    }

    @Override public boolean delete(String name, String locationId) {
        checkNotNull(name);
        checkArgument(!name.isEmpty());
        return getKeyPairApi(locationId).delete(name);
    }

    @Override public KeyPair get(String name, String locationId) {
        checkNotNull(name);
        checkArgument(!name.isEmpty());
        return keyPairConverter.apply(new KeyPairInRegion(getKeyPairApi(locationId).get(name),
            locationGetStrategy.get(locationId)));
    }
}
