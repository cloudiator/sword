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

package de.uniulm.omi.cloudiator.sword.domain;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;

/**
 * Created by daniel on 09.01.15.
 */
public class VirtualMachineTemplateBuilder {

  @Nullable
  private String name;
  private String imageId;
  private String hardwareFlavorId;
  private String locationId;
  @Nullable
  private TemplateOptions templateOptions;

  private VirtualMachineTemplateBuilder() {
  }

  private VirtualMachineTemplateBuilder(VirtualMachineTemplate virtualMachineTemplate) {
    name = virtualMachineTemplate.name();
    imageId = virtualMachineTemplate.imageId();
    hardwareFlavorId = virtualMachineTemplate.hardwareFlavorId();
    locationId = virtualMachineTemplate.locationId();
    templateOptions = virtualMachineTemplate.templateOptions().orNull();
  }

  public static VirtualMachineTemplateBuilder newBuilder() {
    return new VirtualMachineTemplateBuilder();
  }

  public static VirtualMachineTemplateBuilder of(VirtualMachineTemplate virtualMachineTemplate) {
    checkNotNull(virtualMachineTemplate, "virtualMachineTemplate is null");
    return new VirtualMachineTemplateBuilder(virtualMachineTemplate);
  }

  public VirtualMachineTemplateBuilder name(@Nullable final String name) {
    this.name = name;
    return this;
  }

  public VirtualMachineTemplateBuilder image(final String imageId) {
    this.imageId = imageId;
    return this;
  }

  public VirtualMachineTemplateBuilder hardwareFlavor(final String hardwareFlavorId) {
    this.hardwareFlavorId = hardwareFlavorId;
    return this;
  }

  public VirtualMachineTemplateBuilder location(final String locationId) {
    this.locationId = locationId;
    return this;
  }

  public VirtualMachineTemplateBuilder templateOptions(final TemplateOptions templateOptions) {
    this.templateOptions = templateOptions;
    return this;
  }

  public VirtualMachineTemplate build() {
    return new VirtualMachineTemplateImpl(name, imageId, hardwareFlavorId, locationId,
        templateOptions);
  }


}
