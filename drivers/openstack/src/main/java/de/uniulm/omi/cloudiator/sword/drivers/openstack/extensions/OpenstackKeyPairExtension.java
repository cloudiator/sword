/*
 * Copyright (c) 2014-2018 University of Ulm
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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.domain.LocationScope;
import de.uniulm.omi.cloudiator.sword.domain.KeyPair;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.domain.KeyPairInRegion;
import de.uniulm.omi.cloudiator.sword.extensions.KeyPairExtension;
import de.uniulm.omi.cloudiator.sword.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.sword.util.IdScopeByLocations;
import de.uniulm.omi.cloudiator.sword.util.IdScopedByLocation;
import de.uniulm.omi.cloudiator.sword.util.LocationHierarchy;
import de.uniulm.omi.cloudiator.sword.util.NamingStrategy;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import javax.annotation.Nullable;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.extensions.KeyPairApi;

/**
 * Implementation of the {@link KeyPairExtension} interface for Openstack.
 */
@Deprecated
public class OpenstackKeyPairExtension implements KeyPairExtension {

  private final OneWayConverter<KeyPairInRegion, KeyPair> keyPairConverter;
  private final NamingStrategy namingStrategy;
  private final NovaApi novaApi;
  private final GetStrategy<String, Location> locationGetStrategy;


  /**
   * Constructor.
   *
   * @param keyPairConverter a converter for converting {@link org.jclouds.openstack.nova.v2_0.domain.KeyPair}
   * objects to {@link KeyPair} objects (non null).
   * @param namingStrategy the naming strategy used for creating the key pair names.
   * @throws NullPointerException if any of the supplied arguments is null.
   */
  @Inject
  public OpenstackKeyPairExtension(
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
        LocationHierarchy.of(location).firstParentLocationWithScope(LocationScope.REGION)
            .orElseThrow(() -> new IllegalStateException(
                String.format("Could not find parent region of location %s", location)));

    final Optional<KeyPairApi> keyPairApi = novaApi.getKeyPairApi(region.providerId());
    checkState(keyPairApi.isPresent(),
        String.format("KeyPairApi not present in region %s", region));
    return keyPairApi.get();
  }

  @Override
  public KeyPair create(@Nullable String name, String locationId) {
    if (name != null) {
      checkArgument(!name.isEmpty());
    }
    return keyPairConverter.apply(new KeyPairInRegion(
        getKeyPairApi(locationId).create(namingStrategy.generateUniqueNameBasedOnName(name)),
        locationGetStrategy.get(locationId)));
  }

  @Override
  public KeyPair create(@Nullable String name, String publicKey, String locationId) {
    if (name != null) {
      checkArgument(!name.isEmpty());
    }
    checkNotNull(publicKey);
    checkArgument(!publicKey.isEmpty());
    return keyPairConverter.apply(new KeyPairInRegion(getKeyPairApi(locationId)
        .createWithPublicKey(namingStrategy.generateUniqueNameBasedOnName(name), publicKey),
        locationGetStrategy.get(locationId)));
  }

  @Override
  public boolean delete(String id) {
    checkNotNull(id);
    checkArgument(!id.isEmpty());
    return getKeyPairApi(IdScopeByLocations.from(id).getLocationId())
        .delete(IdScopeByLocations.from(id).getId());
  }

  @Override
  public KeyPair get(String id) {
    checkNotNull(id);
    checkArgument(!id.isEmpty());
    final IdScopedByLocation scopedId = IdScopeByLocations.from(id);
    return keyPairConverter.apply(
        new KeyPairInRegion(getKeyPairApi(scopedId.getLocationId()).get(scopedId.getId()),
            locationGetStrategy.get(scopedId.getLocationId())));
  }
}
