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

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import java.util.Optional;
import javax.annotation.Nullable;

/**
 * Created by daniel on 03.12.14.
 */
public class HardwareFlavorImpl extends ResourceImpl implements HardwareFlavor {

  @Nullable
  private Double gbDisk;
  private int cores;
  private long ram;

  HardwareFlavorImpl(String id, String providerId, String name, @Nullable Location location,
      int cores, long ram, @Nullable Double gbDisk) {
    super(id, providerId, name, location);
    checkArgument(cores > 0, "Cores must be > 0");
    checkArgument(ram > 0, "RAM must be > 0");
    if (gbDisk != null) {
      checkArgument(gbDisk > 0, "Disk must be > 0");
    }
    this.cores = cores;
    this.ram = ram;
    this.gbDisk = gbDisk;
  }

  @Override
  public int numberOfCores() {
    return cores;
  }

  @Override
  public long mbRam() {
    return ram;
  }

  @Nullable
  @Override
  public Optional<Double> gbDisk() {
    return Optional.ofNullable(gbDisk);
  }

  @Override
  protected ToStringHelper toStringHelper() {
    return super.toStringHelper().add("cores", cores).add("ram", ram).add("disk", gbDisk);
  }
}
