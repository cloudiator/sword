/*
 * Copyright (c) 2014-2019 University of Ulm
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

package de.uniulm.cloudiator.sword.drivers.simulation.suppliers;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Supplier;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import de.uniulm.cloudiator.sword.drivers.simulation.config.SimulationConstants.Properties;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavorBuilder;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import java.util.Set;
import javax.inject.Named;

public class HardwareGenerator implements Supplier<Set<HardwareFlavor>> {

  @Named(Properties.HARDWARE_CPU_RANGE)
  @Inject(optional = true)
  String cpuRange = "1 ... 32";

  @Named(Properties.HARDWARE_CPU_MULTI)
  @Inject(optional = true)
  int cpuMulti = 2;

  @Named(Properties.HARDWARE_RAM_RANGE)
  @Inject(optional = true)
  String ramRange = "512 ... 8192";

  @Named(Properties.HARDWARE_RAM_MULTI)
  @Inject(optional = true)
  int ramMulti = 2;

  @Named(Properties.HARDWARE_DISK_RANGE)
  @Inject(optional = true)
  String diskRange = "10 ... 100";

  @Named(Properties.HARDWARE_DISK_STEP)
  @Inject(optional = true)
  int diskStep = 10;


  private static class RangeParser {

    private final String range;

    private RangeParser(String range) {
      this.range = range;
      checkArgument(lowerBound() <= higherBound(), String
          .format("Lower bound %s needs to be smaller or equal higher bound %s.", lowerBound(),
              higherBound()));
    }

    private static RangeParser of(String range) {
      return new RangeParser(range);
    }

    public int lowerBound() {
      return Integer.parseInt(range.split(" ... ")[0]);
    }

    public int higherBound() {
      return Integer.parseInt(range.split(" ... ")[1]);
    }
  }

  private final Supplier<Set<Location>> locationSupplier;

  @Inject
  public HardwareGenerator(
      Supplier<Set<Location>> locationSupplier) {
    this.locationSupplier = locationSupplier;
  }

  private static String generateId(int cores, int ram, int disk) {
    return String.format("Cores:%s,Ram:%s,Disk:%s", cores, ram, disk);
  }

  @Override
  public Set<HardwareFlavor> get() {

    Set<HardwareFlavor> hardwareFlavors = Sets.newHashSet();

    for (Location location : locationSupplier.get()) {

      for (int core = RangeParser.of(cpuRange).lowerBound();
          core <= RangeParser.of(cpuRange).higherBound(); core = core * cpuMulti) {

        for (int ram = RangeParser.of(ramRange).lowerBound();
            ram <= RangeParser.of(ramRange).higherBound(); ram = ram * ramMulti) {

          for (int disk = RangeParser.of(diskRange).lowerBound();
              disk <= RangeParser.of(diskRange).higherBound(); disk = disk + diskStep) {

            final String generateId = generateId(core, ram, disk);
            final HardwareFlavor hardwareFlavor = HardwareFlavorBuilder.newBuilder()
                .id(location.id() + "/" + generateId)
                .providerId(generateId)
                .name(generateId).location(location).cores(core).mbRam(ram).gbDisk((double) disk).build();

            hardwareFlavors.add(hardwareFlavor);

          }

        }


      }
    }
    return hardwareFlavors;
  }
}
