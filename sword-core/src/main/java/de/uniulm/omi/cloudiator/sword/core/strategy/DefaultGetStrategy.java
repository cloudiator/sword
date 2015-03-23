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

package de.uniulm.omi.cloudiator.sword.core.strategy;

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.api.domain.*;
import de.uniulm.omi.cloudiator.sword.api.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.sword.api.supplier.Supplier;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * Created by daniel on 11.03.15.
 */
public class DefaultGetStrategy<T extends Resource> implements GetStrategy<String, T> {

    private final Supplier<Set<T>> supplier;

    @Inject
    public DefaultGetStrategy(Supplier<Set<T>> supplier) {
        this.supplier = supplier;
    }

    @Nullable
    @Override
    public T get(String s) {
        for (T t : supplier.get()) {
            if (t.id().equals(s)) {
                return t;
            }
        }
        return null;
    }

    public static class DefaultVirtualMachineGetStrategy extends DefaultGetStrategy<VirtualMachine> {
        @Inject
        public DefaultVirtualMachineGetStrategy(Supplier<Set<VirtualMachine>> supplier) {
            super(supplier);
        }
    }

    public static class DefaultImageGetStrategy extends DefaultGetStrategy<Image> {
        @Inject
        public DefaultImageGetStrategy(Supplier<Set<Image>> supplier) {
            super(supplier);
        }
    }

    public static class DefaultLocationGetStrategy extends DefaultGetStrategy<Location> {
        @Inject
        public DefaultLocationGetStrategy(Supplier<Set<Location>> supplier) {
            super(supplier);
        }
    }

    public static class DefaultHardwareFlavorGetStrategy extends DefaultGetStrategy<HardwareFlavor> {
        @Inject
        public DefaultHardwareFlavorGetStrategy(Supplier<Set<HardwareFlavor>> supplier) {
            super(supplier);
        }
    }
}
