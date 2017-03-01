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

import com.google.common.base.MoreObjects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 01.07.16.
 */
public class SecurityGroupRuleImpl implements SecurityGroupRule {

    private static final String PORT_ERROR_MESSAGE =
        "%s needs to be in range of the port range of the protocol, the protocol has the range %s-%s, but the port you supplied is %s";

    private final IpProtocol ipProtocol;
    private final int fromPort;
    private final int toPort;
    private final Cidr cidr;

    public SecurityGroupRuleImpl(IpProtocol ipProtocol, int fromPort, int toPort, Cidr cidr) {
        checkNotNull(ipProtocol, "ipProtocol is null");
        this.ipProtocol = ipProtocol;

        checkArgument(portInRange(ipProtocol, fromPort), String
            .format(PORT_ERROR_MESSAGE, "formPort", ipProtocol.minPort(), ipProtocol.maxPort(),
                fromPort));
        this.fromPort = fromPort;
        checkArgument(portInRange(ipProtocol, fromPort), String
            .format(PORT_ERROR_MESSAGE, "toPort", ipProtocol.minPort(), ipProtocol.maxPort(),
                fromPort));
        checkArgument(!(toPort < fromPort),
            String.format("toPort (%s) must not be smaller than fromPort (%s)", toPort, fromPort));
        this.toPort = toPort;
        checkNotNull(cidr, "cidr is null");
        this.cidr = cidr;
    }

    private static boolean portInRange(IpProtocol ipProtocol, int port) {
        return (port >= ipProtocol.minPort()) && (port <= ipProtocol.maxPort());
    }

    @Override public IpProtocol ipProtocol() {
        return ipProtocol;
    }

    @Override public int fromPort() {
        return fromPort;
    }

    @Override public int toPort() {
        return toPort;
    }

    @Override public Cidr cidr() {
        return cidr;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        SecurityGroupRuleImpl that = (SecurityGroupRuleImpl) o;

        if (fromPort != that.fromPort)
            return false;
        if (toPort != that.toPort)
            return false;
        if (ipProtocol != that.ipProtocol)
            return false;
        return cidr.equals(that.cidr);
    }

    @Override public int hashCode() {
        int result = ipProtocol.hashCode();
        result = 31 * result + fromPort;
        result = 31 * result + toPort;
        result = 31 * result + cidr.hashCode();
        return result;
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this).add("ipProtocol", ipProtocol)
            .add("fromPort", fromPort).add("toPort", toPort).add("cidr", cidr).toString();
    }
}
