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

package de.uniulm.omi.cloudiator.sword.drivers.jclouds;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.api.ServiceConfiguration;
import de.uniulm.omi.cloudiator.sword.api.domain.SecurityGroup;
import de.uniulm.omi.cloudiator.sword.api.extensions.SecurityGroupService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.extensions.SecurityGroupExtension;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Created by daniel on 01.07.16.
 */
public class JCloudsSecurityGroupService implements SecurityGroupService {

    private final ComputeServiceContext computeServiceContext;
    private final ServiceConfiguration serviceConfiguration;

    @Inject public JCloudsSecurityGroupService(JCloudsViewFactory jCloudsViewFactory,
        ServiceConfiguration serviceConfiguration) {

        checkNotNull(jCloudsViewFactory);
        checkNotNull(serviceConfiguration);

        this.computeServiceContext =
            jCloudsViewFactory.buildJCloudsView(ComputeServiceContext.class);
        this.serviceConfiguration = serviceConfiguration;

    }

    @Override public Set<SecurityGroup> listSecurityGroups() {
        final Optional<SecurityGroupExtension> optional =
            this.computeServiceContext.getComputeService().getSecurityGroupExtension();
        checkState(optional.isPresent(),"security group extension not present.");

        return null;
    }

    @Override public SecurityGroup createSecurityGroup(String name, String location) {
        return null;
    }
}
