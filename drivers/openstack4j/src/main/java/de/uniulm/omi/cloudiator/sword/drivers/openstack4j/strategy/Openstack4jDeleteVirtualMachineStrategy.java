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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.strategy;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Inject;
import com.google.inject.Provider;
import de.uniulm.omi.cloudiator.sword.strategy.DeleteVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.util.IdScopeByLocations;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by daniel on 29.11.16.
 */
public class Openstack4jDeleteVirtualMachineStrategy implements DeleteVirtualMachineStrategy {

  private final Provider<OSClient> osClient;
  private static final Logger LOGGER = LoggerFactory
      .getLogger(Openstack4jDeleteVirtualMachineStrategy.class);

  @Inject
  public Openstack4jDeleteVirtualMachineStrategy(Provider<OSClient> osClient) {
    checkNotNull(osClient, "osClient is null.");
    this.osClient = osClient;
  }

  @Override
  public void apply(String id) {

    //todo: use region?
    final Server server = osClient.get().compute().servers()
        .get(IdScopeByLocations.from(id).getId());

    final String keyName = server.getKeyName();

    checkNotNull(id, "id is null.");
    LOGGER.info(String.format("Deleting server %s.", server));
    osClient.get().compute().servers().delete(server.getId());

    if (keyName != null) {
      try {
        LOGGER.debug(String
            .format("Deleting keyPair %s belonging to previously deleted server %s", keyName,
                server));
        osClient.get().compute().keypairs().delete(keyName);
      } catch (Exception e) {
        //ignored
        LOGGER.warn(String.format("Error while deleting keyPair %s. Ignoring this error.", keyName),
            e);
      }
    }

  }
}
