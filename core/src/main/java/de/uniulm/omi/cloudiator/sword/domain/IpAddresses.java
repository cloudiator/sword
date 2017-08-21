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

import de.uniulm.omi.cloudiator.sword.domain.IpAddress.IpAddressType;
import de.uniulm.omi.cloudiator.sword.domain.IpAddress.IpVersion;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpAddresses {

  private IpAddresses() {
    throw new AssertionError("Do not instantiate.");
  }

  public static IpAddress of(String ip) {
    try {
      InetAddress inetAddress = InetAddress.getByName(ip);
      IpAddressType ipAddressType;
      IpVersion ipVersion;

      if (inetAddress instanceof Inet4Address) {
        ipVersion = IpVersion.V4;
      } else if (inetAddress instanceof Inet6Address) {
        ipVersion = IpVersion.V6;
      } else {
        throw new IllegalStateException("Could not determine ip version of " + inetAddress);
      }

      if (inetAddress.isSiteLocalAddress()) {
        ipAddressType = IpAddressType.PRIVATE;
      } else {
        ipAddressType = IpAddressType.PUBLIC;
      }

      return new IpAddressImpl(ip, ipAddressType, ipVersion);

    } catch (UnknownHostException e) {
      throw new IllegalArgumentException(String.format("%s is an illegal ip address.", ip), e);
    }
  }

}
