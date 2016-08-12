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

import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.sword.api.domain.PricingService;
import de.uniulm.omi.cloudiator.sword.api.extensions.ComputeInstancePrice;
import io.github.cloudiator.cloudharmony.ApiClient;
import io.github.cloudiator.cloudharmony.ApiException;
import io.github.cloudiator.cloudharmony.api.ApiApi;
import io.github.cloudiator.cloudharmony.model.ComputeInstanceType;
import io.github.cloudiator.cloudharmony.model.ComputeInstanceTypePrice;
import io.github.cloudiator.cloudharmony.model.ComputeProperties;
import io.github.cloudiator.cloudharmony.model.IDList;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 10.08.16.
 */
public class CloudHarmonyPriceService implements PricingService {

    private final ApiApi api;
    private final String service;
    private final OneWayConverter<ComputeInstanceTypePrice, ComputeInstancePrice>
        computePriceConverter;

    public CloudHarmonyPriceService(String provider) {

        checkNotNull(provider);

        this.computePriceConverter = new ComputeInstanceTypePriceToComputeInstancePrice();

        final ApiClient apiClient = new ApiClient();
        api = new ApiApi(apiClient);

        try {
            final IDList services =
                api.getServices(null, Collections.emptyList(), Collections.emptyList());
            checkArgument(services.getIds().contains(provider), String
                .format("Provider %s is not supported. Supported providers are %s.", provider,
                    Arrays.toString(services.getIds().toArray())));
            this.service = provider;
        } catch (ApiException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override public Set<ComputeInstancePrice> retrievePricing(String hardware) {

        checkNotNull(hardware);

        try {
            final List<ComputeProperties> computeProperties =
                api.getComputeProperties(service, null, null);
            checkArgument(computeProperties.stream()
                    .filter(properties -> properties.getInstanceTypes().contains(hardware)).findAny()
                    .isPresent(), "Hardware %s not found. Supported hardware is %s", hardware,
                computeProperties.stream().flatMap(
                    (Function<ComputeProperties, Stream<?>>) properties -> properties
                        .getInstanceTypes().stream()).collect(Collectors.toSet()));


            final ComputeInstanceType computeInstanceType =
                api.getComputeInstanceType(service, hardware, null, Collections.emptyList(),
                    Collections.emptyList());

            return computeInstanceType.getPricing().stream().map(computePriceConverter)
                .collect(Collectors.toSet());

        } catch (ApiException e) {
            throw new IllegalStateException(e);
        }
    }

}
