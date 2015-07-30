/*
 * Copyright (c) 2014-2015 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.core.domain.impl;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import de.uniulm.omi.cloudiator.sword.api.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.core.domain.builders.LoginCredentialBuilder;
import de.uniulm.omi.cloudiator.sword.core.domain.builders.VirtualMachineBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * Created by daniel on 30.07.15.
 */
public class VirtualMachineImplTest {

    String virtualMachineId = "1";
    String virtualMachineName = "name";
    String publicIpAddress = "93.184.216.34";
    String privateIpAddress = "192.168.0.2";
    LoginCredential loginCredential =
        LoginCredentialBuilder.newBuilder().password("password").username("username").build();

    VirtualMachine validVirtualMachine;
    VirtualMachineBuilder validVirtualMachineBuilder;

    @Before public void before() {
        validVirtualMachineBuilder =
            VirtualMachineBuilder.newBuilder().id(virtualMachineId).name(virtualMachineName)
                .addPrivateIpAddress(privateIpAddress).addPublicIpAddress(publicIpAddress)
                .loginCredential(loginCredential);
        validVirtualMachine = validVirtualMachineBuilder.build();
    }

    @Test(expected = NullPointerException.class) public void testConstructorDisallowsNullId() {
        validVirtualMachineBuilder.id(null).build();
    }

    @Test(expected = IllegalArgumentException.class) public void testConstructorDisallowsEmptyId() {
        validVirtualMachineBuilder.id("").build();
    }

    @Test(expected = NullPointerException.class) public void testConstructorDisallowsNullName() {
        validVirtualMachineBuilder.name(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorDisallowsEmptyName() {
        validVirtualMachineBuilder.name("").build();
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorDisallowsNullPublicAddresses() {
        new VirtualMachineImpl(virtualMachineId, virtualMachineName, null,
            new HashSet<>(Collections.singletonList(privateIpAddress)),
            Optional.fromNullable(loginCredential));
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorDisallowsNullPrivateAddresses() {
        new VirtualMachineImpl(virtualMachineId, virtualMachineName,
            new HashSet<>(Collections.singletonList(publicIpAddress)), null,
            Optional.fromNullable(loginCredential));
    }

    //todo: implement
    @Test public void testToString() throws Exception {

    }

    @Test public void testPublicAddresses() throws Exception {
        assertThat(validVirtualMachine.publicAddresses().contains(publicIpAddress), equalTo(true));
    }

    @Test public void testPrivateAddresses() throws Exception {
        assertThat(validVirtualMachine.privateAddresses().contains(privateIpAddress),
            equalTo(true));
    }

    @Test public void testLoginCredential() throws Exception {
        assertThat(validVirtualMachine.loginCredential().get(), equalTo(loginCredential));
    }

    @Test public void testIpAddressesAreImmutable() throws Exception {
        assertThat(validVirtualMachine.privateAddresses(), instanceOf(ImmutableSet.class));
        assertThat(validVirtualMachine.publicAddresses(), instanceOf(ImmutableSet.class));
    }
}
