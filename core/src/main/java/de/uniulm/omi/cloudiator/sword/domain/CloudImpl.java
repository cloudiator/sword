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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Charsets;
import com.google.common.hash.Funnel;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import java.util.Optional;
import javax.annotation.Nullable;

/**
 * Created by daniel on 18.01.17.
 */
public class CloudImpl implements Cloud {

  private final Api api;
  @Nullable
  private final String endpoint;
  private final CloudCredential cloudCredential;
  private final Configuration configuration;
  @Nullable
  private final CloudType cloudType;

  CloudImpl(Api api, @Nullable String endpoint, CloudCredential cloudCredential,
      Configuration configuration, @Nullable CloudType cloudType) {

    checkNotNull(api, "api is null.");
    if (endpoint != null) {
      checkArgument(!endpoint.isEmpty());
    }
    checkNotNull(cloudCredential, "credentials is null");
    checkNotNull(configuration, "configuration is null");

    this.api = api;
    this.endpoint = endpoint;
    this.cloudCredential = cloudCredential;
    this.configuration = configuration;
    this.cloudType = cloudType;
  }

  @Override
  public String id() {
    return HashingCloudIdGenerator.generateId(this);
  }

  @Override
  public Api api() {
    return api;
  }

  @Override
  public Optional<String> endpoint() {
    return Optional.ofNullable(endpoint);
  }

  @Override
  public CloudCredential credential() {
    return cloudCredential;
  }

  @Override
  public Configuration configuration() {
    return configuration;
  }

  @Nullable
  @Override
  public CloudType cloudType() {
    return cloudType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CloudImpl cloud = (CloudImpl) o;

    if (!api.equals(cloud.api)) {
      return false;
    }
    if (endpoint != null ? !endpoint.equals(cloud.endpoint) : cloud.endpoint != null) {
      return false;
    }
    if (!cloudCredential.equals(cloud.cloudCredential)) {
      return false;
    }
    if (!configuration.equals(cloud.configuration)) {
      return false;
    }
    return cloudType == cloud.cloudType;
  }

  @Override
  public int hashCode() {
    int result = api.hashCode();
    result = 31 * result + (endpoint != null ? endpoint.hashCode() : 0);
    result = 31 * result + cloudCredential.hashCode();
    result = 31 * result + configuration.hashCode();
    result = 31 * result + (cloudType != null ? cloudType.hashCode() : 0);
    return result;
  }

  private static class HashingCloudIdGenerator {

    private static final Funnel<Cloud> CLOUD_FUNNEL = (Funnel<Cloud>) (from, into) -> {
      into.putString(from.api().providerName(), Charsets.UTF_8);
      into.putString(from.credential().id(), Charsets.UTF_8);
      if (from.endpoint().isPresent()) {
        into.putString(from.endpoint().get(), Charsets.UTF_8);
      }
    };
    private final static HashFunction HASH_FUNCTION = Hashing.md5();

    static String generateId(Cloud cloud) {
      return HASH_FUNCTION.hashObject(cloud, CLOUD_FUNNEL).toString();
    }

  }
}
