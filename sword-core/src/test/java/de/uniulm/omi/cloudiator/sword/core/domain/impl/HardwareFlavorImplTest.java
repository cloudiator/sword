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

package de.uniulm.omi.cloudiator.sword.core.domain.impl;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by daniel on 03.12.14.
 */
public class HardwareFlavorImplTest {

    private final String idTest = "123456";
    private final String nameTest = "name";
    private final int coresTest = 1;
    private final int ramTest = 1024;
    private HardwareFlavorImpl hardwareFlavor;

    @Before
    public void before() {
        this.hardwareFlavor = new HardwareFlavorImpl(idTest, nameTest, coresTest, ramTest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void coresNotZeroTest() {
        new HardwareFlavorImpl(idTest, nameTest, 0, ramTest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void coresNotNegativeTest() {
        new HardwareFlavorImpl(idTest, nameTest, -1, ramTest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ramNotZeroTest() {
        new HardwareFlavorImpl(idTest, nameTest, coresTest, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ramNotNegativeTest() {
        new HardwareFlavorImpl(idTest, nameTest, coresTest, -1);
    }

    @Test(expected = NullPointerException.class)
    public void idNotNullTest() {
        new HardwareFlavorImpl(null, nameTest, coresTest, ramTest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void idNotEmptyTest() {
        new HardwareFlavorImpl("", nameTest, coresTest, ramTest);
    }

    @Test(expected = NullPointerException.class)
    public void nameNotNullTest() {
        new HardwareFlavorImpl(idTest, null, coresTest, ramTest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nameNotEmptyTest() {
        new HardwareFlavorImpl(idTest, "", coresTest, ramTest);
    }

    @Test
    public void getIdTest() {
        assertThat(hardwareFlavor.id(), equalTo(idTest));
    }

    @Test
    public void getCoresTest() {
        assertThat(hardwareFlavor.numberOfCores(), equalTo(coresTest));
    }

    @Test
    public void getRamTest() {
        assertThat(hardwareFlavor.mbRam(), equalTo(ramTest));
    }

    @Test
    public void getNameTest() {
        assertThat(hardwareFlavor.name(), equalTo(nameTest));
    }

}

