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

package de.uniulm.omi.cloudiator.sword.core.domain;

import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.LocationScope;
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
    private final String testName = "name";
    private final int testCores = 1;
    private final long testRam = 1024;
    private final Float testDisk = 1.024F;
    private final Location testLocation =
        LocationBuilder.newBuilder().id("test").name("test").parent(null).assignable(true)
            .scope(LocationScope.REGION).build();
    private HardwareFlavorImpl validHardwareFlavor;

    @Before public void before() {
        this.validHardwareFlavor =
            HardwareFlavorBuilder.newBuilder().id(testId).name(testName).location(testLocation)
                .cores(testCores).mbRam(testRam).gbDisk(testDisk).build();
    }

    @Test(expected = IllegalArgumentException.class) public void coresNotZeroTest() {
        HardwareFlavorBuilder.newBuilder().id(testId).name(testName).location(testLocation).cores(0)
            .mbRam(testRam).build();
    }

    @Test(expected = IllegalArgumentException.class) public void coresNotNegativeTest() {
        HardwareFlavorBuilder.newBuilder().id(testId).name(testName).location(testLocation)
            .cores(-1).mbRam(testRam).build();
    }

    @Test(expected = IllegalArgumentException.class) public void ramNotZeroTest() {
        HardwareFlavorBuilder.newBuilder().id(testId).name(testName).location(testLocation)
            .cores(testCores).mbRam(0).build();
    }

    @Test(expected = IllegalArgumentException.class) public void ramNotNegativeTest() {
        HardwareFlavorBuilder.newBuilder().id(testId).name(testName).location(testLocation)
            .cores(testCores).mbRam(-1).build();
    }

    @Test(expected = NullPointerException.class) public void idNotNullTest() {
        HardwareFlavorBuilder.newBuilder().id(null).name(testName).location(testLocation)
            .cores(testCores).mbRam(testRam).build();
    }

    @Test(expected = IllegalArgumentException.class) public void idNotEmptyTest() {
        HardwareFlavorBuilder.newBuilder().id("").name(testName).location(testLocation)
            .cores(testCores).mbRam(testRam).build();
    }

    @Test(expected = NullPointerException.class) public void nameNotNullTest() {
        HardwareFlavorBuilder.newBuilder().id(testId).name(null).location(testLocation)
            .cores(testCores).mbRam(testRam).build();
    }

    @Test(expected = IllegalArgumentException.class) public void nameNotEmptyTest() {
        HardwareFlavorBuilder.newBuilder().id(testId).name("").location(testLocation)
            .cores(testCores).mbRam(testRam).build();
    }

    @Test public void diskNullTest() {
        final HardwareFlavorImpl hardwareFlavor =
            HardwareFlavorBuilder.newBuilder().id(testId).name(testName).location(testLocation)
                .cores(testCores).mbRam(testRam).gbDisk(null).build();
        assertThat(hardwareFlavor.gbDisk(), nullValue());
    }

    @Test(expected = IllegalArgumentException.class) public void diskNotGreaterZeroTest() {
        HardwareFlavorBuilder.newBuilder().id(testId).name(testName).location(testLocation)
            .cores(testCores).mbRam(testRam).gbDisk(0F).build();
    }

    @Test public void getIdTest() {
        assertThat(validHardwareFlavor.id(), equalTo(testId));
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

