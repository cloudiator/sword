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

package de.uniulm.omi.cloudiator.sword.domain;


import static com.google.common.base.Preconditions.checkNotNull;

import de.uniulm.omi.cloudiator.domain.OperatingSystem;

/**
 * Created by daniel on 01.12.14.
 */
public class ImageBuilder {

  private String id;
  private String providerId;
  private String name;
  private Location location;
  private OperatingSystem os;

  private ImageBuilder() {

  }

  private ImageBuilder(Image image) {
    id = image.id();
    providerId = image.providerId();
    name = image.name();
    location = image.location().orElse(null);
    os = image.operatingSystem();
  }

  public static ImageBuilder newBuilder() {
    return new ImageBuilder();
  }

  public static ImageBuilder of(Image image) {
    checkNotNull(image, "image is null");
    return new ImageBuilder(image);
  }

  public ImageBuilder id(String id) {
    this.id = id;
    return this;
  }

  public ImageBuilder providerId(String providerId) {
    this.providerId = providerId;
    return this;
  }

  public ImageBuilder name(String name) {
    this.name = name;
    return this;
  }

  public ImageBuilder location(Location location) {
    this.location = location;
    return this;
  }

  public ImageBuilder os(OperatingSystem os) {
    this.os = os;
    return this;
  }

  public ImageImpl build() {
    return new ImageImpl(id, providerId, name, location, os);
  }
}
