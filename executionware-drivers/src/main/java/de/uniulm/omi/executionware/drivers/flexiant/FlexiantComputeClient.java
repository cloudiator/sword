/*
 * Copyright (c) 2014 University of Ulm
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

package de.uniulm.omi.executionware.drivers.flexiant;

import com.google.inject.Inject;
import de.uniulm.omi.executionware.api.ServiceConfiguration;
import de.uniulm.omi.executionware.api.exceptions.DriverException;
import de.uniulm.omi.flexiant.*;

import java.util.Set;

/**
 * Created by daniel on 05.12.14.
 */
public class FlexiantComputeClient implements FlexiantComputeClientApi {

    private final de.uniulm.omi.flexiant.FlexiantComputeClient flexiantComputeClient;

    @Inject
    public FlexiantComputeClient(ServiceConfiguration serviceConfiguration) {
        flexiantComputeClient = new de.uniulm.omi.flexiant.FlexiantComputeClient(
                serviceConfiguration.getEndpoint(),
                serviceConfiguration.getCredentials().getUser(),
                serviceConfiguration.getCredentials().getPassword()
        );
    }

    @Override
    public Set<? extends FlexiantImage> listImages() {
        try {
            return this.flexiantComputeClient.getImages();
        } catch (FlexiantException e) {
            throw new DriverException("Could not retrieve images", e);
        }
    }

    @Override
    public Set<? extends FlexiantHardware> listHardware() {
        try {
            return this.flexiantComputeClient.getHardwareFlavors();
        } catch (FlexiantException e) {
            throw new DriverException("Could not retrieve hardware", e);
        }
    }

    @Override
    public Set<? extends FlexiantLocation> listLocations() {
        try {
            return this.flexiantComputeClient.getLocations();
        } catch (FlexiantException e) {
            throw new DriverException("Could not retrieve images", e);
        }
    }

    @Override
    public Set<? extends FlexiantServer> listServers() {
        try {
            return this.flexiantComputeClient.getServers();
        } catch (FlexiantException e) {
            throw new DriverException("Could not retrieve servers.", e);
        }
    }
}
