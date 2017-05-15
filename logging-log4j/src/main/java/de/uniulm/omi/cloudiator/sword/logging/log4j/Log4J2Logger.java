/*
 * Copyright (c) 2014-2015 University of Ulm
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.uniulm.omi.cloudiator.sword.logging.log4j;

import de.uniulm.omi.cloudiator.sword.logging.Logger;
import de.uniulm.omi.cloudiator.sword.logging.LoggerFactory;
import org.apache.logging.log4j.LogManager;

/**
 * Created by daniel on 05.03.15.
 */
public class Log4J2Logger implements Logger {

  private final org.apache.logging.log4j.Logger delegate;

  public Log4J2Logger(org.apache.logging.log4j.Logger delegate) {
    this.delegate = delegate;
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
    return delegate.isErrorEnabled();
  }

  @Override
  public boolean isFatalEnabled() {
    return delegate.isFatalEnabled();
  }

  @Override
  public void trace(String message) {
    delegate.trace(message);
  }

  @Override
  public void debug(String message) {
    delegate.debug(message);
  }

  @Override
  public void info(String message) {
    delegate.info(message);
  }

  @Override
  public void warn(String message) {
    delegate.warn(message);
  }

  @Override
  public void warn(String message, Throwable throwable) {
    delegate.warn(message, throwable);
  }

  @Override
  public void error(String message) {
    delegate.error(message);
  }

  @Override
  public void error(String message, Throwable throwable) {
    delegate.error(message, throwable);
  }


  @Override
  public void fatal(String message) {
    delegate.fatal(message);
  }

  @Override
  public void fatal(String message, Throwable throwable) {
    delegate.fatal(message, throwable);
  }

  public static class Log4JLoggingFactory implements LoggerFactory {

    @Override
    public Logger getLogger(String category) {
      return new Log4J2Logger(LogManager.getLogger(category));
    }
  }


}
