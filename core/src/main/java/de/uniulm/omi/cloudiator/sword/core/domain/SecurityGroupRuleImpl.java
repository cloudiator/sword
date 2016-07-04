/*
 * Copyright (c) 2014-2016 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.core.domain;

import com.google.common.base.MoreObjects;
import de.uniulm.omi.cloudiator.sword.api.domain.Cidr;
import de.uniulm.omi.cloudiator.sword.api.domain.IpProtocol;
import de.uniulm.omi.cloudiator.sword.api.domain.SecurityGroupRule;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 01.07.16.
 */
public class SecurityGroupRuleImpl implements SecurityGroupRule {

    private static final String portRangeErrorMessage =
        String.format("port needs to be in range %s to %s.", MIN_PORT, MAX_PORT);

    private final IpProtocol ipProtocol;
    private final int fromPort;
    private final int toPort;
    private final Cidr cidr;

    public SecurityGroupRuleImpl(IpProtocol ipProtocol, int fromPort, int toPort, Cidr cidr) {
        checkNotNull(ipProtocol);
        this.ipProtocol = ipProtocol;

        checkArgument(portInRange(fromPort), portRangeErrorMessage);
        this.fromPort = fromPort;
        checkArgument(portInRange(toPort), portRangeErrorMessage);
        checkArgument(!(toPort < fromPort), "toPort must not be smaller than fromPort");
        this.toPort = toPort;
        checkNotNull(cidr);
        this.cidr = cidr;
    }

    private static boolean portInRange(int port) {
        return (port >= MIN_PORT) && (port <= MAX_PORT);
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
        //todo override equals
        return super.equals(o);
    }

    @Override public int hashCode() {
        //todo override equals
        return super.hashCode();
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this).add("ipProtocol", ipProtocol)
            .add("fromPort", fromPort).add("toPort", toPort).add("cidr", cidr).toString();
    }
}
