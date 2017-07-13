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

import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

/**
 * Options for a {@link VirtualMachineTemplate}.
 */
public interface TemplateOptions {

  /**
   * The keypair to use for the virtual machine.
   *
   * @return the name of the keypair or null.
   */
  @Deprecated
  @Nullable
  String keyPairName();

  /**
   * The ports to open for the virtual machine.
   * The set is immutable {@link com.google.common.collect.ImmutableSet}
   *
   * @return a set of integers depicting the ports.
   */
  Set<Integer> inboundPorts();

  /**
   * Tags to assign to the virtual machine.
   * The map is immutable {@link com.google.common.collect.ImmutableMap}
   *
   * @return a map of key=>value tags.
   */
  Map<String, String> tags();

  /**
   * UserData to pass to the virtual machine.
   *
   * @return a string describing user data.
   */
  String userData();

  /**
   * A map of additional generic options in
   * key->value form.
   * <p/>
   * The map is immutable ({@link com.google.common.collect.ImmutableMap})
   *
   * @return an immutable map
   */
  Map<Object, Object> additionalOptions();

}
