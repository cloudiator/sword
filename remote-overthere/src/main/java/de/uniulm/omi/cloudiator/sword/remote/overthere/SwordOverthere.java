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

package de.uniulm.omi.cloudiator.sword.remote.overthere;

import com.xebialabs.overthere.ConnectionOptions;
import com.xebialabs.overthere.Overthere;
import com.xebialabs.overthere.RuntimeIOException;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteException;

/**
 * Created by daniel on 23.09.15.
 */
public class SwordOverthere {

    private SwordOverthere() {
    }

    public static SwordOverthereConnection getConnection(String protocol,
        final ConnectionOptions options) throws RemoteException {
        try {
            return new ThrowingSwordOverthereConnection(Overthere.getConnection(protocol, options));
        } catch (RuntimeIOException e) {
            throw new RemoteException(e);
        }
    }
}