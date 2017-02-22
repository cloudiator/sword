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

package de.uniulm.omi.cloudiator.sword.drivers.openstack.strategy;

import de.uniulm.omi.cloudiator.sword.strategy.DeleteVirtualMachineStrategy;
import org.jclouds.http.HttpResponse;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 15.08.16.
 */
public class OpenstackDeleteVirtualMachineStrategy implements DeleteVirtualMachineStrategy {

    private final DeleteVirtualMachineStrategy jcloudsDeleteVirtualMachineStrategy;

    public OpenstackDeleteVirtualMachineStrategy(
        DeleteVirtualMachineStrategy jcloudsDeleteVirtualMachineStrategy) {
        checkNotNull(jcloudsDeleteVirtualMachineStrategy);
        this.jcloudsDeleteVirtualMachineStrategy = jcloudsDeleteVirtualMachineStrategy;
    }

    @Override public void apply(String virtualMachineId) {
        // try to workaround jcloud issue
        // https://issues.apache.org/jira/browse/JCLOUDS-1155
        try {
            jcloudsDeleteVirtualMachineStrategy.apply(virtualMachineId);
        } catch (org.jclouds.http.HttpResponseException e) {
            final HttpResponse response = e.getResponse();
            if (response.getStatusCode() == 400) {
                if (e.getContent().contains("Security Group") && e.getContent()
                    .contains("in use")) {
                    //silently error error
                    return;
                }
            }
            throw e;
        }
    }
}
