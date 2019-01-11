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

package de.uniulm.omi.cloudiator.sword.drivers.azure.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import com.microsoft.azure.management.Azure;
import de.uniulm.omi.cloudiator.sword.domain.Cloud;
import java.io.IOException;

/**
 * Created by daniel on 16.05.17.
 */
public class AzureProvider implements Provider<Azure> {

  private final static String DELIMITER = ":";
  private final Cloud cloud;

  @Inject
  public AzureProvider(Cloud cloud) {
    checkNotNull(cloud, "cloud is null");
    this.cloud = cloud;
  }

  @Override
  public Azure get() {
    String clientTenant = cloud.credential().user();
    int colonIndex = clientTenant.lastIndexOf(DELIMITER);
    if (colonIndex == -1) {
        throw new IllegalStateException("Expected user '" + clientTenant + "' to be of format clientId:tenant");
    }
    String clientId = clientTenant.substring(0, colonIndex);
    String tenantId = clientTenant.substring(colonIndex + 1);
    String key = cloud.credential().password();

    ApplicationTokenCredentials credentials = new ApplicationTokenCredentials(
        clientId, tenantId, key, AzureEnvironment.AZURE);
    try {
      return Azure.authenticate(credentials).withDefaultSubscription();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}
