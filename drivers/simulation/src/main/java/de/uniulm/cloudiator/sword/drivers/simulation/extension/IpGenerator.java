/*
 * Copyright (c) 2014-2019 University of Ulm
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

package de.uniulm.cloudiator.sword.drivers.simulation.extension;

import de.uniulm.omi.cloudiator.sword.domain.IpAddress;
import de.uniulm.omi.cloudiator.sword.domain.IpAddress.IpAddressType;
import de.uniulm.omi.cloudiator.sword.domain.IpAddress.IpVersion;
import de.uniulm.omi.cloudiator.sword.domain.IpAddresses;
import java.util.Random;

public class IpGenerator {

  private IpGenerator() {
    throw new AssertionError("Do not instantiate");
  }

  private static final Random random = new Random();

  public static IpAddress generatePublic() {

    while (true) {
      String ip =
          random.nextInt(256) + "." + random.nextInt(256) + "." + random.nextInt(256) + "." + random
              .nextInt(256);

      return IpAddresses.of(ip, IpAddressType.PUBLIC, IpVersion.V4);
    }
  }

  public static IpAddress generatePrivate() {
    String ip =
        192 + "." + 168 + "." + random.nextInt(256) + "." + random
            .nextInt(256);
    return IpAddresses.of(ip, IpAddressType.PRIVATE, IpVersion.V4);
  }

}
