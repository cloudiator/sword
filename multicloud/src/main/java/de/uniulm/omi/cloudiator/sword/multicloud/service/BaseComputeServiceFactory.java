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

package de.uniulm.omi.cloudiator.sword.multicloud.service;

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.domain.Cloud;
import de.uniulm.omi.cloudiator.domain.Configuration;
import de.uniulm.omi.cloudiator.sword.api.remote.AbstractRemoteModule;
import de.uniulm.omi.cloudiator.sword.api.service.ComputeService;
import de.uniulm.omi.cloudiator.sword.core.logging.AbstractLoggingModule;
import de.uniulm.omi.cloudiator.sword.service.ServiceBuilder;

import javax.annotation.Nullable;

/**
 * Created by daniel on 18.01.17.
 */
public class BaseComputeServiceFactory implements ComputeServiceFactory {

    @Nullable @Inject(optional = true) private AbstractRemoteModule abstractRemoteModule;
    @Nullable @Inject(optional = true) private AbstractLoggingModule abstractLoggingModule;

    public BaseComputeServiceFactory() {
    }

    @Override public ComputeService computeService(Cloud cloud, Configuration configuration) {

        final ServiceBuilder serviceBuilder =
            ServiceBuilder.newServiceBuilder().cloud(cloud).configuration(configuration);
        if (abstractLoggingModule != null) {
            serviceBuilder.loggingModule(abstractLoggingModule);
        }
        if (abstractRemoteModule != null) {
            serviceBuilder.remoteModule(abstractRemoteModule);
        }
        return serviceBuilder.build();
    }
}
