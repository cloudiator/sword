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

package de.uniulm.omi.executionware.core.domain.impl;

import de.uniulm.omi.executionware.core.domain.builders.LocationBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

/**
 * Created by daniel on 03.12.14.
 */
public class LocationImplTest {

    private final String idTest = "123456";
    private final String nameTest = "This is a very fine location";
    private final boolean assignableTest = true;
    private LocationImpl location;

    @Before
    public void before() {
        this.location = LocationBuilder
                .newBuilder()
                .id(idTest)
                .name(nameTest)
                .assignable(assignableTest)
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void idNotNullableTest() {
        final LocationImpl location = LocationBuilder
                .newBuilder()
                .id(null)
                .name(nameTest)
                .assignable(assignableTest)
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void nameNotNullableTest() {
        final LocationImpl location = LocationBuilder
                .newBuilder()
                .id(idTest)
                .name(null)
                .assignable(assignableTest)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void idNotEmptyTest() {
        final LocationImpl location = LocationBuilder
                .newBuilder()
                .id("")
                .name(nameTest)
                .assignable(assignableTest)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void nameNotEmptyTest() {
        final LocationImpl location = LocationBuilder
                .newBuilder()
                .id(idTest)
                .name("")
                .assignable(assignableTest)
                .build();
    }

    @Test
    public void getIdTest() {
        assertThat(this.location.id(), equalTo(this.idTest));
    }

    @Test
    public void getAssignableTest() {
        assertThat(this.location.isAssignable(), equalTo(this.assignableTest));
    }

    @Test
    public void getNameTest() {
        assertThat(this.location.name(), equalTo(this.nameTest));
    }

    @Test
    public void toStringTest() {
        assertThat(this.location.toString().contains(this.idTest), equalTo(true));
        assertThat(this.location.toString().contains(this.nameTest), equalTo(true));
        assertThat(this.location.toString().contains(String.valueOf(this.assignableTest)), equalTo(true));
    }

}
