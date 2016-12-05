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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.internal;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 18.11.16.
 */
public class RegionSupplierProvider implements Provider<RegionSupplier> {

    private final KeyStoneVersion keyStoneVersion;
    private final Injector injector;

    @Inject public RegionSupplierProvider(KeyStoneVersion keyStoneVersion, Injector injector) {
        checkNotNull(injector, "injector is null");
        this.injector = injector;
        checkNotNull(keyStoneVersion, "keyStoneVersion is null");
        this.keyStoneVersion = keyStoneVersion;
    }

    @Override public RegionSupplier get() {
        final FilteringRegionSupplier filteringRegionSupplier = new FilteringRegionSupplier(
            injector.getInstance(keyStoneVersion.regionSupplierClass()));
        injector.injectMembers(filteringRegionSupplier);
        return new MemoizedRegionSupplier(filteringRegionSupplier);
    }

    private static class MemoizedRegionSupplier implements RegionSupplier {

        private final Supplier<Set<Location>> delegate;

        private MemoizedRegionSupplier(Supplier<Set<Location>> delegate) {
            checkNotNull(delegate, "delegate is null");
            this.delegate = Suppliers.memoize(delegate);
        }

        @Override public Set<Location> get() {
            return delegate.get();
        }
    }
}
