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

package de.uniulm.omi.cloudiator.sword.drivers.openstack.domain;

import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.domain.LocationScoped;
import java.util.Optional;
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.openstack.nova.v2_0.domain.KeyPair;

/**
 * Created by daniel on 30.11.16.
 */
@Deprecated
public class KeyPairInRegion extends KeyPair implements LocationScoped {

  private final KeyPair delegate;
  private final Location region;

  public KeyPairInRegion(KeyPair delegate, Location region) {
    super(delegate.getPublicKey(), delegate.getPrivateKey(), delegate.getUserId(),
        delegate.getName(), delegate.getFingerprint());
    this.delegate = delegate;
    this.region = region;
  }

  @Override
  @Nullable
  public String getPublicKey() {
    return delegate.getPublicKey();
  }

  @Override
  @Nullable
  public String getPrivateKey() {
    return delegate.getPrivateKey();
  }

  @Override
  @Nullable
  public String getUserId() {
    return delegate.getUserId();
  }

  @Override
  public String getName() {
    return delegate.getName();
  }

  @Override
  @Nullable
  public String getFingerprint() {
    return delegate.getFingerprint();
  }

  @Override
  public Optional<Location> location() {
    return Optional.of(region);
  }
}
