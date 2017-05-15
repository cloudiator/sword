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

package de.uniulm.omi.cloudiator.sword.logging;

/**
 * Logger interface. Defines methods for logging.
 */
public interface Logger {

  /**
   * @return true if trace logging is enabled, otherwise false.
   */
  boolean isTraceEnabled();

  /**
   * @return true of debug logging is enabled, otherwise false.
   */
  boolean isDebugEnabled();

  /**
   * @return true if info logging is enabled, otherwise false.
   */
  boolean isInfoEnabled();

  /**
   * @return true if warn logging is enabled, otherwise false.
   */
  boolean isWarnEnabled();

  /**
   * @return true if error logging is enabled, otherwise false.
   */
  boolean isErrorEnabled();

  /**
   * @return true if fatal logging is enabled, otherwise false.
   */
  boolean isFatalEnabled();

  /**
   * Logs a message on trace level.
   *
   * @param message the message to log.
   */
  void trace(String message);

  /**
   * Logs a message on debug level.
   *
   * @param message the message to log.
   */
  void debug(String message);

  /**
   * Logs a message on info level.
   *
   * @param message the message to log.
   */
  void info(String message);

  /**
   * Logs a message on warning level.
   *
   * @param message message to log.
   */
  void warn(String message);

  /**
   * Logs a message and a {@link Throwable} on warn level.
   *
   * @param message the message to log.
   * @param throwable the throwable to log.
   */
  void warn(String message, Throwable throwable);

  /**
   * Logs a message on error level.
   *
   * @param message the message to log.
   */
  void error(String message);

  /**
   * Logs a message and a {@link Throwable} on error level.
   *
   * @param message the message to log.
   * @param throwable the throwable to log.
   */
  void error(String message, Throwable throwable);

  /**
   * Logs a message and a {@link Throwable} on fatal level.
   *
   * @param message the message to log.
   */
  void fatal(String message);

  /**
   * Logs a message and a {@link Throwable} on fatal level.
   *
   * @param message the message to log.
   * @param throwable the throwable to log.
   */
  void fatal(String message, Throwable throwable);
}
