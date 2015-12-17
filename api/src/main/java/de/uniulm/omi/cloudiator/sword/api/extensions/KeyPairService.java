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

package de.uniulm.omi.cloudiator.sword.api.extensions;

import de.uniulm.omi.cloudiator.sword.api.domain.KeyPair;
import de.uniulm.omi.cloudiator.sword.api.exceptions.KeyPairException;

import javax.annotation.Nullable;

/**
 * A service for the creation of key pairs at the given
 * cloud provider.
 */
public interface KeyPairService {

    /**
     * Automatically creates a private/public key pair, and
     * registers it with the provider.
     *
     * @param name the name for the keypair (non null)
     * @return the created keypair, will hold private key.
     * @throws KeyPairException         if an error occurs during creation.
     * @throws NullPointerException     if the name is null.
     * @throws IllegalArgumentException if the name is empty.
     */
    KeyPair create(String name);

    /**
     * Registers the public key at the cloud provider.
     *
     * @param name      the name for the key (non null)
     * @param publicKey the public key (non null)
     * @return the registered keypair
     * @throws KeyPairException         if an error occurs during creation.
     * @throws NullPointerException     if any of the arguments is null.
     * @throws IllegalArgumentException if any of the supplied strings are empty.
     */
    KeyPair create(String name, String publicKey);

    /**
     * Deletes the key pair from the cloud provider.
     *
     * @param name the name of the keypair to delete (non null)
     * @return true if it was deleted successful, false of not.
     * @throws KeyPairException         if an error occurs during creation.
     * @throws NullPointerException     if the name is null
     * @throws IllegalArgumentException if the name is empty.
     */
    boolean delete(String name);

    /**
     * Retrieves information about the key pair.
     *
     * @param name the name of the keypair (non null).
     * @return the keypair, or null if not found.
     * @throws KeyPairException         if an error occurs during creation.
     * @throws NullPointerException     if the name is null.
     * @throws IllegalArgumentException if the name is empty.
     */
    @Nullable KeyPair get(String name);


}
