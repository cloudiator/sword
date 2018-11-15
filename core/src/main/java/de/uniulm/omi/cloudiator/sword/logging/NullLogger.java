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

package de.uniulm.omi.cloudiator.sword.logging;

/**
 * Created by daniel on 06.03.15.
 */
public class NullLogger implements Logger {

  @Override
  public boolean isTraceEnabled() {
    return false;
  }

  @Override
  public boolean isDebugEnabled() {
    return false;
  }

  @Override
  public boolean isInfoEnabled() {
    return false;
  }

  @Override
  public boolean isWarnEnabled() {
    return false;
  }

  @Override
  public boolean isErrorEnabled() {
    return false;
  }

  @Override
  public boolean isFatalEnabled() {
    return false;
  }

  @Override
  public void trace(String message) {
    // do nothing
  }

  @Override
  public void debug(String message) {
    // do nothing
  }

  @Override
  public void info(String message) {
    // do nothing
  }

  @Override
  public void warn(String message) {
    // do nothing
  }

  @Override
  public void warn(String message, Throwable throwable) {
    // do nothing
  }

  @Override
  public void error(String message) {
    // do nothing
  }

  @Override
  public void error(String message, Throwable throwable) {
    // do nothing
  }

  @Override
  public void fatal(String message) {
    // do nothing
  }

  @Override
  public void fatal(String message, Throwable throwable) {
    // do nothing
  }

  public static class NullLoggerFactory implements LoggerFactory {

    @Override
    public Logger getLogger(String category) {
      return new NullLogger();
    }
  }
}
