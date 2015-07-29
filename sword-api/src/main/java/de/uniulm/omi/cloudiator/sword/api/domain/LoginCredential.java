/*
 * Copyright (c) 2014-2015 University of Ulm
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.uniulm.omi.cloudiator.sword.api.domain;

import com.google.common.base.Optional;

/**
 * Represents a credential for login on a remote
 * machine.
 * <p/>
 * The login credential always has a username, but either
 * a password or a private key.
 */
public interface LoginCredential {

    /**
     * @return the username used for login.
     */
    String username();

    /**
     * @return an {@link Optional} password used for login.
     */
    Optional<String> password();

    /**
     * @return an {@link Optional} private key used for login.
     */
    Optional<String> privateKey();

    /**
     * Check if the password is set.
     *
     * @return returns true if the password is set otherwise false.
     */
    boolean isPasswordCredential();

    /**
     * Check if the private key is set.
     *
     * @return returns true of the private key is set otherwise false.
     */
    boolean isPrivateKeyCredential();
}
