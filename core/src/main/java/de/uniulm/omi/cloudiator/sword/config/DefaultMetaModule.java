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

package de.uniulm.omi.cloudiator.sword.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import de.uniulm.omi.cloudiator.sword.base.MetaService;
import de.uniulm.omi.cloudiator.sword.domain.*;

import java.util.Optional;

/**
 * Created by daniel on 09.03.17.
 */
public class DefaultMetaModule extends AbstractModule {

    @Override protected void configure() {
        bind(MetaService.MetaServiceFactory.class).to(overrideMetaServiceFactory());
    }

    @Provides @Singleton
    final MetaService providesMetaService(MetaService.MetaServiceFactory metaServiceFactory,
        Cloud cloud) {
        return metaServiceFactory.of(cloud);
    }

    protected Class<? extends MetaService.MetaServiceFactory> overrideMetaServiceFactory() {
        return NoOpMetaService.class;
    }

    public static class NoOpMetaService implements MetaService, MetaService.MetaServiceFactory {

        @Override public MetaService of(Cloud cloud) {
            return new NoOpMetaService();
        }

        @Override public Optional<PriceModel> priceModel(HardwareFlavor hardwareFlavor) {
            return Optional.empty();
        }

        @Override public Optional<GeoLocation> geoLocation(Location location) {
            return Optional.empty();
        }
    }
}
