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

package de.uniulm.omi.cloudiator.sword.onestep.suppliers;

import client.ApiException;
import client.api.TemplatesApi;
import client.model.template_response.OperatingSystem;
import com.google.common.base.Supplier;
import com.google.inject.Inject;
import com.oktawave.api.client.ApiClient;
import de.uniulm.omi.cloudiator.sword.domain.Image;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.onestep.domain.ImageTemplate;
import de.uniulm.omi.cloudiator.sword.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ImageSupplier implements Supplier<Set<Image>> {

  private static Logger LOGGER = LoggerFactory.getLogger(HardwareSupplier.class);

  private final ApiClient apiClient;
  private final TemplatesApi templatesApi;
  private final OneWayConverter<ImageTemplate, Image> templateConverter;

  //Note that we need location get strategy as template requests require active regions only
  @Inject
  public ImageSupplier(ApiClient apiClient, TemplatesApi templatesApi,
                       OneWayConverter<ImageTemplate, Image> templateConverter,
                       GetStrategy<String, Location> locationGetStrategy) {
    this.apiClient = apiClient;
    apiClient.setBasePath("https://panel.onestep.cloud/api/virtual_machines/");
    this.templatesApi = templatesApi;
    this.templateConverter = templateConverter;
  }

  @Override
  public Set<Image> get() {
    List<OperatingSystem> operatingSystems = new ArrayList<>();

    try {
      operatingSystems = templatesApi.templatesGet("All", null, null, null, null, null)
              .getOperatingSystems();
    } catch (ApiException e) {
      LOGGER.error("Could not get Image Template from One Step Cloud", e);
    }

    return operatingSystemsToImageTemplate(operatingSystems)
            .stream()
            .map(templateConverter)
            .collect(Collectors.toSet());
  }

  //This casting is necessary as from each operating system we can build multiple images
  private Set<ImageTemplate> operatingSystemsToImageTemplate(List<OperatingSystem> operatingSystems) {
    Set<ImageTemplate> imageTemplates = new HashSet<>();

    operatingSystems.forEach(
            operatingSystem -> operatingSystem.getVersions().forEach(
                    version -> version.getClusters().forEach(
                            cluster -> imageTemplates.add(
                                    new ImageTemplate(operatingSystem.getId(), operatingSystem.getName(), version.getName(), cluster)))));

    return imageTemplates;
  }

}
