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

import de.uniulm.omi.cloudiator.sword.api.domain.Image;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by daniel on 03.12.14.
 */
public class ImageImplTest {

    private final String testId = "123456";
    private final String testName = "name";
    private final Location testLocation =
        LocationBuilder.newBuilder().id("test").name("test").parent(null).assignable(true).build();
    private Image validImage;

    @Before public void before() {
        validImage =
            ImageBuilder.newBuilder().id(testId).name(testName).location(testLocation).build();
    }

    @Test(expected = NullPointerException.class) public void idNotNullableTest() {
        ImageBuilder.newBuilder().id(null).name(testName).location(testLocation).build();
    }

    @Test(expected = IllegalArgumentException.class) public void idNotEmptyTest() {
        ImageBuilder.newBuilder().id("").name(testName).location(testLocation).build();
    }

    @Test(expected = NullPointerException.class) public void nameNotNullableTest() {
        ImageBuilder.newBuilder().id(testId).name(null).location(testLocation).build();
    }

    @Test(expected = IllegalArgumentException.class) public void nameNotEmptyTest() {
        ImageBuilder.newBuilder().id(testId).name("").location(testLocation).build();
    }


    @Test public void getIdTest() {
        assertThat(validImage.id(), equalTo(testId));
    }

    @Test public void getNameTest() {
        assertThat(validImage.name(), equalTo(testName));
    }

    @Test public void getLocationTest() {
        assertThat(validImage.location().get(), equalTo(testLocation));
    }

}
