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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.domain.Cloud;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nullable;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.api.exceptions.AuthenticationException;
import org.openstack4j.api.exceptions.ClientResponseException;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.openstack.OSFactory;

/**
 * Created by daniel on 17.11.16.
 */
public class OsClientV3Factory implements OsClientFactory {

  private static Token token = null;
  private final Cloud cloud;


  @Inject
  public OsClientV3Factory(Cloud cloud) {
    checkNotNull(cloud, "serviceConfiguration is null");
    this.cloud = cloud;
  }

  /**
   * This method tries to determine if the authentication should happen by using the user provided
   * information as domain ID or domain name resp. project ID or project name.
   *
   * It does so by brute forcing the four different possible combinations.
   *
   * Returns early on success.
   *
   * @param domainIdOrName the domain id or name
   * @param projectIdOrName the project id or name
   * @param endpoint the endpoint
   * @param userId the user
   * @param password the password of the user
   * @return The valid identifiers if a valid combination exists or null if all combinations failed.
   */
  @Nullable
  private static Identifiers handleIdentifiers(String domainIdOrName, String projectIdOrName,
      String endpoint, String userId, String password) {

    //we try all four cases and return the one that works
    Set<Identifiers> possibleIdentifiers = new HashSet<Identifiers>() {{
      add(new Identifiers(Identifier.byName(domainIdOrName), Identifier.byName(projectIdOrName)));
      add(new Identifiers(Identifier.byId(domainIdOrName), Identifier.byId(projectIdOrName)));
      add(new Identifiers(Identifier.byId(domainIdOrName), Identifier.byName(projectIdOrName)));
      add(new Identifiers(Identifier.byName(domainIdOrName), Identifier.byId(projectIdOrName)));
    }};

    for (Identifiers candidate : possibleIdentifiers) {
      try {
        authenticate(endpoint, userId, password, candidate.getDomain(), candidate.getProject());
      } catch (AuthenticationException | ClientResponseException e) {
        continue;
      }
      return candidate;
    }
    return null;
  }

  private static OSClientV3 authenticate(String endpoint, String userId, String password,
      Identifier domain, Identifier project) {
    return OSFactory.builderV3().endpoint(endpoint)
        .credentials(userId, password, domain)
        .scopeToProject(project).authenticate();
  }

  @Override
  public OSClient create() {

    OSClient osClient;

    if (token == null) {
      osClient = authFromServiceConfiguration();
      token = ((OSClient.OSClientV3) osClient).getToken();
    } else {
      osClient = authFromToken();
    }
    return osClient;
  }

  private OSClient authFromToken() {
    return OSFactory.clientFromToken(token);
  }

  private OSClient authFromServiceConfiguration() {

    final String[] split = cloud.credential().user().split(":");
    checkState(split.length == 3, String
        .format("Illegal username, expected user to be of format domain:project:user, got %s",
            cloud.credential().user()));

    final String domainId = split[0];
    final String tenantId = split[1];
    final String userId = split[2];

    checkState(cloud.endpoint().isPresent(), "Endpoint is required for Openstack4J Driver.");

    final Identifiers identifiers = handleIdentifiers(domainId, tenantId, cloud.endpoint().get(),
        userId, cloud.credential().password());

    checkState(identifiers != null,
        "Could not determine correct identifiers to use for domain and project. Probably the credentials are wrong.");

    return authenticate(cloud.endpoint().get(), userId, cloud.credential().password(),
        identifiers.getDomain(), identifiers.getProject());
  }

  private static class Identifiers {

    private final Identifier domain;
    private final Identifier project;

    private Identifiers(Identifier domain, Identifier project) {
      this.domain = domain;
      this.project = project;
    }


    public Identifier getDomain() {
      return domain;
    }

    public Identifier getProject() {
      return project;
    }
  }

}
