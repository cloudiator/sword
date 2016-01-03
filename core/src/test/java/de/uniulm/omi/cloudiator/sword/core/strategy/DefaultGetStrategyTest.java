/*
 * Copyright (c) 2014-2015 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.core.strategy;


import com.google.common.base.Supplier;
import de.uniulm.omi.cloudiator.sword.api.domain.Identifiable;
import de.uniulm.omi.cloudiator.sword.api.strategy.GetStrategy;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

/**
 * Created by daniel on 29.07.15.
 */
public class DefaultGetStrategyTest {

    private GetStrategy<String, Identifiable> getStrategy;
    private final Identifiable resourceToRetrieve = new TestResource("1");

    @Before public void setUp() throws Exception {
        Supplier<Set<Identifiable>> resourceSupplier = () -> {
            Set<Identifiable> resourceSet = new HashSet<>(2);
            resourceSet.add(resourceToRetrieve);
            resourceSet.add(new TestResource("2"));
            return resourceSet;
        };
        this.getStrategy = new DefaultGetStrategy<>(resourceSupplier);
    }

    @Test public void testGet() throws Exception {
        assertThat(getStrategy.get("1"), equalTo(resourceToRetrieve));
    }

    @Test public void testGetNull() throws Exception {
        assertThat(getStrategy.get("3"), nullValue());
    }

    @Test(expected = NullPointerException.class) public void testGetThrowsNullPointerException()
        throws Exception {
        getStrategy.get(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetThrowsIllegalArgumentException() {
        getStrategy.get("");
    }

    private final static class TestResource implements Identifiable {

        private final String id;

        public TestResource(String id) {
            this.id = id;
        }

        @Override public String id() {
            return id;
        }
    }
}