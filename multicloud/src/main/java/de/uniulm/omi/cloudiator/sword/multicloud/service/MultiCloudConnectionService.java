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

package de.uniulm.omi.cloudiator.sword.multicloud.service;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.Lists;
import com.google.common.net.HostAndPort;
import de.uniulm.omi.cloudiator.domain.RemoteType;
import de.uniulm.omi.cloudiator.sword.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.remote.RemoteConnection;
import de.uniulm.omi.cloudiator.sword.remote.RemoteException;
import de.uniulm.omi.cloudiator.sword.service.ComputeService;
import de.uniulm.omi.cloudiator.sword.service.ConnectionService;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by daniel on 23.01.17.
 */
public class MultiCloudConnectionService implements ConnectionService {

  private final ComputeServiceProvider computeServiceProvider;

  public MultiCloudConnectionService(ComputeServiceProvider computeServiceProvider) {
    checkNotNull(computeServiceProvider, "computeServiceProvider is null");
    this.computeServiceProvider = computeServiceProvider;
  }

  @Override
  public RemoteConnection getRemoteConnection(HostAndPort hostAndPort, RemoteType remoteType,
      LoginCredential loginCredential) throws RemoteException {

    //for the time being we simple choose a random connection service from the list.
    checkState(!computeServiceProvider.all().isEmpty(),
        "Connection service requires at least one registered compute service");

    final ArrayList<ComputeService> computeServices =
        Lists.newArrayList(computeServiceProvider.all().values());
    Collections.shuffle(computeServices);
    return computeServices.get(0).connectionService()
        .getRemoteConnection(hostAndPort, remoteType, loginCredential);
  }
}
