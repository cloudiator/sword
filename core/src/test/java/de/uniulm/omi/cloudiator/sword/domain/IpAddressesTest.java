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
import static org.hamcrest.core.IsEqual.equalTo;

import de.uniulm.omi.cloudiator.sword.domain.IpAddress.IpAddressType;
import de.uniulm.omi.cloudiator.sword.domain.IpAddress.IpVersion;
import org.junit.Test;

public class IpAddressesTest {

  @Test
  public void testPrivateIPV4() {
    final IpAddress ipAddress = IpAddresses.of("192.168.1.1");
    assertThat(ipAddress.type(), equalTo(IpAddressType.PRIVATE));
    assertThat(ipAddress.version(), equalTo(IpVersion.V4));
  }

  @Test
  public void testPublicIpV4() {
    final IpAddress ipAddress = IpAddresses.of("141.59.27.143");
    assertThat(ipAddress.type(), equalTo(IpAddressType.PUBLIC));
    assertThat(ipAddress.version(), equalTo(IpVersion.V4));
  }

  @Test
  public void testPublicIpV6() {
    final IpAddress ipAddress = IpAddresses.of("2a00:1450:400e:805::2003");
    assertThat(ipAddress.type(), equalTo(IpAddressType.PUBLIC));
    assertThat(ipAddress.version(), equalTo(IpVersion.V6));
  }

  @Test
  public void testPrivateIpV6() {
    //todo
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalIpV4() {
    IpAddresses.of("17.2.3.4.6");
  }

}
