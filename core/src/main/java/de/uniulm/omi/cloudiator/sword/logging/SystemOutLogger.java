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
 * A logger implementation using System.out and System.err for logging information.
 */
public class SystemOutLogger implements Logger {

  @Override
  public boolean isTraceEnabled() {
    return true;
  }

  @Override
  public boolean isDebugEnabled() {
    return true;
  }

  @Override
  public boolean isInfoEnabled() {
    return true;
  }

  @Override
  public boolean isWarnEnabled() {
    return true;
  }

  @Override
  public boolean isErrorEnabled() {
    return true;
  }

  @Override
  public boolean isFatalEnabled() {
    return true;
  }

  @Override
  public void trace(String message) {
    System.out.println(message);
  }

  @Override
  public void debug(String message) {
    System.out.println(message);
  }

  @Override
  public void info(String message) {
    System.out.println(message);
  }

  @Override
  public void warn(String message) {
    System.out.println(message);
  }

  @Override
  public void warn(String message, Throwable throwable) {
    System.out.println(message);
    throwable.printStackTrace();
  }

  @Override
  public void error(String message) {
    System.err.println(message);
  }

  @Override
  public void error(String message, Throwable throwable) {
    System.err.println(message);
    throwable.printStackTrace();
  }

  @Override
  public void fatal(String message) {
    System.err.println(message);
  }

  @Override
  public void fatal(String message, Throwable throwable) {
    System.err.println(message);
    throwable.printStackTrace();
  }
}
