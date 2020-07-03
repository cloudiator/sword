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

package de.uniulm.omi.cloudiator.sword.remote.internal;

import com.github.rholder.retry.*;
import com.google.common.base.MoreObjects;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.uniulm.omi.cloudiator.domain.RemoteType;
import de.uniulm.omi.cloudiator.sword.annotations.Base;
import de.uniulm.omi.cloudiator.sword.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.properties.Constants;
import de.uniulm.omi.cloudiator.sword.remote.RemoteConnection;
import de.uniulm.omi.cloudiator.sword.remote.RemoteConnectionFactory;
import de.uniulm.omi.cloudiator.sword.remote.RemoteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by daniel on 19.08.15.
 */
class RetryingConnectionFactory implements RemoteConnectionFactory {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(RetryingConnectionFactory.class);

  private final RemoteConnectionFactory remoteConnectionFactory;
  @Inject(optional = true)
  @Named(Constants.SSH_MAX_RETRIES)
  private int connectionRetries = 12;
  @Inject(optional = true)
  @Named(Constants.SSH_FIXED_WAIT_SECONDS)
  private long
          sshFixedWaitSeconds = 120;
  @Inject(optional = true)
  @Named(Constants.SSH_ATTEMPT_TIME)
  private long
          sshAttemptTime = 240;

  @Inject
  RetryingConnectionFactory(@Base RemoteConnectionFactory remoteConnectionFactory) {
    this.remoteConnectionFactory = remoteConnectionFactory;
  }

  @Override
  public RemoteConnection createRemoteConnection(String remoteAddress, RemoteType remoteType,
      LoginCredential loginCredential, int port) {

    LOGGER.info(String.format(
        "%s is opening a remote connection to remoteAddress: %s, remoteType: %s, loginCredential %s and port %s.",
        this, remoteAddress, remoteType, loginCredential, port));

    Callable<RemoteConnection> callable = () -> remoteConnectionFactory
        .createRemoteConnection(remoteAddress, remoteType, loginCredential, port);

    Retryer<RemoteConnection> remoteConnectionRetryer =
        RetryerBuilder.<RemoteConnection>newBuilder().retryIfRuntimeException()
            .withRetryListener(new RetryListener() {
              @Override
              public <V> void onRetry(Attempt<V> attempt) {
                if (attempt.hasException()) {
                  LOGGER.debug(String.format(
                      "Attempt number %s received exception %s. Delay since initial start: %s",
                      attempt.getAttemptNumber(), attempt.getExceptionCause().getMessage(),
                      attempt.getDelaySinceFirstAttempt()), attempt.getExceptionCause());
                }
              }
            })
            .retryIfException(throwable -> throwable instanceof RemoteException)
            .withStopStrategy(StopStrategies.stopAfterAttempt(connectionRetries))
            .withWaitStrategy(WaitStrategies
                .fixedWait(sshFixedWaitSeconds, TimeUnit.SECONDS))
            .withAttemptTimeLimiter(
                AttemptTimeLimiters.fixedTimeLimit(sshAttemptTime, TimeUnit.SECONDS))
            .build();

    try {
      return remoteConnectionRetryer.call(callable);
    } catch (ExecutionException | RetryException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("connectionRetries", connectionRetries)
        .add("fixedWaitTime", sshFixedWaitSeconds).toString();
  }
}
