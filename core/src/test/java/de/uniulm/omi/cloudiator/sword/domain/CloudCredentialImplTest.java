/*
 * Copyright (c) 2014-2018 University of Ulm
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by daniel on 04.12.14.
 */
public class CloudCredentialImplTest {

  private String userTest = "user";
  private String passwordTest = "secret123?password";
  private CloudCredential cloudCredential;

  @Before
  public void before() {
    this.cloudCredential =
        CredentialsBuilder.newBuilder().user(userTest).password(passwordTest).build();
  }

  @Test(expected = NullPointerException.class)
  public void userNotNullTest() {
    CredentialsBuilder.newBuilder().user(null).password(this.passwordTest).build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void userNotEmptyTest() {
    CredentialsBuilder.newBuilder().user("").password(passwordTest).build();
  }

  @Test(expected = NullPointerException.class)
  public void passwordNotNullTest() {
    CredentialsBuilder.newBuilder().user(userTest).password(null).build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void passwordNotEmptyTest() {
    CredentialsBuilder.newBuilder().user(userTest).password("").build();
  }

  @Test
  public void getUserTest() {
    assertThat(this.cloudCredential.user(), equalTo(this.userTest));
  }

  @Test
  public void getPasswordTest() {
    assertThat(this.cloudCredential.password(), equalTo(this.passwordTest));
  }

  @Test
  public void testEquals() {
    assertTrue(CredentialsBuilder.newBuilder().password(passwordTest).user(userTest).build()
        .equals(CredentialsBuilder.newBuilder().password(passwordTest).user(userTest).build()));
    assertFalse(CredentialsBuilder.newBuilder().password(passwordTest).user(userTest).build()
        .equals(
            CredentialsBuilder.newBuilder().password("wrongPassword").user(userTest).build()));
    assertFalse(CredentialsBuilder.newBuilder().password(passwordTest).user(userTest).build()
        .equals(
            CredentialsBuilder.newBuilder().password(passwordTest).user("wrongUser").build()));
    assertFalse(CredentialsBuilder.newBuilder().password(passwordTest).user(userTest).build()
        .equals(new HashSet<>()));
  }
}
