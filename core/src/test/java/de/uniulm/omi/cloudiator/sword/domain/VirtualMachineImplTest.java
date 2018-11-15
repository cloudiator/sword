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

package de.uniulm.omi.cloudiator.sword.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import de.uniulm.omi.cloudiator.domain.LocationScope;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by daniel on 30.07.15.
 */
public class VirtualMachineImplTest {

  private final Location testLocation =
      LocationBuilder.newBuilder().id("locationId").providerId("locationProviderId")
          .name("locationName").parent(null)
          .assignable(true)
          .scope(LocationScope.REGION).build();
  String testId = "1";
  String providerId = "providerId";
  String testName = "name";

  IpAddress testPublicIpAddress = IpAddresses.of("93.184.216.34");
  IpAddress testPrivateIpAddress = IpAddresses.of("192.168.0.2");

  LoginCredential loginCredential =
      LoginCredentialBuilder.newBuilder().password("password").username("username").build();

  VirtualMachine validVirtualMachine;
  VirtualMachineBuilder validVirtualMachineBuilder;

  @Before
  public void before() {
    validVirtualMachineBuilder =
        VirtualMachineBuilder.newBuilder().id(testId).providerId(providerId).name(testName)
            .location(testLocation).addIpAddress(testPrivateIpAddress)
            .addIpAddress(testPublicIpAddress).loginCredential(loginCredential);
    validVirtualMachine = validVirtualMachineBuilder.build();
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorDisallowsNullId() {
    validVirtualMachineBuilder.id(null).build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorDisallowsEmptyId() {
    validVirtualMachineBuilder.id("").build();
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorDisallowsNullName() {
    validVirtualMachineBuilder.name(null).build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorDisallowsEmptyName() {
    validVirtualMachineBuilder.name("").build();
  }

  @Test
  public void testId() {
    assertThat(validVirtualMachine.id(), equalTo(testId));
  }

  @Test
  public void testProviderId() {
    assertThat(validVirtualMachine.providerId(), equalTo(providerId));
  }

  @Test
  public void testName() {
    assertThat(validVirtualMachine.name(), equalTo(testName));
  }

  @Test
  public void testLocation() {
    assertThat(validVirtualMachine.location().get(), equalTo(testLocation));
  }

  @Test
  public void testPublicAddresses() throws Exception {
    assertThat(validVirtualMachine.ipAddresses().contains(testPublicIpAddress),
        equalTo(true));
  }

  @Test
  public void testPrivateAddresses() throws Exception {
    assertThat(validVirtualMachine.ipAddresses().contains(testPrivateIpAddress),
        equalTo(true));
  }

  @Test
  public void testLoginCredential() throws Exception {
    assertThat(validVirtualMachine.loginCredential().get(), equalTo(loginCredential));
    assertThat(
        validVirtualMachineBuilder.loginCredential(null).build().loginCredential().isPresent(),
        equalTo(false));
  }
}
