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
import client.api.ApiClient;
import client.api.TemplatesApi;
import client.model.regions.Region;
import client.model.templates.ApiCollectionTemplate;
import client.model.templates.OperatingSystem;
import client.model.templates.PrivateNetwork;
import com.google.inject.Inject;
import com.google.inject.Provider;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.domain.ImageTemplate;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.domain.ImageTemplatesSet;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.domain.ActiveRegionsSet;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.suppliers.HardwareSupplier;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImageTemplatesProvider implements Provider<ImageTemplatesSet> {

    private static Logger LOGGER = LoggerFactory.getLogger(HardwareSupplier.class);

    private final TemplatesApi templatesApi;
    private final ActiveRegionsSet activeRegionsSet;
    private final OneWayConverter<Region, Location> converter;

    //Note that we need location get strategy as template requests require active regions only
    @Inject
    public ImageTemplatesProvider(ApiClient apiClient, TemplatesApi templatesApi,
                                  ActiveRegionsSet activeRegionsSet, OneWayConverter<Region, Location> converter) {
        apiClient.setBasePath("https://panel.onestep.cloud/api/");
        this.templatesApi = templatesApi;
        this.activeRegionsSet = activeRegionsSet;
        this.converter = converter;
    }

    @Override
    public ImageTemplatesSet get() {
        Set<ImageTemplate> imageTemplates = new HashSet<>();
        int workspace = templatesApi.getApiClient().getCurrentWorkspace();

        for (Region region : activeRegionsSet.getRegions()) {
            try {
                ApiCollectionTemplate apiCollectionTemplate = templatesApi.templatesGet(workspace, region.getId());
                imageTemplates.addAll(operatingSystemsToImageTemplate(apiCollectionTemplate.getOperatingSystems(), region));

                //we need to store private network as it will be needed during virtual machine creation
                PrivateNetwork defaultPrivateNetwork = apiCollectionTemplate
                        .getPrivateNetworks()
                        .stream()
                        .filter(e -> e.getName().equals("Default"))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("No default private network was found for region: " + region.getId()));

                templatesApi.getApiClient().addDefaultPrivateNetwork(region.getId(), defaultPrivateNetwork);

            } catch (ApiException e) {
                LOGGER.error("Could not get Image Template from One Step Cloud", e);
            }
        }

        return new ImageTemplatesSet(imageTemplates);
    }

    //This casting is necessary as from each operating system we can build multiple images
    private Set<ImageTemplate> operatingSystemsToImageTemplate(List<OperatingSystem> operatingSystems, Region region) {
        Set<ImageTemplate> imageTemplates = new HashSet<>();

        operatingSystems.forEach(
                operatingSystem -> operatingSystem.getVersions().forEach(
                        version -> version.getClusters().forEach(
                                cluster -> imageTemplates.add(
                                        new ImageTemplate(
                                                operatingSystem.getId(),
                                                operatingSystem.getName(),
                                                version.getId(),
                                                version.getName(),
                                                converter.apply(region),
                                                cluster
                                        )
                                )
                        )
                )
        );

        return imageTemplates;
    }

}
