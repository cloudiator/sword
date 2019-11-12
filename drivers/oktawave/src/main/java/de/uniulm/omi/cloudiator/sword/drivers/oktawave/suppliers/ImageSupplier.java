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
import com.oktawave.api.client.handler.ApiClient;
import com.oktawave.api.client.handler.ApiException;
import com.oktawave.api.client.handler.OciTemplatesApi;
import com.oktawave.api.client.handler.auth.OAuth;
import com.oktawave.api.client.model.Template;
import de.uniulm.omi.cloudiator.sword.domain.Image;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ImageSupplier implements Supplier<Set<Image>> {

  private static Logger LOGGER = LoggerFactory.getLogger(HardwareSupplier.class);

  private final ApiClient apiClient;
  private final OciTemplatesApi ociTemplatesApi;
  private final OneWayConverter<Template, Image> templateConverter;

  @Inject
  public ImageSupplier(ApiClient apiClient, OciTemplatesApi ociTemplatesApi, OneWayConverter<Template, Image> templateConverter) {
    this.apiClient = apiClient;
    this.ociTemplatesApi = ociTemplatesApi;
    this.templateConverter = templateConverter;
  }

  @Override
  public Set<Image> get() {
    Set<Template> templates = new HashSet<>();

    try {
      templates = new HashSet<>(ociTemplatesApi.templatesGet("All", null, null, null, null, null)
              .getItems());
    } catch (ApiException e) {
      LOGGER.error("Could not get Template from Oktawave", e);
    }

    return templates
            .stream()
            .map(templateConverter)
            .collect(Collectors.toSet());
  }
}
