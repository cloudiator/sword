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

package de.uniulm.omi.cloudiator.sword.drivers.onestep.internal;

import client.ApiException;
import client.api.RegionsApi;
import client.model.regions.Region;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.domain.ActiveRegionsSet;
import de.uniulm.omi.cloudiator.sword.properties.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class ActiveRegionsProvider implements Provider<ActiveRegionsSet> {

    private static Logger LOGGER = LoggerFactory.getLogger(ActiveRegionsProvider.class);

    private final RegionsApi regionsApi;

    @Inject(optional = true)
    @Named(Constants.SWORD_REGIONS)
    private String regionFilter = null;
    private Set<String> allowedRegions = null;

    @Inject
    public ActiveRegionsProvider(RegionsApi regionsApi) {
        this.regionsApi = checkNotNull(regionsApi, "regionsApi is null");
    }

    private Set<String> getAllowedRegions() {
        if (regionFilter != null && allowedRegions == null) {
            allowedRegions = Arrays.stream(regionFilter.split("[,;]")).collect(Collectors.toSet());
        }
        return allowedRegions;
    }

    @Override
    public ActiveRegionsSet get() {

        try {
            Set<String> filter = getAllowedRegions();
            return new ActiveRegionsSet(
                    regionsApi.regionsGet(null)
                            .getRegions()
                            .stream()
                            .filter(Region::isIsActive)
                            .filter(region -> filter == null || filter.contains(region.getName()))
                            .collect(Collectors.toSet())
            );
        } catch (ApiException e) {
            LOGGER.error("Could not get regions from OneStep", e);
        }

        return new ActiveRegionsSet(new HashSet<>());
    }
}
