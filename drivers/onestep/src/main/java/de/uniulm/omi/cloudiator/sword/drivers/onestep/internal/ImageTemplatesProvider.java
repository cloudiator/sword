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
import client.model.Region;
import client.model.template_response.OperatingSystem;
import com.google.inject.Inject;
import com.google.inject.Provider;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.domain.ImageTemplate;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.domain.ImageTemplatesSet;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.domain.ActiveRegionsSet;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.suppliers.HardwareSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class ImageTemplatesProvider implements Provider<ImageTemplatesSet> {

    private static Logger LOGGER = LoggerFactory.getLogger(HardwareSupplier.class);

    private final ApiClient apiClient;
    private final TemplatesApi templatesApi;
    private final ActiveRegionsSet activeRegionsSet;

    //Note that we need location get strategy as template requests require active regions only
    @Inject
    public ImageTemplatesProvider(ApiClient apiClient, TemplatesApi templatesApi,
                                  ActiveRegionsSet activeRegionsSet) {
        this.apiClient = apiClient;
        apiClient.setBasePath("https://staging.onestep.cloud/api/");
        this.templatesApi = templatesApi;
        this.activeRegionsSet = activeRegionsSet;
    }

    @Override
    public ImageTemplatesSet get() {
        Set<OperatingSystem> operatingSystems = new HashSet<>();

        for (Region region : activeRegionsSet.getRegions()) {
            try {
                operatingSystems.addAll(templatesApi.templatesGet(apiClient.getCurrentWorkspace(), region.getId())
                        .getOperatingSystems());
            } catch (ApiException e) {
                LOGGER.error("Could not get Image Template from One Step Cloud", e);
            }
        }

        return new ImageTemplatesSet(operatingSystemsToImageTemplate(operatingSystems));
    }

    //This casting is necessary as from each operating system we can build multiple images
    private Set<ImageTemplate> operatingSystemsToImageTemplate(Set<OperatingSystem> operatingSystems) {
        Set<ImageTemplate> imageTemplates = new HashSet<>();

        operatingSystems.forEach(
                operatingSystem -> operatingSystem.getVersions().forEach(
                        version -> version.getClusters().forEach(
                                cluster -> imageTemplates.add(
                                        new ImageTemplate(operatingSystem.getId(), operatingSystem.getName(), version.getName(), cluster)))));

        return imageTemplates;
    }

}
