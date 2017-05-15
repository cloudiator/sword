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

package de.uniulm.omi.cloudiator.sword.drivers.openstack;

import com.google.inject.ImplementedBy;
import java.util.Set;
import javax.annotation.Nullable;
import org.jclouds.openstack.nova.v2_0.domain.FloatingIP;

/**
 * Created by daniel on 19.01.15.
 */
@ImplementedBy(OpenstackFloatingIpClientImpl.class)
public interface OpenstackFloatingIpClient {

  boolean isAvailable(String region);

  Set<FloatingIP> list(String region);

  @Nullable
  FloatingIP allocateFromPool(String pool, String region);

  @Nullable
  FloatingIP create(String region);

  void addToServer(String region, String address, String serverId);

  void removeFromServer(String region, String address, String serverId);
}
