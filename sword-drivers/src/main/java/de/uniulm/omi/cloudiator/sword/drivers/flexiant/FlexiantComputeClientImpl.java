/*
 * Copyright (c) 2014-2015 University of Ulm
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.uniulm.omi.cloudiator.sword.drivers.flexiant;

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.api.ServiceConfiguration;
import de.uniulm.omi.cloudiator.sword.api.exceptions.DriverException;
import de.uniulm.omi.flexiant.client.api.FlexiantException;
import de.uniulm.omi.flexiant.domain.impl.*;

import java.util.Set;


/**
 * Created by daniel on 05.12.14.
 */
public class FlexiantComputeClientImpl implements FlexiantComputeClient {

    private final de.uniulm.omi.flexiant.client.compute.FlexiantComputeClient flexiantComputeClient;
    private final ServiceConfiguration serviceConfiguration;

    @Inject public FlexiantComputeClientImpl(ServiceConfiguration serviceConfiguration) {
        flexiantComputeClient = new de.uniulm.omi.flexiant.client.compute.FlexiantComputeClient(
            serviceConfiguration.getEndpoint(), serviceConfiguration.getCredentials().user(),
            serviceConfiguration.getCredentials().password());
        this.serviceConfiguration = serviceConfiguration;
    }

    @Override public Set<Image> listImages() {
        try {
            return this.flexiantComputeClient.getImages(null);
        } catch (FlexiantException e) {
            throw new DriverException("Could not retrieve images", e);
        }
    }

    @Override public Set<Hardware> listHardware() {
        try {
            return this.flexiantComputeClient.getHardwareFlavors(null);
        } catch (FlexiantException e) {
            throw new DriverException("Could not retrieve hardware", e);
        }
    }

    @Override public Set<Location> listLocations() {
        try {
            return this.flexiantComputeClient.getLocations();
        } catch (FlexiantException e) {
            throw new DriverException("Could not retrieve images", e);
        }
    }

    @Override public Set<Server> listServers() {
        try {
            return this.flexiantComputeClient.getServers(serviceConfiguration.getNodeGroup(), null);
        } catch (FlexiantException e) {
            throw new DriverException("Could not retrieve servers.", e);
        }
    }

    @Override public Server createServer(ServerTemplate serverTemplate) {
        try {
            return this.flexiantComputeClient.createServer(serverTemplate);
        } catch (FlexiantException e) {
            throw new DriverException(e);
        }
    }

    @Override public void deleteServer(String serverUUID) {
        try {
            this.flexiantComputeClient.deleteServer(serverUUID);
        } catch (FlexiantException e) {
            throw new DriverException(e);
        }
    }


}
