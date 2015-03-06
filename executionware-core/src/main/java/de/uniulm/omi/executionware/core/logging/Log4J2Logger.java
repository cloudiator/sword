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

package de.uniulm.omi.executionware.core.logging;

import de.uniulm.omi.executionware.api.logging.Logger;
import de.uniulm.omi.executionware.api.logging.LoggerFactory;
import org.apache.logging.log4j.LogManager;

/**
 * Created by daniel on 05.03.15.
 */
public class Log4J2Logger implements Logger {

    private final org.apache.logging.log4j.Logger log4J2Logger;

    public Log4J2Logger(org.apache.logging.log4j.Logger log4J2Logger) {
        this.log4J2Logger = log4J2Logger;
    }

    @Override
    public boolean isTraceEnabled() {
        return log4J2Logger.isTraceEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return log4J2Logger.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return log4J2Logger.isInfoEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return log4J2Logger.isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return log4J2Logger.isErrorEnabled();
    }

    @Override
    public boolean isFatalEnabled() {
        return log4J2Logger.isFatalEnabled();
    }

    @Override
    public void trace(String message) {
        log4J2Logger.trace(message);
    }

    @Override
    public void debug(String message) {
        log4J2Logger.debug(message);
    }

    @Override
    public void info(String message) {
        log4J2Logger.info(message);
    }

    @Override
    public void warn(String message) {
        log4J2Logger.warn(message);
    }

    @Override
    public void warn(String message, Throwable throwable) {
        log4J2Logger.warn(message, throwable);
    }

    @Override
    public void error(String message) {
        log4J2Logger.error(message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        log4J2Logger.error(message, throwable);
    }


    @Override
    public void fatal(String message) {
        log4J2Logger.fatal(message);
    }

    @Override
    public void fatal(String message, Throwable throwable) {
        log4J2Logger.fatal(message, throwable);
    }

    public static class Log4JLoggingFactory implements LoggerFactory {
        @Override
        public Logger getLogger(String category) {
            return new Log4J2Logger(LogManager.getLogger(category));
        }
    }


}
