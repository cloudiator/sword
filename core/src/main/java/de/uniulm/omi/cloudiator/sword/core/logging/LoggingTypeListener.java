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

package de.uniulm.omi.cloudiator.sword.core.logging;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import de.uniulm.omi.cloudiator.sword.api.logging.InjectLogger;
import de.uniulm.omi.cloudiator.sword.api.logging.Logger;
import de.uniulm.omi.cloudiator.sword.api.logging.LoggerFactory;

import java.lang.reflect.Field;

/**
 * Created by daniel on 06.03.15.
 */
public class LoggingTypeListener implements TypeListener {

    private final LoggerFactory loggerFactory;

    public LoggingTypeListener(LoggerFactory loggerFactory) {
        this.loggerFactory = loggerFactory;
    }


    @Override
    public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
        Class<?> clazz = typeLiteral.getRawType();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getType() == Logger.class &&
                        field.isAnnotationPresent(InjectLogger.class)) {
                    typeEncounter.register(new LoggingMembersInjector<>(field, loggerFactory));
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

}
