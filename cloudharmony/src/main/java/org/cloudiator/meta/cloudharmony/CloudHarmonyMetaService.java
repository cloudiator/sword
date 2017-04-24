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

package org.cloudiator.meta.cloudharmony;

import de.uniulm.omi.cloudiator.sword.base.MetaService;
import de.uniulm.omi.cloudiator.sword.config.DefaultMetaModule;
import de.uniulm.omi.cloudiator.sword.domain.*;
import io.github.cloudiator.cloudharmony.ApiClient;
import io.github.cloudiator.cloudharmony.ApiException;
import io.github.cloudiator.cloudharmony.api.ApiApi;
import io.github.cloudiator.cloudharmony.model.CloudService;
import io.github.cloudiator.cloudharmony.model.ComputeInstanceType;

import java.util.Optional;

/**
 * Created by daniel on 09.03.17.
 */
public class CloudHarmonyMetaService implements MetaService {

    public static class CloudHarmonyMetaServiceFactory implements MetaService.MetaServiceFactory {
        @Override public MetaService of(Cloud cloud) {
            try {
                return new CloudHarmonyMetaService(
                    new ProviderToCloudHarmony().apply(cloud.api().providerName()));
            } catch (Exception e) {
                return new DefaultMetaModule.NoOpMetaService();
            }
        }
    }


    private static final ApiClient API_CLIENT = new ApiClient();
    private static final ApiApi API_API = new ApiApi(API_CLIENT);

    private final String computeService;

    private CloudHarmonyMetaService(String computeService) {
        this.computeService = computeService;
    }

    private CloudService getCloudService() {
        try {
            return API_API.getService(computeService, null);
        } catch (ApiException e) {
            throw new IllegalStateException("Could not retrieve cloud service", e);
        }
    }

    private ComputeInstanceType getComputeInstanceType(String hardwareName) {
        try {
            return API_API.getComputeInstanceType(computeService, hardwareName, null, null, null);
        } catch (ApiException e) {
            throw new IllegalStateException("Could not retrieve computeInstanceType");
        }
    }

    @Override public Optional<GeoLocation> geoLocation(Location location) {
        return getCloudService().getRegions().stream()
            .filter(serviceRegion -> serviceRegion.getProviderCode().equals(location.providerId()))
            .findFirst().map(
                serviceRegion -> GeoLocationBuilder.newBuilder().city(serviceRegion.getCity())
                    .country(serviceRegion.getCountry().toString())
                    .latitude(serviceRegion.getLocationLat())
                    .longitude(serviceRegion.getLocationLong()).build());

    }

    @Override public Optional<PriceModel> priceModel(HardwareFlavor hardwareFlavor) {
        ComputeInstanceType computeInstanceType = getComputeInstanceType(hardwareFlavor.name());

        return Optional.empty();
    }
}
