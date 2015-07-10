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

import de.uniulm.omi.cloudiator.sword.api.domain.KeyPair;
import de.uniulm.omi.cloudiator.sword.core.domain.builders.KeyPairBuilder;
import org.jclouds.ssh.SshKeys;

import java.util.Map;

/**
 * Created by daniel on 18.05.15.
 */
public class KeyPairs {

    public static KeyPair generate(String name) {
        final Map.Entry<String, String> sshKeyPair =
            SshKeys.generate().entrySet().iterator().next();
        return KeyPairBuilder.create().name(name).publicKey(sshKeyPair.getKey())
            .privateKey(sshKeyPair.getValue()).build();
    }
}
