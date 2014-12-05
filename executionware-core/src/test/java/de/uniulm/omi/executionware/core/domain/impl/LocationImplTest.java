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
import static org.hamcrest.Matchers.nullValue;

/**
 * Created by daniel on 03.12.14.
 */
public class LocationImplTest {

    private final String idTest = "123456";
    private final String descriptionTest = "This is a very fine location";
    private LocationImpl location;

    @Before
    public void before() {
        this.location = new LocationImpl(this.idTest, this.descriptionTest);
    }

    @Test(expected = NullPointerException.class)
    public void idNotNullableTest() {
        final LocationImpl location = new LocationImpl(null, this.descriptionTest);
    }

    @Test
    public void descriptionNullTest() {
        final LocationImpl location = new LocationImpl(this.idTest, null);
    }

    @Test
    public void getIdTest() {
        assertThat(this.location.getId(), equalTo(this.idTest));
    }

    @Test
    public void getDescriptionTest() {
        assertThat(this.location.getDescription(), equalTo(this.descriptionTest));
    }

    @Test
    public void getDescriptionNullTest() {
        final LocationImpl location = new LocationImpl(this.idTest, null);
        assertThat(location.getDescription(), nullValue());
    }

}
