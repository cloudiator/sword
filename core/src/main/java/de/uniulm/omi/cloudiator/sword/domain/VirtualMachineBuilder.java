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

package de.uniulm.omi.cloudiator.sword.domain;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nullable;

/**
 * Created by daniel on 09.12.14.
 */
public class VirtualMachineBuilder {

  private Set<IpAddress> ipAddresses = new HashSet<>();
  @Nullable
  private String id;
  private String providerId;
  @Nullable
  private String name;
  @Nullable
  private Location location;
  @Nullable
  private LoginCredential loginCredential;
  @Nullable
  private Image image;
  @Nullable
  private HardwareFlavor hardwareFlavor;

  private VirtualMachineBuilder() {

  }

  private VirtualMachineBuilder(VirtualMachine virtualMachine) {
    ipAddresses = Sets.newHashSet(virtualMachine.ipAddresses());
    id = virtualMachine.id();
    providerId = virtualMachine.providerId();
    name = virtualMachine.name();
    location = virtualMachine.location().orElse(null);
    loginCredential = virtualMachine.loginCredential().orElse(null);
  }

  public static VirtualMachineBuilder newBuilder() {
    return new VirtualMachineBuilder();
  }

  public static VirtualMachineBuilder of(VirtualMachine virtualMachine) {
    checkNotNull(virtualMachine, "virtualMachine is null");
    return new VirtualMachineBuilder(virtualMachine);
  }

  public VirtualMachineBuilder id(String id) {
    this.id = id;
    return this;
  }

  public VirtualMachineBuilder providerId(String providerId) {
    this.providerId = providerId;
    return this;
  }

  public VirtualMachineBuilder name(String name) {
    this.name = name;
    return this;
  }

  public VirtualMachineBuilder location(Location location) {
    this.location = location;
    return this;
  }

  public VirtualMachineBuilder addIpString(String ip) {
    this.ipAddresses.add(IpAddresses.of(ip));
    return this;
  }

  public VirtualMachineBuilder addIpStrings(Set<String> ips) {
    ips.forEach(this::addIpString);
    return this;
  }

  public VirtualMachineBuilder addIpAddress(IpAddress ipAddress) {
    this.ipAddresses.add(ipAddress);
    return this;
  }

  public VirtualMachineBuilder addIpAddressess(Collection<? extends IpAddress> ipAddresses) {
    this.ipAddresses.addAll(ipAddresses);
    return this;
  }

  public VirtualMachineBuilder loginCredential(final LoginCredential loginCredential) {
    this.loginCredential = loginCredential;
    return this;
  }

  public VirtualMachineBuilder image(final Image image) {
    this.image = image;
    return this;
  }

  public VirtualMachineBuilder hardware(final HardwareFlavor hardwareFlavor) {
    this.hardwareFlavor = hardwareFlavor;
    return this;
  }

  public VirtualMachine build() {
    return new VirtualMachineImpl(id, providerId, name, location, ipAddresses,
        loginCredential, image, hardwareFlavor);
  }
}
