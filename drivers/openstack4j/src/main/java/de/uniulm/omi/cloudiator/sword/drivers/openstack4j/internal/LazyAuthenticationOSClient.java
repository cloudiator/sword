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

import com.google.common.reflect.AbstractInvocationHandler;
import com.google.inject.Inject;
import java.lang.reflect.Method;
import org.openstack4j.api.OSClient;
import org.openstack4j.openstack.internal.OSClientSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by daniel on 02.12.16.
 */
@Deprecated
public class LazyAuthenticationOSClient extends AbstractInvocationHandler {

  private static ThreadLocal<OSClient> delegate = new ThreadLocal<>();
  private final OsClientFactory osClientFactory;
  private static final Logger LOGGER = LoggerFactory.getLogger(LazyAuthenticationOSClient.class);

  @Inject
  public LazyAuthenticationOSClient(OsClientFactory osClientFactory) {
    this.osClientFactory = osClientFactory;
  }

  @Override
  protected synchronized Object handleInvocation(Object proxy, Method method, Object[] args)
      throws Throwable {

    if (delegate.get() == null) {
      delegate.set(osClientFactory.create());
    }
    if (OSClientSession.getCurrent() == null) {
      delegate.set(osClientFactory.create());
    }
    return method.invoke(delegate.get(), args);
  }

  @Override
  public String toString() {
    return "LazyAuthenticationOSClient{}";
  }
}
