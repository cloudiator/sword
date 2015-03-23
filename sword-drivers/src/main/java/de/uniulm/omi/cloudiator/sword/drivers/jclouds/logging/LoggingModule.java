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

package de.uniulm.omi.cloudiator.sword.drivers.jclouds.logging;

import de.uniulm.omi.cloudiator.sword.api.logging.LoggerFactory;
import org.jclouds.logging.BaseLogger;
import org.jclouds.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 06.03.15.
 */
public class LoggingModule extends org.jclouds.logging.config.LoggingModule {

    private final LoggerFactory loggerFactory;

    public LoggingModule(LoggerFactory loggerFactory) {
        checkNotNull(loggerFactory);
        this.loggerFactory = loggerFactory;
    }

    @Override public Logger.LoggerFactory createLoggerFactory() {
        return new DelegateLoggerFactory(this.loggerFactory);
    }

    private static class DelegateLoggerFactory implements Logger.LoggerFactory {

        private final de.uniulm.omi.cloudiator.sword.api.logging.LoggerFactory
            delegateLoggerFactory;

        private DelegateLoggerFactory(
            de.uniulm.omi.cloudiator.sword.api.logging.LoggerFactory deleteLoggerFactory) {
            checkNotNull(deleteLoggerFactory);
            this.delegateLoggerFactory = deleteLoggerFactory;
        }

        @Override public Logger getLogger(String category) {
            return new DelegateLogger(this.delegateLoggerFactory.getLogger(category), category);
        }
    }


    private static class DelegateLogger extends BaseLogger {

        private final de.uniulm.omi.cloudiator.sword.api.logging.Logger delegateLogger;
        private final String category;

        private DelegateLogger(de.uniulm.omi.cloudiator.sword.api.logging.Logger delegateLogger,
            String category) {
            checkNotNull(delegateLogger);
            checkNotNull(category);
            this.delegateLogger = delegateLogger;
            this.category = category;
        }

        @Override protected void logError(String message, Throwable e) {
            delegateLogger.error(message, e);
        }

        @Override protected void logError(String message) {
            delegateLogger.error(message);
        }

        @Override protected void logWarn(String message, Throwable e) {
            delegateLogger.warn(message, e);
        }

        @Override protected void logWarn(String message) {
            delegateLogger.warn(message);
        }

        @Override protected void logInfo(String message) {
            delegateLogger.info(message);
        }

        @Override protected void logDebug(String message) {
            delegateLogger.debug(message);
        }

        @Override protected void logTrace(String message) {
            delegateLogger.trace(message);
        }

        @Override public String getCategory() {
            return category;
        }

        @Override public boolean isTraceEnabled() {
            return delegateLogger.isTraceEnabled();
        }

        @Override public boolean isDebugEnabled() {
            return delegateLogger.isDebugEnabled();
        }

        @Override public boolean isInfoEnabled() {
            return delegateLogger.isInfoEnabled();
        }

        @Override public boolean isWarnEnabled() {
            return delegateLogger.isWarnEnabled();
        }

        @Override public boolean isErrorEnabled() {
            return delegateLogger.isErrorEnabled() || delegateLogger.isFatalEnabled();
        }
    }

}
