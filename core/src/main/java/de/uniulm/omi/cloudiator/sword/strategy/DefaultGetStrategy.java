/*
 * Copyright (c) 2014-2015 University of Ulm
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.uniulm.omi.cloudiator.sword.strategy;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Supplier;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.domain.Identifiable;
import de.uniulm.omi.cloudiator.sword.annotations.Memoized;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.Image;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import java.util.Set;
import javax.annotation.Nullable;


/**
 * A generic get strategy, that uses the supplier and searches for the correct
 * id within the supplier.
 *
 * @param <T> the type of the identifiable to search for.
 * @author Daniel Baur <daniel.baur@uni-ulm.de>
 */
public class DefaultGetStrategy<T extends Identifiable> implements GetStrategy<String, T> {

  private final Supplier<Set<T>> supplier;

  /**
   * Creates a default get strategy.
   *
   * @param supplier the supplier in which to search for the id.
   */
  @Inject
  public DefaultGetStrategy(Supplier<Set<T>> supplier) {
    this.supplier = supplier;
  }

  /**
   * Searches for the given id in the supplier.
   *
   * @param s the id.
   * @return the found resource or null.
   * @throws NullPointerException if the given id is null.
   * @throws IllegalArgumentException if the given id is empty.
   */
  @Nullable
  @Override
  public T get(String s) {
    checkNotNull(s);
    checkArgument(!s.isEmpty());
    for (T t : supplier.get()) {
      if (t.id().equals(s)) {
        return t;
      }
    }
    return null;
  }

  /**
   * A generic get strategy for virtual machines.
   *
   * @todo necessary?
   */
  public static class DefaultVirtualMachineGetStrategy
      extends DefaultGetStrategy<VirtualMachine> {

    @Inject
    public DefaultVirtualMachineGetStrategy(Supplier<Set<VirtualMachine>> supplier) {
      super(supplier);
    }
  }


  /**
   * A generic get strategy for images.
   * * @todo necessary?
   */
  public static class DefaultImageGetStrategy extends DefaultGetStrategy<Image> {

    @Inject
    public DefaultImageGetStrategy(@Memoized Supplier<Set<Image>> supplier) {
      super(supplier);
    }
  }


  /**
   * A generic get strategy for locations.
   * * @todo necessary?
   */
  public static class DefaultLocationGetStrategy extends DefaultGetStrategy<Location> {

    @Inject
    public DefaultLocationGetStrategy(@Memoized Supplier<Set<Location>> supplier) {
      super(supplier);
    }
  }


  /**
   * A generic get strategy for hardware flavors.
   * * @todo necessary?
   */
  public static class DefaultHardwareFlavorGetStrategy
      extends DefaultGetStrategy<HardwareFlavor> {

    @Inject
    public DefaultHardwareFlavorGetStrategy(@Memoized Supplier<Set<HardwareFlavor>> supplier) {
      super(supplier);
    }
  }
}
