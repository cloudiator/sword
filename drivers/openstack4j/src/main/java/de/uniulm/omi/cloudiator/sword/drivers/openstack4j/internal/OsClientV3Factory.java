/*
 * Copyright (c) 2014-2016 University of Ulm
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
import de.uniulm.omi.cloudiator.sword.ServiceContext;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.openstack.OSFactory;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Created by daniel on 17.11.16.
 */
public class OsClientV3Factory implements OsClientFactory {

    private final ServiceContext serviceContext;
    private static Token token = null;


    @Inject public OsClientV3Factory(ServiceContext serviceContext) {
        checkNotNull(serviceContext, "serviceConfiguration is null");
        this.serviceContext = serviceContext;
    }

    @Override public OSClient create() {

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

        final String[] split = serviceContext.cloud().credentials().user().split(":");
        checkState(split.length == 3, String
            .format("Illegal username, expected user to be of format domain:tenant:user, got %s",
                serviceContext.cloud().credentials().user()));

        final String domainId = split[0];
        final String tenantId = split[1];
        final String userId = split[2];


        //todo resolve identifier from credentials
        Identifier domainIdentifier = Identifier.byId(domainId);
        Identifier tenantIdentifier = Identifier.byId(tenantId);

        checkState(serviceContext.cloud().endpoint().isPresent(),
            "Endpoint is required for Openstack4J Driver.");

        return OSFactory.builderV3().endpoint(serviceContext.cloud().endpoint().get())
            .credentials(userId, serviceContext.cloud().credentials().password(), domainIdentifier)
            .scopeToProject(tenantIdentifier).authenticate();
    }

}
