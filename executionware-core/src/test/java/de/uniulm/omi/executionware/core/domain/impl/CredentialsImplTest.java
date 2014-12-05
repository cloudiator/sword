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
 * Created by daniel on 04.12.14.
 */
public class CredentialsImplTest {

    private String userTest = "user";
    private String passwordTest = "secret123?password";
    private CredentialsImpl credentials;

    @Before
    public void before() {
        this.credentials = new CredentialsImpl(this.userTest, this.passwordTest);
    }

    @Test(expected = NullPointerException.class)
    public void userNotNullTest() {
        final CredentialsImpl credentials = new CredentialsImpl(null, this.passwordTest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userNotEmptyTest() {
        final CredentialsImpl credentials = new CredentialsImpl("", this.passwordTest);
    }

    @Test(expected = NullPointerException.class)
    public void passwordNotNullTest() {
        final CredentialsImpl credentials = new CredentialsImpl(this.userTest, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void passwordNotEmptyTest() {
        final CredentialsImpl credentials = new CredentialsImpl(this.userTest, "");
    }

    @Test
    public void getUserTest() {
        assertThat(this.credentials.getUser(), equalTo(this.userTest));
    }

    @Test
    public void getPasswordTest() {
        assertThat(this.credentials.getPassword(), equalTo(this.passwordTest));
    }
}
