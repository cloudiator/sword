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

package de.uniulm.omi.cloudiator.sword.drivers.jclouds.logging;

import static com.google.common.base.Preconditions.checkNotNull;

import de.uniulm.omi.cloudiator.sword.logging.LoggerFactory;
import org.jclouds.logging.BaseLogger;
import org.jclouds.logging.Logger;

/**
 * Created by daniel on 06.03.15.
 */
public class JCloudsLoggingModule extends org.jclouds.logging.config.LoggingModule {

  private final LoggerFactory loggerFactory;

  public JCloudsLoggingModule(LoggerFactory loggerFactory) {
    checkNotNull(loggerFactory);
    this.loggerFactory = loggerFactory;
  }

  @Override
  public Logger.LoggerFactory createLoggerFactory() {
    return new DelegateLoggerFactory(this.loggerFactory);
  }

  private static class DelegateLoggerFactory implements Logger.LoggerFactory {

    private final de.uniulm.omi.cloudiator.sword.logging.LoggerFactory delegate;

    private DelegateLoggerFactory(
        de.uniulm.omi.cloudiator.sword.logging.LoggerFactory deleteLoggerFactory) {
      checkNotNull(deleteLoggerFactory);
      this.delegate = deleteLoggerFactory;
    }

    @Override
    public Logger getLogger(String category) {
      return new DelegateLogger(this.delegate.getLogger(category), category);
    }
  }


  private static class DelegateLogger extends BaseLogger {

    private final de.uniulm.omi.cloudiator.sword.logging.Logger delegate;
    private final String category;

    private DelegateLogger(de.uniulm.omi.cloudiator.sword.logging.Logger delegate,
        String category) {
      checkNotNull(delegate);
      checkNotNull(category);
      this.delegate = delegate;
      this.category = category;
    }

    @Override
    protected void logError(String message, Throwable e) {
      delegate.error(message, e);
    }

    @Override
    protected void logError(String message) {
      delegate.error(message);
    }

    @Override
    protected void logWarn(String message, Throwable e) {
      delegate.warn(message, e);
    }

    @Override
    protected void logWarn(String message) {
      delegate.warn(message);
    }

    @Override
    protected void logInfo(String message) {
      delegate.info(message);
    }

    @Override
    protected void logDebug(String message) {
      delegate.debug(message);
    }

    @Override
    protected void logTrace(String message) {
      delegate.trace(message);
    }

    @Override
    public String getCategory() {
      return category;
    }

    @Override
    public boolean isTraceEnabled() {
      return delegate.isTraceEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
      return delegate.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
      return delegate.isInfoEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
      return delegate.isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
      return delegate.isErrorEnabled() || delegate.isFatalEnabled();
    }
  }

}
