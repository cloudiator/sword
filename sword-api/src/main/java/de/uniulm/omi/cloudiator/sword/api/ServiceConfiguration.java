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

package de.uniulm.omi.cloudiator.sword.api;

import de.uniulm.omi.cloudiator.sword.api.domain.Credentials;

/**
 * Provides the configuration of the service.
 */
public interface ServiceConfiguration {

    /**
     * @return the address of the api endpoint of the service provider.
     */
    String getEndpoint();

    /**
     * @return the name of the provider.
     */
    String getProvider();

    /**
     * @return the credentials for the api endpoint.
     */
    Credentials getCredentials();

    /**
     * @return the node group.
     */
    String getNodeGroup();
}
