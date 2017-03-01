/*
 * Copyright (c) 2014-2017 University of Ulm
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

import de.uniulm.omi.cloudiator.domain.LocationScope;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

/**
 * Created by daniel on 03.12.14.
 */
public class HardwareFlavorImplTest {

    private final String testId = "123456";
    private final String testProviderId = "providerId";
    private final String testName = "name";
    private final int testCores = 1;
    private final long testRam = 1024;
    private final Float testDisk = 1.024F;
    private final Location testLocation =
        LocationBuilder.newBuilder().id("test").name("test").parent(null).assignable(true)
            .scope(LocationScope.REGION).build();
    private HardwareFlavorImpl validHardwareFlavor;
    private HardwareFlavorBuilder validHardwareFlavorBuilder;

    @Before public void before() {
        this.validHardwareFlavorBuilder =
            HardwareFlavorBuilder.newBuilder().id(testId).providerId(testProviderId).name(testName)
                .location(testLocation).cores(testCores).mbRam(testRam).gbDisk(testDisk);
        this.validHardwareFlavor = validHardwareFlavorBuilder.build();
    }

    @Test(expected = IllegalArgumentException.class) public void coresNotZeroTest() {
        validHardwareFlavorBuilder.cores(0).build();
    }

    @Test(expected = IllegalArgumentException.class) public void coresNotNegativeTest() {
        validHardwareFlavorBuilder.cores(-1).build();
    }

    @Test(expected = IllegalArgumentException.class) public void ramNotZeroTest() {
        validHardwareFlavorBuilder.mbRam(0).build();
    }

    @Test(expected = IllegalArgumentException.class) public void ramNotNegativeTest() {
        validHardwareFlavorBuilder.mbRam(-1).build();
    }

    @Test(expected = NullPointerException.class) public void idNotNullTest() {
        validHardwareFlavorBuilder.id(null).build();
    }

    @Test(expected = IllegalArgumentException.class) public void idNotEmptyTest() {
        validHardwareFlavorBuilder.id("").build();
    }

    @Test(expected = NullPointerException.class) public void providerIdNotNullTest() {
        validHardwareFlavorBuilder.providerId(null).build();
    }

    @Test(expected = IllegalArgumentException.class) public void providerIdNotEmptyTest() {
        validHardwareFlavorBuilder.providerId("").build();
    }

    @Test(expected = NullPointerException.class) public void nameNotNullTest() {
        validHardwareFlavorBuilder.name(null).build();
    }

    @Test(expected = IllegalArgumentException.class) public void nameNotEmptyTest() {
        validHardwareFlavorBuilder.name("").build();
    }

    @Test public void diskNullTest() {
        final HardwareFlavorImpl hardwareFlavor = validHardwareFlavorBuilder.gbDisk(null).build();
        assertThat(hardwareFlavor.gbDisk(), nullValue());
    }

    @Test(expected = IllegalArgumentException.class) public void diskGreaterZeroTest() {
        validHardwareFlavorBuilder.gbDisk(0F).build();
    }

    @Test public void getIdTest() {
        assertThat(validHardwareFlavor.id(), equalTo(testId));
    }

    @Test public void getProviderIdTest() {
        assertThat(validHardwareFlavor.providerId(), equalTo(testProviderId));
    }

    @Test public void getCoresTest() {
        assertThat(validHardwareFlavor.numberOfCores(), equalTo(testCores));
    }

    @Test public void getRamTest() {
        assertThat(validHardwareFlavor.mbRam(), equalTo(testRam));
    }

    @Test public void getNameTest() {
        assertThat(validHardwareFlavor.name(), equalTo(testName));
    }

    @Test public void getDiskTest() {
        assertThat(validHardwareFlavor.gbDisk, equalTo(testDisk));
    }

    @Test public void getLocationTest() {
        assertThat(validHardwareFlavor.location().get(), equalTo(testLocation));
    }

    @Test public void toStringTest() {
        String string = validHardwareFlavor.toString();
        assertThat(string.contains(testId), equalTo(true));
        assertThat(string.contains(testName), equalTo(true));
        assertThat(string.contains(String.valueOf(testCores)), equalTo(true));
        assertThat(string.contains(String.valueOf(testRam)), equalTo(true));
        assertThat(string.contains(String.valueOf(testDisk)), equalTo(true));
    }

}

