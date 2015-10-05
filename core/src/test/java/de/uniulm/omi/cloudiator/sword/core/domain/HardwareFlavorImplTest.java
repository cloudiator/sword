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

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

/**
 * Created by daniel on 03.12.14.
 */
public class HardwareFlavorImplTest {

    private final String idTest = "123456";
    private final String nameTest = "name";
    private final int coresTest = 1;
    private final long ramTest = 1024;
    private final Float diskTest = 1.024F;
    private HardwareFlavorImpl hardwareFlavor;

    @Before public void before() {
        this.hardwareFlavor =
            HardwareFlavorBuilder.newBuilder().id(idTest).name(nameTest).cores(coresTest)
                .mbRam(ramTest).gbDisk(diskTest).build();
    }

    @Test(expected = IllegalArgumentException.class) public void coresNotZeroTest() {
        HardwareFlavorBuilder.newBuilder().id(idTest).name(nameTest).cores(0).mbRam(ramTest)
            .build();
    }

    @Test(expected = IllegalArgumentException.class) public void coresNotNegativeTest() {
        HardwareFlavorBuilder.newBuilder().id(idTest).name(nameTest).cores(-1).mbRam(ramTest)
            .build();
    }

    @Test(expected = IllegalArgumentException.class) public void ramNotZeroTest() {
        HardwareFlavorBuilder.newBuilder().id(idTest).name(nameTest).cores(coresTest).mbRam(0)
            .build();
    }

    @Test(expected = IllegalArgumentException.class) public void ramNotNegativeTest() {
        HardwareFlavorBuilder.newBuilder().id(idTest).name(nameTest).cores(coresTest).mbRam(-1)
            .build();
    }

    @Test(expected = NullPointerException.class) public void idNotNullTest() {
        HardwareFlavorBuilder.newBuilder().id(null).name(nameTest).cores(coresTest).mbRam(ramTest)
            .build();
    }

    @Test(expected = IllegalArgumentException.class) public void idNotEmptyTest() {
        HardwareFlavorBuilder.newBuilder().id("").name(nameTest).cores(coresTest).mbRam(ramTest)
            .build();
    }

    @Test(expected = NullPointerException.class) public void nameNotNullTest() {
        HardwareFlavorBuilder.newBuilder().id(idTest).name(null).cores(coresTest).mbRam(ramTest)
            .build();
    }

    @Test(expected = IllegalArgumentException.class) public void nameNotEmptyTest() {
        HardwareFlavorBuilder.newBuilder().id(idTest).name("").cores(coresTest).mbRam(ramTest)
            .build();
    }

    @Test public void diskNullTest() {
        final HardwareFlavorImpl hardwareFlavor =
            HardwareFlavorBuilder.newBuilder().id(idTest).name(nameTest).cores(coresTest)
                .mbRam(ramTest).gbDisk(null).build();
        assertThat(hardwareFlavor.gbDisk(), nullValue());
    }

    @Test(expected = IllegalArgumentException.class) public void diskNotGreaterZeroTest() {
        HardwareFlavorBuilder.newBuilder().id(idTest).name(nameTest).cores(coresTest).mbRam(ramTest)
            .gbDisk(0F).build();
    }

    @Test public void getIdTest() {
        assertThat(hardwareFlavor.id(), equalTo(idTest));
    }

    @Test public void getCoresTest() {
        assertThat(hardwareFlavor.numberOfCores(), equalTo(coresTest));
    }

    @Test public void getRamTest() {
        assertThat(hardwareFlavor.mbRam(), equalTo(ramTest));
    }

    @Test public void getNameTest() {
        assertThat(hardwareFlavor.name(), equalTo(nameTest));
    }

    @Test public void getDiskTest() {
        assertThat(hardwareFlavor.gbDisk, equalTo(diskTest));
    }

}

