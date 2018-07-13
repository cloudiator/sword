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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.converters;

import static com.google.common.base.Preconditions.checkNotNull;

import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.Image;
import de.uniulm.omi.cloudiator.sword.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.domain.LoginCredentialBuilder;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachineBuilder;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.domain.ServerInRegion;
import de.uniulm.omi.cloudiator.sword.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import javax.inject.Inject;
import org.openstack4j.model.compute.Address;

/**
 * Created by daniel on 18.11.16.
 */
public class ServerInRegionToVirtualMachine
    implements OneWayConverter<ServerInRegion, VirtualMachine> {

  private final GetStrategy<String, Image> imageGetStrategy;
  private final GetStrategy<String, HardwareFlavor> hardwareFlavorGetStrategy;

  @Inject
  public ServerInRegionToVirtualMachine(GetStrategy<String, Image> imageGetStrategy,
      GetStrategy<String, HardwareFlavor> hardwareFlavorGetStrategy) {

    checkNotNull(imageGetStrategy, "imageGetStrategy is null");
    checkNotNull(hardwareFlavorGetStrategy, "hardwareFlavorGetStrategy is null");

    this.imageGetStrategy = imageGetStrategy;
    this.hardwareFlavorGetStrategy = hardwareFlavorGetStrategy;
  }

  @Override
  public VirtualMachine apply(ServerInRegion serverInRegion) {
    //todo check which region to return. Always region or av or host?
    //todo add login credential and ip addresses

    LoginCredential loginCredential = null;
    if (serverInRegion.keypair().isPresent()) {
      loginCredential = LoginCredentialBuilder.newBuilder()
          .privateKey(serverInRegion.keypair().get().getPrivateKey()).build();
    }

    VirtualMachineBuilder virtualMachineBuilder = VirtualMachineBuilder.newBuilder()
        .name(serverInRegion.getName())
        .image(imageGetStrategy.get(serverInRegion.getImageId()))
        .hardware(hardwareFlavorGetStrategy.get(serverInRegion.getFlavorId()))
        .id(serverInRegion.getId()).providerId(serverInRegion.providerId())
        .location(serverInRegion.region())
        .loginCredential(loginCredential);

    //extract private Ips from serverInRegion
    Set<Entry<String, List<? extends Address>>> addressSet = serverInRegion
        .getAddresses().getAddresses().entrySet();

    if (addressSet.size() > 1) {
      throw new UnsupportedOperationException(
          "Server is configured with multiple networks. This is currently not supported.");
    }

    //adding only the addresses of the first network interface
    if (addressSet.iterator().hasNext()) {
      Entry<String, List<? extends Address>> addressEntry = addressSet.iterator().next();
      //LOGGER.warn(String.format("Using addresses for network:  %s", addressEntry.getKey()));

      List<? extends Address> addressList = addressEntry.getValue();

      Set<String> ipAdressesToAdd = new HashSet<>();

      for (Address address : addressList) {

        //TODO: currently only adding fixed IPs, i.e. private IPs, public IPs will be resolved later
        if (address.getType().equals("fixed")) {
          //LOGGER.warn(String.format("Found private (fixed) IP: %s", address.getAddr()));
          //virtualMachineBuilder.addPrivateIpAddress(address.getAddr());
          ipAdressesToAdd.add(address.getAddr());

        }
      }

      virtualMachineBuilder.addIpStrings(ipAdressesToAdd);

    }

    return virtualMachineBuilder.build();


  }
}
