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

package de.uniulm.omi.cloudiator.sword.util;

import java.util.function.Predicate;
import javax.annotation.Nullable;

/**
 * Created by daniel on 12.08.16.
 */
public interface NamingStrategy {

  /**
   * Generates a unique name based on the name input.
   *
   * @param name the name to use within the unique name.
   * @return a unique named.
   */
  String generateUniqueNameBasedOnName(@Nullable String name);

  /**
   * Generates a name based on the given name.
   *
   * @param name the name
   * @return a name based on the given string.
   */
  String generateNameBasedOnName(String name);

  /**
   * Returns a {@link Predicate} to check if the name belongs to
   * the naming group.
   *
   * @return the predicate
   */
  Predicate<String> belongsToNamingGroup();

}
