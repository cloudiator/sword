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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.extensions;

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.api.domain.KeyPair;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.LocationScope;
import de.uniulm.omi.cloudiator.sword.api.extensions.KeyPairService;
import de.uniulm.omi.cloudiator.sword.api.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.sword.api.util.NamingStrategy;
import de.uniulm.omi.cloudiator.sword.core.domain.KeyPairBuilder;
import de.uniulm.omi.cloudiator.sword.core.util.LocationHierarchy;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.Keypair;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.*;

/**
 * Created by daniel on 30.11.16.
 */
public class Openstack4JKeyPairService implements KeyPairService {

    private final OSClient osClient;
    private final NamingStrategy namingStrategy;
    private final GetStrategy<String, Location> locationGetStrategy;

    @Inject public Openstack4JKeyPairService(OSClient osClient, NamingStrategy namingStrategy,
        GetStrategy<String, Location> locationGetStrategy) {
        checkNotNull(locationGetStrategy, "locationGetStrategy is null");
        this.locationGetStrategy = locationGetStrategy;
        checkNotNull(namingStrategy, "namingStrategy is null");
        this.namingStrategy = namingStrategy;

        checkNotNull(osClient, "osClient is null");
        this.osClient = osClient;
    }

    private Location getRegion(String locationId) {
        Location location = locationGetStrategy.get(locationId);
        checkState(location != null, "Did not find location " + locationId);

        return LocationHierarchy.of(location).firstLocationWithScope(LocationScope.REGION)
            .orElseThrow(() -> new IllegalStateException(
                String.format("Could not find parent region in location %s", location)));
    }

    @Override public KeyPair create(@Nullable String name, String location) {
        return this.create(name, null, location);
    }

    @Override
    public KeyPair create(@Nullable String name, @Nullable String publicKey, String location) {

        checkNotNull(location, "location is null");
        if (name != null) {
            checkArgument(!name.isEmpty(), "name is empty");
        }
        if (publicKey != null) {
            checkArgument(!publicKey.isEmpty(), "publicKey is empty");
        }

        final Location region = getRegion(location);
        Keypair osKeyPair = osClient.useRegion(region.id()).compute().keypairs()
            .create(namingStrategy.generateUniqueNameBasedOnName(name), publicKey);

        checkNotNull(osKeyPair, "keyPair was not generated.");
        checkState(osKeyPair.getPrivateKey() != null && publicKey == null,
            "privateKeyPair not present in generated keyPair (when no public key is supplied)");
        return KeyPairBuilder.newBuilder().id(osKeyPair.getName()).providerId(osKeyPair.getName())
            .location(region).publicKey(osKeyPair.getPublicKey())
            .privateKey(osKeyPair.getPrivateKey()).name(osKeyPair.getName()).build();
    }

    @Override public boolean delete(String name, String location) {
        checkNotNull(name, "name is null");
        checkNotNull(location, "location is null");
        return osClient.useRegion(getRegion(location).id()).compute().keypairs().delete(name)
            .isSuccess();
    }

    @Nullable @Override public KeyPair get(String name, String location) {
        final Keypair retrieved =
            osClient.useRegion(getRegion(location).id()).compute().keypairs().get(name);
        return KeyPairBuilder.newBuilder().location(getRegion(location)).id(retrieved.getName())
            .providerId(retrieved.getName()).name(retrieved.getName()).build();
    }
}
