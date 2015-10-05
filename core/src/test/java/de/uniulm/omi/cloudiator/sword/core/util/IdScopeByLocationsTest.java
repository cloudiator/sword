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

package de.uniulm.omi.cloudiator.sword.core.util;

import de.uniulm.omi.cloudiator.sword.api.util.IdScopedByLocation;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by daniel on 30.07.15.
 */
public class IdScopeByLocationsTest {

    private String testLocation = "location";
    private String testId = "id";
    private String testScopedId = "location/id";

    @Test public void testFromScopeAndId() throws Exception {
        final IdScopedByLocation idScopedByLocationWithLocation =
            IdScopeByLocations.from(testLocation, testId);
        assertThat(idScopedByLocationWithLocation.getId(), equalTo(testId));
        assertThat(idScopedByLocationWithLocation.getIdWithLocation(), equalTo(testScopedId));
        assertThat(idScopedByLocationWithLocation.getLocationId(), equalTo(testLocation));

        final IdScopedByLocation idScopedByLocationWithoutLocation =
            IdScopeByLocations.from(null, testId);
        assertThat(idScopedByLocationWithoutLocation.getId(), equalTo(testId));
        assertThat(idScopedByLocationWithoutLocation.getIdWithLocation(), equalTo(testId));
        assertThat(idScopedByLocationWithoutLocation.getLocationId(), nullValue());
    }

    @Test(expected = NullPointerException.class) public void testFromScopeAndIdRejectsNullId() {
        IdScopeByLocations.from(testLocation, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromScopeAndIdRejectsEmptyId() {
        IdScopeByLocations.from(testLocation, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromScopeAndIdRejectsEmptyLocation() {
        IdScopeByLocations.from("", testId);
    }

    @Test public void testFromId() throws Exception {
        final IdScopedByLocation idScopedByLocationWithLocation =
            IdScopeByLocations.from(testScopedId);
        assertThat(idScopedByLocationWithLocation.getId(), equalTo(testId));
        assertThat(idScopedByLocationWithLocation.getIdWithLocation(), equalTo(testScopedId));
        assertThat(idScopedByLocationWithLocation.getLocationId(), equalTo(testLocation));

        final IdScopedByLocation idScopedByLocationWithoutLocation =
            IdScopeByLocations.from(testId);
        assertThat(idScopedByLocationWithoutLocation.getId(), equalTo(testId));
        assertThat(idScopedByLocationWithoutLocation.getIdWithLocation(), equalTo(testId));
        assertThat(idScopedByLocationWithoutLocation.getLocationId(), nullValue());
    }

    @Test(expected = NullPointerException.class) public void testFromIdRejectsNullId() {
        IdScopeByLocations.from(null);
    }

    @Test(expected = IllegalArgumentException.class) public void testFromIdRejectsEmptyId() {
        IdScopeByLocations.from("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromIdRejectsMultipleDelimiters() {
        IdScopeByLocations.from(testScopedId + "/test");
    }
}
