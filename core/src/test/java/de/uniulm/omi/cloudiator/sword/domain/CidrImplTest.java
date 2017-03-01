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

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by daniel on 12.01.17.
 */
public class CidrImplTest {

    @Test public void getAddressTest() {
        final Cidr cidr = CidrImpl.of("192.168.1.1", 24);
        assertThat(cidr.address(), is(equalTo("192.168.1.1")));
    }

    @Test public void getSlashTest() {
        final Cidr cidr = CidrImpl.of("192.168.1.1", 24);
        assertThat(cidr.slash(), is(equalTo(24)));
    }

    @Test public void toStringTest() {
        final Cidr cidr = CidrImpl.of("192.168.1.1", 24);
        assertThat(cidr.toString(), is(equalTo("192.168.1.1/24")));
    }

    @Test(expected = IllegalArgumentException.class) public void cidrWithoutSlashRejected() {
        CidrImpl.of("0.0.0.0");
    }

    @Test(expected = IllegalArgumentException.class) public void cidrWithMultipleSlashRejected() {
        CidrImpl.of("0.0.0.0/24/24");
    }

    @Test(expected = IllegalArgumentException.class) public void cidrWithNonIntegerSlashRejected() {
        CidrImpl.of("0.0.0.0/test");
    }

    @Test(expected = IllegalArgumentException.class) public void cidrWithToHighSlashRejected() {
        CidrImpl.of("0.0.0.0/33");
    }

    @Test(expected = IllegalArgumentException.class) public void cidrWithToLowSlashRejected() {
        CidrImpl.of("0.0.0.0/-1");
    }

    @Test(expected = IllegalArgumentException.class) public void emptySlashIsRejected() {
        CidrImpl.of("0.0.0.0/");
    }

    @Test(expected = IllegalArgumentException.class) public void addressWithMissingOctetRejected() {
        CidrImpl.of("0.0.0/24");
    }

    @Test(expected = IllegalArgumentException.class)
    public void addressWithAdditionalOctetRejected() {
        CidrImpl.of("0.0.0.0.0/24");
    }

    @Test(expected = IllegalArgumentException.class) public void addressWithToLowOctetRejected() {
        CidrImpl.of("0.-1.0.0/24");
    }

    @Test(expected = IllegalArgumentException.class) public void addressWithToHighOctetRejected() {
        CidrImpl.of("0.256.0.0/24");
    }

    @Test(expected = IllegalArgumentException.class) public void emptyAddressRejected() {
        CidrImpl.of("/24");
    }

    @Test(expected = IllegalArgumentException.class)
    public void addressWithNonIntegerOctetRejected() {
        CidrImpl.of("0.String.0.0/24");
    }

    final private String lowAddress = "0.0.0.0/0";

    @Test public void testLowAddress() {
        final Cidr cidr = CidrImpl.of(lowAddress);
        assertThat(cidr.address(), is(equalTo("0.0.0.0")));
        assertThat(cidr.slash(), equalTo(0));
    }

    final private String highAddress = "255.255.255.255/32";

    @Test public void testHighAddress() {
        final Cidr cidr = CidrImpl.of(highAddress);
        assertThat(cidr.address(), is(equalTo("255.255.255.255")));
        assertThat(cidr.slash(), is(equalTo(32)));
    }

    @Test public void testEquals() {
        assertTrue(CidrImpl.of("0.0.0.0", 32).equals(CidrImpl.of("0.0.0.0", 32)));
        assertFalse(CidrImpl.of("0.0.0.0", 24).equals(CidrImpl.of("0.0.0.0", 32)));
        assertFalse(CidrImpl.of("0.0.0.0", 32).equals(CidrImpl.of("192.168.1.0", 32)));
        assertFalse(CidrImpl.of("0.0.0.0", 32).equals(new ArrayList<>()));
    }
}
