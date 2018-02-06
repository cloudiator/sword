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

package org.cloudiator.meta.cloudharmony;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.uniulm.omi.cloudiator.sword.base.MetaService;
import de.uniulm.omi.cloudiator.sword.domain.GeoLocation;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.domain.PriceModel;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class CachedMetaService implements MetaService {

  private final MetaService delegate;
  private static final Cache<String, GeoLocation> GEO_LOCATION_CACHE = CacheBuilder.newBuilder()
      .build();
  private static final Cache<String, PriceModel> PRICE_MODEL_CACHE = CacheBuilder.newBuilder()
      .build();

  private static final Supplier<CacheLoadException> CACHE_LOAD_EXCEPTION_SUPPLIER = new Supplier<CacheLoadException>() {
    @Override
    public CacheLoadException get() {
      return new CacheLoadException("Cache loading faled");
    }
  };

  private static class CacheLoadException extends Exception {

    public CacheLoadException() {
      super();
    }

    public CacheLoadException(String s) {
      super(s);
    }

    public CacheLoadException(String s, Throwable throwable) {
      super(s, throwable);
    }

    public CacheLoadException(Throwable throwable) {
      super(throwable);
    }

    protected CacheLoadException(String s, Throwable throwable, boolean b, boolean b1) {
      super(s, throwable, b, b1);
    }
  }

  public CachedMetaService(MetaService delegate) {
    this.delegate = delegate;
  }

  @Override
  public final Optional<PriceModel> priceModel(HardwareFlavor hardwareFlavor) {
    try {
      return Optional.ofNullable(PRICE_MODEL_CACHE.get(hardwareIdentity().apply(hardwareFlavor),
          () -> delegate.priceModel(hardwareFlavor).orElseThrow(CACHE_LOAD_EXCEPTION_SUPPLIER)));
    } catch (ExecutionException e) {
      //todo: add logging
      return Optional.empty();
    }
  }

  @Override
  public final Optional<GeoLocation> geoLocation(Location location) {
    try {
      return Optional.ofNullable(
          GEO_LOCATION_CACHE.get(locationIdentity().apply(location),
              () -> delegate.geoLocation(location).orElseThrow(CACHE_LOAD_EXCEPTION_SUPPLIER)));
    } catch (ExecutionException e) {
      //todo: add logging
      return Optional.empty();
    }
  }

  protected abstract Function<HardwareFlavor, String> hardwareIdentity();

  protected abstract Function<Location, String> locationIdentity();
}
