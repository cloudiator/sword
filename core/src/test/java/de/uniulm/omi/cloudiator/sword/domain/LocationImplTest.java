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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import de.uniulm.omi.cloudiator.domain.LocationScope;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link LocationImpl} resp. {@link LocationBuilder}.
 */
public class LocationImplTest {

  private final String testId = "123456";
  private final String testName = "This is a very fine location";
  private final boolean testAssignable = true;
  private final LocationScope testScope = LocationScope.HOST;
  private final Location testParent =
      LocationBuilder.newBuilder().id("test").name("test").parent(null).assignable(true)
          .scope(LocationScope.REGION).build();
  private Location validLocation;

  @Before
  public void before() {
    this.validLocation =
        LocationBuilder.newBuilder().id(testId).name(testName).assignable(testAssignable)
            .parent(testParent).scope(testScope).build();
  }

  @Test(expected = NullPointerException.class)
  public void idNotNullableTest() {

    LocationBuilder.newBuilder().id(null).name(testName).assignable(testAssignable)
        .parent(testParent).scope(testScope).build();
  }

  @Test(expected = NullPointerException.class)
  public void nameNotNullableTest() {

    LocationBuilder.newBuilder().id(testId).name(null).assignable(testAssignable)
        .parent(testParent).scope(testScope).build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void idNotEmptyTest() {

    LocationBuilder.newBuilder().id("").name(testName).assignable(testAssignable)
        .parent(testParent).scope(testScope).build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nameNotEmptyTest() {

    LocationBuilder.newBuilder().id(testId).name("").assignable(testAssignable)
        .parent(testParent).scope(testScope).build();
  }

  @Test(expected = NullPointerException.class)
  public void locationScopeNotNullTest() {
    LocationBuilder.newBuilder().id(testId).name(testName).assignable(testAssignable)
        .parent(testParent).scope(null).build();
  }

  @Test
  public void getIdTest() {
    assertThat(validLocation.id(), equalTo(testId));
  }

  @Test
  public void getAssignableTest() {
    assertThat(validLocation.isAssignable(), equalTo(testAssignable));
  }

  @Test
  public void getNameTest() {
    assertThat(validLocation.name(), equalTo(testName));
  }

  @Test
  public void getParentTest() {
    assertThat(validLocation.parent().get(), equalTo(testParent));
  }

  @Test
  public void getLocationScopeTest() {
    assertThat(validLocation.locationScope(), equalTo(testScope));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLocationScopeOfParentIsLarger() {
    Location parent =
        LocationBuilder.newBuilder().id("43824302").scope(LocationScope.ZONE).name("parent")
            .build();
    Location child = LocationBuilder.newBuilder().id("3724387492").scope(LocationScope.PROVIDER)
        .name("child").parent(parent).build();
  }

  @Test
  public void toStringTest() {
    assertThat(this.validLocation.toString().contains(testId), equalTo(true));
    assertThat(this.validLocation.toString().contains(testName), equalTo(true));
    assertThat(this.validLocation.toString().contains(String.valueOf(testAssignable)),
        equalTo(true));
    assertThat(this.validLocation.toString().contains(testScope.name()), equalTo(true));
  }

}
