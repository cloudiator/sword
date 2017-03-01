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

import org.junit.Test;

import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by daniel on 18.01.17.
 */
public class ApiModelImplTest {

    @Test public void testProviderName() {
        final Api api = ApiBuilder.newBuilder().providerName("providerName").build();
        assertThat(api.providerName(), is(equalTo("providerName")));
    }

    @Test(expected = NullPointerException.class)
    public void testNullProviderNameCausesNullPointerException() {
        ApiBuilder.newBuilder().providerName(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyProviderNameCausesIllegalArgumentException() {
        ApiBuilder.newBuilder().providerName("").build();
    }

    @Test public void testEquals() {
        assertTrue(ApiBuilder.newBuilder().providerName("providerName").build()
            .equals(ApiBuilder.newBuilder().providerName("providerName").build()));
        assertFalse(ApiBuilder.newBuilder().providerName("providerName").build()
            .equals(ApiBuilder.newBuilder().providerName("hello World!").build()));
        assertFalse(
            ApiBuilder.newBuilder().providerName("providerName").build().equals(new HashSet<>()));
    }

}
