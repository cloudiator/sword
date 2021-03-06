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

import com.google.common.base.MoreObjects;
import javax.annotation.Nullable;

/**
 * Created by daniel on 18.01.17.
 */
public class CloudBuilder {

  @Nullable
  private Api api;
  @Nullable
  private String endpoint;
  @Nullable
  private CloudCredential cloudCredential;
  @Nullable
  private Configuration configuration;
  @Nullable
  private CloudType cloudType;

  private CloudBuilder() {

  }

  private CloudBuilder(Cloud cloud) {
    this.api = cloud.api();
    this.endpoint = cloud.endpoint().orElse(null);
    this.cloudCredential = cloud.credential();
    this.cloudType = cloud.cloudType();
    this.configuration = cloud.configuration();
  }

  public static CloudBuilder newBuilder() {
    return new CloudBuilder();
  }

  public static CloudBuilder of(Cloud cloud) {
    checkNotNull(cloud, "cloud is null");
    return new CloudBuilder(cloud);
  }

  public CloudBuilder api(Api api) {
    this.api = api;
    return this;
  }

  public CloudBuilder endpoint(String endpoint) {
    this.endpoint = endpoint;
    return this;
  }

  public CloudBuilder credentials(CloudCredential cloudCredential) {
    this.cloudCredential = cloudCredential;
    return this;
  }

  public CloudBuilder configuration(Configuration configuration) {
    this.configuration = configuration;
    return this;
  }

  public CloudBuilder cloudType(CloudType cloudType) {
    this.cloudType = cloudType;
    return this;
  }

  public Cloud build() {
    return new CloudImpl(api, endpoint, cloudCredential, configuration, cloudType);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).toString();
  }

}
