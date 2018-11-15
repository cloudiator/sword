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

import com.google.common.base.MoreObjects;
import java.io.IOException;
import java.net.InetAddress;

public class IpAddressImpl implements IpAddress {

  private final String ip;
  private final IpAddressType type;
  private final IpVersion version;

  IpAddressImpl(String ip,
      IpAddressType type, IpVersion version) {
    this.ip = ip;
    this.type = type;
    this.version = version;
  }

  @Override
  public String ip() {
    return ip;
  }

  @Override
  public IpAddressType type() {
    return type;
  }

  @Override
  public IpVersion version() {
    return version;
  }

  @Override
  public boolean isPingable() {
    try {
      return InetAddress.getByName(ip()).isReachable(5000);
    } catch (IOException e) {
      return false;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    IpAddressImpl ipAddress = (IpAddressImpl) o;

    return ip.equals(ipAddress.ip);
  }

  @Override
  public int hashCode() {
    return ip.hashCode();
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("ip", ip).add("type", type).add("version", version)
        .toString();
  }
}
