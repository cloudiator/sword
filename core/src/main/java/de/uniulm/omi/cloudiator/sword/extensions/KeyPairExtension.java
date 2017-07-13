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

package de.uniulm.omi.cloudiator.sword.extensions;


import de.uniulm.omi.cloudiator.sword.domain.KeyPair;
import javax.annotation.Nullable;

/**
 * A service for the creation of key pairs at the given
 * cloud provider.
 */
@Deprecated public interface KeyPairExtension {

  /**
   * Automatically creates a private/public key pair, and
   * registers it with the provider.
   *
   * @param name the name for the keypair (nullable)
   * @param locationId the locationId to create the keyPair in
   * @return the created keypair, will hold private key.
   * @throws IllegalArgumentException if the name is empty.
   */
  KeyPair create(@Nullable String name, String locationId);

  /**
   * Registers the public key at the cloud provider.
   *
   * @param name the name for the key (nullable)
   * @param publicKey the public key (non null)
   * @param locationId the locationId to create the keyPair in
   * @return the registered keypair
   * @throws NullPointerException if the public key is null.
   * @throws IllegalArgumentException if any of the supplied strings are empty.
   */
  KeyPair create(@Nullable String name, String publicKey, String locationId);

  /**
   * Deletes the key pair from the cloud provider.
   *
   * @param id id of the keypair
   * @return true if it was deleted successful, false of not.
   * @throws NullPointerException if the id is null.
   * @throws IllegalArgumentException if the id is empty.
   */
  boolean delete(String id);

  /**
   * Retrieves information about the key pair.
   *
   * @param id of the keypair
   * @return the keypair, or null if not found.
   * @throws NullPointerException if the id is null.
   * @throws IllegalArgumentException if the id is empty.
   */
  @Nullable
  KeyPair get(String id);


}
