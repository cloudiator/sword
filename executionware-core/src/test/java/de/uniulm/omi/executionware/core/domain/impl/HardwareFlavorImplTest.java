/*
 * Copyright (c) 2014 University of Ulm
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

package de.uniulm.omi.executionware.core.domain.impl;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by daniel on 03.12.14.
 */
public class HardwareFlavorImplTest {

    private final String idTest = "123456";
    private final int coresTest = 1;
    private final int ramTest = 1024;
    private HardwareFlavorImpl hardwareFlavor;

    @Before
    public void before() {
        this.hardwareFlavor = new HardwareFlavorImpl(this.idTest, this.coresTest, this.ramTest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void coresNotZeroTest() {
        new HardwareFlavorImpl(this.idTest, 0, this.ramTest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void coresNotNegativeTest() {
        new HardwareFlavorImpl(this.idTest, -1, this.ramTest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ramNotZeroTest() {
        new HardwareFlavorImpl(this.idTest, this.coresTest, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ramNotNegativeTest() {
        new HardwareFlavorImpl(this.idTest, this.coresTest, -1);
    }

    @Test(expected = NullPointerException.class)
    public void idNotNullTest() {
        new HardwareFlavorImpl(null, this.coresTest, this.ramTest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void idNotEmptyTest() {
        new HardwareFlavorImpl("", this.coresTest, this.ramTest);
    }

    @Test
    public void getIdTest() {
        assertThat(hardwareFlavor.getId(), equalTo(this.idTest));
    }

    @Test
    public void getCoresTest() {
        assertThat(hardwareFlavor.numberOfCores(), equalTo(this.coresTest));
    }

    @Test
    public void getRamTest() {
        assertThat(hardwareFlavor.mbRam(), equalTo(this.ramTest));
    }

}

