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

import com.google.common.base.MoreObjects;
import javax.annotation.Nullable;

/**
 * Created by daniel on 03.12.14.
 */
public class HardwareFlavorBuilder {

  private String id;
  private String providerId;
  private String name;
  private int cores;
  private long mbRam;
  @Nullable
  private Double gbDisk;
  @Nullable
  private Location location;

  private HardwareFlavorBuilder() {
  }

  private HardwareFlavorBuilder(HardwareFlavor hardwareFlavor) {
    id = hardwareFlavor.id();
    providerId = hardwareFlavor.providerId();
    name = hardwareFlavor.name();
    cores = hardwareFlavor.numberOfCores();
    mbRam = hardwareFlavor.mbRam();
    gbDisk = hardwareFlavor.gbDisk().orElse(null);
    location = hardwareFlavor.location().orElse(null);
  }

  public static HardwareFlavorBuilder newBuilder() {
    return new HardwareFlavorBuilder();
  }

  public static HardwareFlavorBuilder of(HardwareFlavor hardwareFlavor) {
    checkNotNull(hardwareFlavor, "hardwareFlavor is null");
    return new HardwareFlavorBuilder(hardwareFlavor);
  }

  public HardwareFlavorImpl build() {
    return new HardwareFlavorImpl(id, providerId, name, location, cores, mbRam, gbDisk);
  }

  public HardwareFlavorBuilder id(String id) {
    this.id = id;
    return this;
  }

  public HardwareFlavorBuilder providerId(String providerId) {
    this.providerId = providerId;
    return this;
  }

  public HardwareFlavorBuilder name(String name) {
    this.name = name;
    return this;
  }

  public HardwareFlavorBuilder location(Location location) {
    this.location = location;
    return this;
  }

  public HardwareFlavorBuilder cores(int cores) {
    this.cores = cores;
    return this;
  }

  public HardwareFlavorBuilder mbRam(long mbRam) {
    this.mbRam = mbRam;
    return this;
  }

  public HardwareFlavorBuilder gbDisk(@Nullable Double gbDisk) {
    this.gbDisk = gbDisk;
    return this;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).toString();
  }
}
