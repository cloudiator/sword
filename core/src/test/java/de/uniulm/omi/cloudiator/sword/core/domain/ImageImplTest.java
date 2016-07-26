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

import de.uniulm.omi.cloudiator.common.os.*;
import de.uniulm.omi.cloudiator.sword.api.domain.Image;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.LocationScope;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by daniel on 03.12.14.
 */
public class ImageImplTest {

    private final String testId = "123456";
    private final String testProviderId = "providerId";
    private final String testName = "name";
    private final Location testLocation =
        LocationBuilder.newBuilder().id("test").name("test").parent(null).assignable(true)
            .scope(LocationScope.REGION).build();
    private final OperatingSystem testOs =
        OperatingSystemBuilder.newBuilder().architecture(OperatingSystemArchitecture.AMD64)
            .family(OperatingSystemFamily.UBUNTU).version(OperatingSystemVersion.of(1404, "14.04"))
            .build();
    private Image validImage;
    private ImageBuilder validImageBuilder;

    @Before public void before() {

        validImageBuilder =
            ImageBuilder.newBuilder().id(testId).providerId(testProviderId).name(testName)
                .location(testLocation).os(testOs);
        validImage = validImageBuilder.build();
    }

    @Test(expected = NullPointerException.class) public void idNotNullableTest() {
        validImageBuilder.id(null).build();
    }

    @Test(expected = IllegalArgumentException.class) public void idNotEmptyTest() {
        validImageBuilder.id("").build();
    }

    @Test(expected = NullPointerException.class) public void providerIdNotNullableTest() {
        validImageBuilder.providerId(null).build();
    }

    @Test(expected = IllegalArgumentException.class) public void providerIdNotEmptyTest() {
        validImageBuilder.providerId("").build();
    }

    @Test(expected = NullPointerException.class) public void nameNotNullableTest() {
        validImageBuilder.name(null).build();
    }

    @Test(expected = IllegalArgumentException.class) public void nameNotEmptyTest() {
        validImageBuilder.name("").build();
    }

    @Test public void getOperatingSystemTest() {
        Image underTest = validImage;
        assertThat(underTest.operatingSystem().isPresent(), equalTo(true));
        assertThat(underTest.operatingSystem().get(), equalTo(testOs));
        underTest = validImageBuilder.os(null).build();
        assertThat(underTest.operatingSystem().isPresent(), equalTo(false));
    }

    @Test public void getIdTest() {
        assertThat(validImage.id(), equalTo(testId));
    }

    @Test public void getProviderIdTest() {
        assertThat(validImage.providerId(), equalTo(testProviderId));
    }

    @Test public void getNameTest() {
        assertThat(validImage.name(), equalTo(testName));
    }

    @Test public void getLocationTest() {
        assertThat(validImage.location().get(), equalTo(testLocation));
    }

}
