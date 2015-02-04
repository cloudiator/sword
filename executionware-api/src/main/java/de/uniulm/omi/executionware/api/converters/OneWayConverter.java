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

package de.uniulm.omi.executionware.api.converters;

import com.google.common.base.Function;

/**
 * A one-way converter for converting one type to another type.
 *
 * Extends {@link com.google.common.base.Function} so it can be used
 * in their functional programming methods.
 *
 * @param <T> the type to be converted.
 * @param <S> the resulting type.
 */
public interface OneWayConverter<T,S> extends Function<T,S> {
}
