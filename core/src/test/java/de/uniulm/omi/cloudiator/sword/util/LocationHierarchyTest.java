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

package de.uniulm.omi.cloudiator.sword.util;

import de.uniulm.omi.cloudiator.domain.Location;
import de.uniulm.omi.cloudiator.domain.LocationScope;
import de.uniulm.omi.cloudiator.domain.LocationBuilder;
import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by daniel on 17.01.17.
 */
public class LocationHierarchyTest {

    @Test(expected = NullPointerException.class) public void nullLocationRejected() {
        LocationHierarchy.of(null);
    }

    @Test public void testTopMostLocation() {
        final Location region =
            LocationBuilder.newBuilder().id("5732752").name("region").scope(LocationScope.REGION)
                .build();
        final Location zone =
            LocationBuilder.newBuilder().id("37589327592").name("zone").scope(LocationScope.ZONE)
                .parent(region).build();
        final Location host =
            LocationBuilder.newBuilder().id("57472592759").name("host").scope(LocationScope.HOST)
                .parent(zone).build();
        final LocationHierarchy regionHierarchy = LocationHierarchy.of(region);
        final LocationHierarchy zoneHierarchy = LocationHierarchy.of(zone);
        final LocationHierarchy hostHierarchy = LocationHierarchy.of(host);
        assertThat(regionHierarchy.topmostLocation(), is(equalTo(region)));
        assertThat(zoneHierarchy.topmostLocation(), is(equalTo(region)));
        assertThat(hostHierarchy.topmostLocation(), is(equalTo(region)));
    }

    @Test public void testFirstParentWithLocationScope() {
        final Location region =
            LocationBuilder.newBuilder().id("37483749").name("region").scope(LocationScope.REGION)
                .build();
        final Location zone =
            LocationBuilder.newBuilder().id("748372894").name("zone").scope(LocationScope.ZONE)
                .parent(region).build();
        final Location host =
            LocationBuilder.newBuilder().id("4738724932").name("host").scope(LocationScope.HOST)
                .parent(zone).build();
        final LocationHierarchy regionHierarchy = LocationHierarchy.of(region);
        final LocationHierarchy zoneHierarchy = LocationHierarchy.of(zone);
        final LocationHierarchy hostHierarchy = LocationHierarchy.of(host);

        assertThat(regionHierarchy.firstParentLocationWithScope(LocationScope.REGION).get(),
            is(equalTo(region)));
        assertThat(regionHierarchy.firstParentLocationWithScope(LocationScope.PROVIDER),
            is(equalTo(Optional.empty())));

        assertThat(zoneHierarchy.firstParentLocationWithScope(LocationScope.REGION).get(),
            is(equalTo(region)));

        assertThat(hostHierarchy.firstParentLocationWithScope(LocationScope.HOST).get(),
            is(equalTo(host)));
        assertThat(hostHierarchy.firstParentLocationWithScope(LocationScope.ZONE).get(),
            is(equalTo(zone)));
        assertThat(hostHierarchy.firstParentLocationWithScope(LocationScope.REGION).get(),
            is(equalTo(region)));
    }

    @Test public void testIteration() {
        final Location last =
            LocationBuilder.newBuilder().id("5737523").name("last").scope(LocationScope.ZONE)
                .build();
        final Location first =
            LocationBuilder.newBuilder().id("57375932").name("first").scope(LocationScope.HOST)
                .parent(last).build();

        final LocationHierarchy hierarchy = LocationHierarchy.of(first);

        //checking iteration using for loop
        List<Location> locations = new LinkedList<>();
        for (Location location : hierarchy) {
            locations.add(location);
        }
        assertThat(locations.get(0), is(equalTo(first)));
        assertThat(locations.get(1), is(equalTo(last)));

        //checking iteration directly using iterator.
        final Iterator<Location> iterator = hierarchy.iterator();
        assertTrue(iterator.hasNext());
        assertThat(iterator.next(), is(equalTo(first)));
        assertTrue(iterator.hasNext());
        assertThat(iterator.next(), is(equalTo(last)));
        assertFalse(iterator.hasNext());
    }



}
