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
import de.uniulm.omi.cloudiator.sword.api.ServiceContext;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.identity.v2.Access;
import org.openstack4j.openstack.OSFactory;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Created by daniel on 17.11.16.
 */
public class OsClientV2Factory implements OsClientFactory {

    private final ServiceContext serviceContext;
    private Access access = null;

    @Inject public OsClientV2Factory(ServiceContext serviceContext) {
        checkNotNull(serviceContext, "serviceConfiguration is null");
        this.serviceContext = serviceContext;
    }

    @Override public synchronized OSClient create() {

        checkState(serviceContext.cloud().endpoint().isPresent(),
            String.format("%s requires endpoint to be present", this));

        final String[] split = serviceContext.cloud().credentials().user().split(":");
        checkState(split.length == 2, "Illegal username, expected tenant:user");
        final String tenantName = split[0];
        final String userName = split[1];

        if (access == null) {
            final OSClient.OSClientV2 authenticate =
                OSFactory.builderV2().endpoint(serviceContext.cloud().endpoint().get())
                    .credentials(userName, serviceContext.cloud().credentials().password())
                    .tenantName(tenantName).authenticate();
            this.access = authenticate.getAccess();
            return authenticate;
        }
        return OSFactory.clientFromAccess(access);
    }
}
