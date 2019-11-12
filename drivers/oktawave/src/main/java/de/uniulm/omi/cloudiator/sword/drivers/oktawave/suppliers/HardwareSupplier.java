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

package de.uniulm.omi.cloudiator.sword.drivers.oktawave.suppliers;

import com.google.common.base.Supplier;
import com.google.inject.Inject;
import com.oktawave.api.client.handler.ApiException;
import com.oktawave.api.client.handler.OciApi;
import com.oktawave.api.client.model.InstanceType;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class HardwareSupplier implements Supplier<Set<HardwareFlavor>> {

    private static Logger LOGGER = LoggerFactory.getLogger(HardwareSupplier.class);

    private final OciApi ociApi;
    private final OneWayConverter<InstanceType, HardwareFlavor> hardwareConverter;


    @Inject
    public HardwareSupplier(OciApi ociApi,
                            OneWayConverter<InstanceType, HardwareFlavor> hardwareConverter) {
        this.ociApi = checkNotNull(ociApi, "ociApi is null");
        this.hardwareConverter = checkNotNull(hardwareConverter, "hardwareConverter is null");

    }

    @Override
    public Set<HardwareFlavor> get() {
        Set<InstanceType> instanceTypes = new HashSet<>();

        try {
            instanceTypes = new HashSet<>(ociApi.instancesGetInstancesTypes(null, null, null, null, null, null)
                    .getItems());
        } catch (ApiException e) {
            LOGGER.error("Could not get InstanceType from Oktawave", e);
        }

        return instanceTypes.stream()
                .map(hardwareConverter)
                .collect(Collectors.toSet());
    }
}
