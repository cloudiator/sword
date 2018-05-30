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

package de.uniulm.omi.cloudiator.sword.domain;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;
import de.uniulm.omi.cloudiator.sword.domain.IpAddress.IpAddressType;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

/**
 * Basic implementation of the {@link VirtualMachine} interface.
 */
public class VirtualMachineImpl extends ResourceImpl implements VirtualMachine {


  private final Set<IpAddress> ipAddresses;
  @Nullable
  private final LoginCredential loginCredential;
  @Nullable
  private final Image image;
  @Nullable
  private final HardwareFlavor hardwareFlavor;

  VirtualMachineImpl(String id, String providerId, String name, @Nullable Location location,
      Set<IpAddress> ipAddresses,
      @Nullable LoginCredential loginCredential, @Nullable Image image,
      @Nullable HardwareFlavor hardwareFlavor) {
    super(id, providerId, name, location);

    checkNotNull(ipAddresses);

    this.ipAddresses = ImmutableSet.copyOf(ipAddresses);
    this.loginCredential = loginCredential;
    this.image = image;
    this.hardwareFlavor = hardwareFlavor;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("id", id()).add("providerId", providerId())
        .add("name", name()).add("loginCredential", loginCredential)
        .add("ipAddresses", Arrays.toString(ipAddresses.toArray()))
        .add("image", image).add("hardwareFlavor", hardwareFlavor).toString();
  }

  @Override
  public Set<IpAddress> ipAddresses() {
    return ipAddresses;
  }

  @Override
  public Set<IpAddress> publicIpAddresses() {
    return ipAddresses.stream().filter(ipAddress -> ipAddress.type().equals(IpAddressType.PUBLIC))
        .collect(
            Collectors.toSet());
  }

  @Override
  public Set<IpAddress> privateIpAddresses() {
    return ipAddresses.stream().filter(ipAddress -> ipAddress.type().equals(IpAddressType.PRIVATE))
        .collect(
            Collectors.toSet());
  }

  @Override
  public Optional<Image> image() {
    return Optional.ofNullable(image);
  }

  @Override
  public Optional<String> imageId() {
    if (image == null) {
      return Optional.empty();
    }
    return Optional.of(image.id());
  }

  @Override
  public Optional<HardwareFlavor> hardware() {
    return Optional.ofNullable(hardwareFlavor);
  }

  @Override
  public Optional<String> hardwareId() {
    if (hardwareFlavor == null) {
      return Optional.empty();
    }
    return Optional.of(hardwareFlavor.id());
  }

  @Override
  public Optional<LoginCredential> loginCredential() {
    return Optional.ofNullable(loginCredential);
  }

  @Override
  public State state() {
    //todo implement state
    throw new UnsupportedOperationException("VirtualMachine state not implemented yet.");
  }
}
