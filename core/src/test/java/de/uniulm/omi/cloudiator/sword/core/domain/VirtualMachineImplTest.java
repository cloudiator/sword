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

package de.uniulm.omi.cloudiator.sword.core.domain;

import com.google.common.collect.ImmutableSet;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * Created by daniel on 30.07.15.
 */
public class VirtualMachineImplTest {

    String testId = "1";
    String testName = "name";
    String testPublicIpAddress = "93.184.216.34";
    String testPrivateIpAddress = "192.168.0.2";
    private final Location testLocation =
        LocationBuilder.newBuilder().id("test").name("test").parent(null).assignable(true).build();
    LoginCredential loginCredential =
        LoginCredentialBuilder.newBuilder().password("password").username("username").build();

    VirtualMachine validVirtualMachine;
    VirtualMachineBuilder validVirtualMachineBuilder;

    @Before public void before() {
        validVirtualMachineBuilder =
            VirtualMachineBuilder.newBuilder().id(testId).name(testName).location(testLocation)
                .addPrivateIpAddress(testPrivateIpAddress).addPublicIpAddress(testPublicIpAddress)
                .loginCredential(loginCredential);
        validVirtualMachine = validVirtualMachineBuilder.build();
    }

    @Test(expected = NullPointerException.class) public void testConstructorDisallowsNullId() {
        validVirtualMachineBuilder.id(null).name(testName).location(testLocation).build();
    }

    @Test(expected = IllegalArgumentException.class) public void testConstructorDisallowsEmptyId() {
        validVirtualMachineBuilder.id("").name(testName).location(testLocation).build();
    }

    @Test(expected = NullPointerException.class) public void testConstructorDisallowsNullName() {
        validVirtualMachineBuilder.id(testId).name(null).location(testLocation).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorDisallowsEmptyName() {
        validVirtualMachineBuilder.id(testId).name("").location(testLocation).build();
    }

    @Test public void testId() {
        assertThat(validVirtualMachine.id(), equalTo(testId));
    }

    @Test public void testName() {
        assertThat(validVirtualMachine.name(), equalTo(testName));
    }

    @Test public void testLocation() {
        assertThat(validVirtualMachine.location().get(), equalTo(testLocation));
    }

    @Test public void testPublicAddresses() throws Exception {
        assertThat(validVirtualMachine.publicAddresses().contains(testPublicIpAddress),
            equalTo(true));
    }

    @Test public void testPrivateAddresses() throws Exception {
        assertThat(validVirtualMachine.privateAddresses().contains(testPrivateIpAddress),
            equalTo(true));
    }

    @Test public void testLoginCredential() throws Exception {
        assertThat(validVirtualMachine.loginCredential().get(), equalTo(loginCredential));
        assertThat(validVirtualMachineBuilder.loginCredential(null).build().loginCredential().get(),
            nullValue());
    }

    @Test public void testIpAddressesAreImmutable() throws Exception {
        assertThat(validVirtualMachine.privateAddresses(), instanceOf(ImmutableSet.class));
        assertThat(validVirtualMachine.publicAddresses(), instanceOf(ImmutableSet.class));
    }
}
