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
import de.uniulm.omi.cloudiator.domain.LocationScope;
import de.uniulm.omi.cloudiator.sword.domain.KeyPair;
import de.uniulm.omi.cloudiator.sword.domain.KeyPairBuilder;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.extensions.KeyPairExtension;
import de.uniulm.omi.cloudiator.sword.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.sword.util.IdScopeByLocations;
import de.uniulm.omi.cloudiator.sword.util.IdScopedByLocation;
import de.uniulm.omi.cloudiator.sword.util.LocationHierarchy;
import de.uniulm.omi.cloudiator.sword.util.NamingStrategy;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.Keypair;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.*;

/**
 * Created by daniel on 30.11.16.
 */
public class Openstack4JKeyPairExtension implements KeyPairExtension {

    private final OSClient osClient;
    private final NamingStrategy namingStrategy;
    private final GetStrategy<String, Location> locationGetStrategy;

    @Inject public Openstack4JKeyPairExtension(OSClient osClient, NamingStrategy namingStrategy,
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

        return LocationHierarchy.of(location).firstParentLocationWithScope(LocationScope.REGION)
            .orElseThrow(() -> new IllegalStateException(
                String.format("Could not find parent region in location %s", location)));
    }

    @Override public KeyPair create(@Nullable String name, String locationId) {
        return this.create(name, null, locationId);
    }

    @Override
    public KeyPair create(@Nullable String name, @Nullable String publicKey, String locationId) {

        checkNotNull(locationId, "location is null");
        if (name != null) {
            checkArgument(!name.isEmpty(), "name is empty");
        }
        if (publicKey != null) {
            checkArgument(!publicKey.isEmpty(), "publicKey is empty");
        }

        final Location region = getRegion(locationId);
        Keypair osKeyPair = osClient.useRegion(region.id()).compute().keypairs()
            .create(namingStrategy.generateUniqueNameBasedOnName(name), publicKey);

        checkNotNull(osKeyPair, "keyPair was not generated.");
        checkState(osKeyPair.getPrivateKey() != null && publicKey == null,
            "privateKeyPair not present in generated keyPair (when no public key is supplied)");
        return KeyPairBuilder.newBuilder()
            .id(IdScopeByLocations.from(region.id(), osKeyPair.getName()).getIdWithLocation())
            .providerId(osKeyPair.getName()).location(region).publicKey(osKeyPair.getPublicKey())
            .privateKey(osKeyPair.getPrivateKey()).name(osKeyPair.getName()).build();
    }

    @Override public boolean delete(String id) {
        checkNotNull(id, "id is null");
        checkArgument(!id.isEmpty(), "id is empty");
        final IdScopedByLocation scopedId = IdScopeByLocations.from(id);
        return osClient.useRegion(getRegion(scopedId.getLocationId()).id()).compute().keypairs()
            .delete(scopedId.getId()).isSuccess();
    }

    @Nullable @Override public KeyPair get(String id) {
        checkNotNull(id);
        checkArgument(!id.isEmpty());
        final IdScopedByLocation scopedId = IdScopeByLocations.from(id);
        final Keypair retrieved =
            osClient.useRegion(getRegion(scopedId.getLocationId()).id()).compute().keypairs()
                .get(scopedId.getId());
        return KeyPairBuilder.newBuilder().location(getRegion(scopedId.getLocationId()))
            .id(scopedId.getIdWithLocation()).providerId(retrieved.getName())
            .name(retrieved.getName()).build();
    }
}
