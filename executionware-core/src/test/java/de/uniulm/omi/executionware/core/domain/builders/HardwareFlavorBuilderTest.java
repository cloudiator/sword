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

package de.uniulm.omi.executionware.core.domain.builders;

import de.uniulm.omi.executionware.core.domain.impl.HardwareFlavorImpl;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by daniel on 03.12.14.
 */
public class HardwareFlavorBuilderTest {

    HardwareFlavorBuilder hardwareFlavorBuilder;

    @Before
    public void before() {
        this.hardwareFlavorBuilder = new HardwareFlavorBuilder();
    }

    @Test
    public void builderTest() {
        String id = "abcdefg";
        int cores = 1;
        int mbRam = 1024;

        HardwareFlavorImpl hardwareFlavor = this.hardwareFlavorBuilder.id(id).cores(cores).mbRam(mbRam).build();
        assertThat(hardwareFlavor.getId(), equalTo(id));
        assertThat(hardwareFlavor.mbRam(), equalTo(mbRam));
        assertThat(hardwareFlavor.numberOfCores(), equalTo(cores));
    }
}
