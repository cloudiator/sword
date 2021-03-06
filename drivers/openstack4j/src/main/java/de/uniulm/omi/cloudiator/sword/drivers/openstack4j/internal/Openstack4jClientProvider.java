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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.reflect.Reflection;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import org.openstack4j.api.OSClient;

/**
 * Created by daniel on 14.11.16.
 */
@Deprecated
public class Openstack4jClientProvider implements Provider<OSClient> {

  private final Injector injector;
  private final KeyStoneVersion keyStoneVersion;

  @Inject
  public Openstack4jClientProvider(Injector injector, KeyStoneVersion keyStoneVersion) {
    checkNotNull(keyStoneVersion, "keyStoneVersion is null");
    this.keyStoneVersion = keyStoneVersion;
    checkNotNull(injector, "injector is null");
    this.injector = injector;
  }

  @Override
  public OSClient get() {
    return Reflection.newProxy(keyStoneVersion.osClientClass(),
        injector.getInstance(LazyAuthenticationOSClient.class));
  }
}
