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

package de.uniulm.omi.cloudiator.sword.api.domain;

import org.junit.Test;

import java.util.Iterator;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by daniel on 17.01.17.
 */
public class LocationScopeTest {

    @Test public void testParents() {
        assertThat(LocationScope.PROVIDER.parents(), empty());
        assertThat(LocationScope.HOST.parents(), hasItem(LocationScope.ZONE));
        assertThat(LocationScope.HOST.parents(), hasItem(LocationScope.REGION));
        assertThat(LocationScope.HOST.parents(), hasItem(LocationScope.PROVIDER));
    }

    @Test public void testParent() {
        assertThat(LocationScope.PROVIDER.parent(), is(equalTo(Optional.empty())));
        assertThat(LocationScope.HOST.parent().get(), is(equalTo(LocationScope.ZONE)));
    }

    @Test public void testHasParent() {
        assertFalse(LocationScope.PROVIDER.hasParent(LocationScope.ZONE));
        assertTrue(LocationScope.HOST.hasParent(LocationScope.REGION));
        assertTrue(LocationScope.REGION.hasParent(LocationScope.PROVIDER));
        assertTrue(LocationScope.ZONE.hasParent(LocationScope.PROVIDER));
        assertFalse(LocationScope.ZONE.hasParent(LocationScope.HOST));
    }

    @Test public void testIteration() {
        final Iterator hostIterator = LocationScope.HOST.iterator();
        assertTrue(hostIterator.hasNext());
        assertThat(hostIterator.next(), is(equalTo(LocationScope.HOST)));
        assertTrue(hostIterator.hasNext());
        assertThat(hostIterator.next(), is(equalTo(LocationScope.ZONE)));
        assertTrue(hostIterator.hasNext());
        assertThat(hostIterator.next(), is(equalTo(LocationScope.REGION)));
        assertTrue(hostIterator.hasNext());
        assertThat(hostIterator.next(), is(equalTo(LocationScope.PROVIDER)));
        assertFalse(hostIterator.hasNext());
    }

}
