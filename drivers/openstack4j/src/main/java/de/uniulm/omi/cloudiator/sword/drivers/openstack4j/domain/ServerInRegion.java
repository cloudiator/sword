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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.domain;

import static com.google.common.base.Preconditions.checkNotNull;

import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.sword.util.IdScopeByLocations;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import org.openstack4j.model.common.Link;
import org.openstack4j.model.compute.Addresses;
import org.openstack4j.model.compute.Fault;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.Image;
import org.openstack4j.model.compute.Keypair;
import org.openstack4j.model.compute.SecurityGroup;
import org.openstack4j.model.compute.Server;

/**
 * Created by daniel on 18.11.16.
 */
public class ServerInRegion implements Server, InRegion, ProviderIdentified {

  private final Server createdServer;
  private final Server retrievedServer;
  private final Location region;
  private final String regionScopedId;
  @Nullable
  private final Keypair keypair;

  public ServerInRegion(Server createdServer, Server retrievedServer, Location region,
      @Nullable Keypair keypair) {
    checkNotNull(retrievedServer, "retrieved server is null");
    checkNotNull(createdServer, "createdServer is null.");
    checkNotNull(region, "region is null.");
    this.createdServer = createdServer;
    this.retrievedServer = retrievedServer;
    this.region = region;
    regionScopedId =
        IdScopeByLocations.from(region.id(), createdServer.getId()).getIdWithLocation();
    this.keypair = keypair;
  }

  public Optional<Keypair> keypair() {
    return Optional.ofNullable(keypair);
  }

  @Override
  public String getId() {
    return regionScopedId;
  }

  @Override
  public String getName() {
    if (createdServer.getName() == null) {
      return retrievedServer.getName();
    }
    return createdServer.getName();
  }

  @Override
  public Addresses getAddresses() {
    return createdServer.getAddresses();
  }

  @Override
  public List<? extends Link> getLinks() {
    return createdServer.getLinks();
  }

  @Override
  public String getImageId() {
    return getImage().getId();
  }

  @Override
  public Image getImage() {

    if (createdServer.getImage() != null) {
      return new ImageInRegion(createdServer.getImage(), region);
    }

    return new ImageInRegion(retrievedServer.getImage(), region);
  }

  @Override
  public String getFlavorId() {
    return getFlavor().getId();
  }

  @Override
  public Flavor getFlavor() {
    if (createdServer.getFlavor() != null) {
      return new FlavorInRegion(createdServer.getFlavor(), region);
    }
    return new FlavorInRegion(retrievedServer.getFlavor(), region);

  }

  @Override
  public String getAccessIPv4() {
    return createdServer.getAccessIPv4();
  }

  @Override
  public String getAccessIPv6() {
    return createdServer.getAccessIPv6();
  }

  @Override
  public String getConfigDrive() {
    return createdServer.getConfigDrive();
  }

  @Override
  public Status getStatus() {
    return createdServer.getStatus();
  }

  @Override
  public int getProgress() {
    return createdServer.getProgress();
  }

  @Override
  public Fault getFault() {
    return createdServer.getFault();
  }

  @Override
  public String getTenantId() {
    return createdServer.getTenantId();
  }

  @Override
  public String getUserId() {
    return createdServer.getUserId();
  }

  @Override
  public String getKeyName() {
    return createdServer.getKeyName();
  }

  @Override
  public String getHostId() {
    return createdServer.getHostId();
  }

  @Override
  public Date getUpdated() {
    return createdServer.getUpdated();
  }

  @Override
  public Date getCreated() {
    return createdServer.getCreated();
  }

  @Override
  public Map<String, String> getMetadata() {
    return createdServer.getMetadata();
  }

  @Override
  public String getTaskState() {
    return createdServer.getTaskState();
  }

  @Override
  public String getPowerState() {
    return createdServer.getPowerState();
  }

  @Override
  public String getVmState() {
    return createdServer.getVmState();
  }

  @Override
  public String getHost() {
    return createdServer.getHost();
  }

  @Override
  public String getInstanceName() {
    return createdServer.getInstanceName();
  }

  @Override
  public String getHypervisorHostname() {
    return createdServer.getHypervisorHostname();
  }

  @Override
  public DiskConfig getDiskConfig() {
    return createdServer.getDiskConfig();
  }

  @Override
  public String getAvailabilityZone() {
    return createdServer.getAvailabilityZone();
  }

  @Override
  public Date getLaunchedAt() {
    return createdServer.getLaunchedAt();
  }

  @Override
  public Date getTerminatedAt() {
    return createdServer.getTerminatedAt();
  }

  @Override
  public List<String> getOsExtendedVolumesAttached() {
    return createdServer.getOsExtendedVolumesAttached();
  }

  @Override
  public String getUuid() {
    return createdServer.getUuid();
  }

  @Override
  public String getAdminPass() {
    return createdServer.getAdminPass();
  }

  @Override
  public List<? extends SecurityGroup> getSecurityGroups() {
    return createdServer.getSecurityGroups();
  }

  @Override
  public String providerId() {
    return createdServer.getId();
  }

  @Override
  public Location region() {
    return region;
  }
}
