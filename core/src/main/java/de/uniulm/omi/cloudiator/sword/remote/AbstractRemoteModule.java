/*
 * Copyright (c) 2014-2018 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.remote;

import com.google.inject.AbstractModule;

/**
 * An abstract module for the definition of an own remote connection module.
 */
public abstract class AbstractRemoteModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(RemoteConnectionFactory.class).to(getRemoteConnectionFactory());
  }

  /**
   * Used for defining the class that will be used for creating remote connections ({@link
   * RemoteConnectionFactory}).
   *
   * @return a class implementing a RemoteConnectionFactory.
   */
  protected abstract Class<? extends RemoteConnectionFactory> getRemoteConnectionFactory();
}
