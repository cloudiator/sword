/*
 * Copyright (c) 2014-2019 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.internal;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.List;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.OSClient.OSClientV2;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.identity.v2.Role;

public class UserIdentity {

  private final Provider<OSClient> osClient;

  @Inject
  public UserIdentity(Provider<OSClient> osClient) {
    this.osClient = osClient;
  }

  public String getUser() {

    final OSClient osClient = this.osClient.get();

    if (osClient instanceof OSClient.OSClientV2) {
      return ((OSClientV2) osClient).getAccess().getUser().getId();
    } else if (osClient instanceof OSClient.OSClientV3) {
      return ((OSClientV3) osClient).getToken().getUser().getId();
    } else {
      throw new AssertionError("Unexpected type of osClient: " + osClient.getClass().getName());
    }


  }

  public String getTenant() {

    final OSClient osClient = this.osClient.get();

    if (osClient instanceof OSClient.OSClientV2) {
      final List<? extends Role> roles = ((OSClientV2) osClient).getAccess().getUser().getRoles();
      throw new UnsupportedOperationException(
          "Could not retrieve tenant. Roles of the user are: " + roles);
    } else if (osClient instanceof OSClient.OSClientV3) {
      return ((OSClientV3) osClient).getToken().getProject().getId();
    } else {
      throw new AssertionError("Unexpected type of osClient: " + osClient.getClass().getName());
    }
  }

}
