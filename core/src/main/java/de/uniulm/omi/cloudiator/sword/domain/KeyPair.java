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


import java.util.Optional;

/**
 * Represents a public/private keypair for accessing
 * servers with SSH.
 * <p/>
 * The private key is optional, and is normally only available
 * if the keypair was just created.
 */
public interface KeyPair extends Resource {

  /**
   * The public key of the keypair.
   *
   * @return the public key.
   */
  String publicKey();

  /**
   * The private key. Is only present if the keypair was just created.
   *
   * @return an {@link Optional} private key.
   */
  Optional<String> privateKey();

}
