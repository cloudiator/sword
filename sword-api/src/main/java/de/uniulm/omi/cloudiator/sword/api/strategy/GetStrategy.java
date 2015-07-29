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

package de.uniulm.omi.cloudiator.sword.api.strategy;


import javax.annotation.Nullable;

/**
 * A generic strategy for retrieving things.
 *
 * @param <S> used for searching.
 * @param <T> gets retrieved
 */
public interface GetStrategy<S, T> {

    /**
     * Searches for a T using the provided argument s.
     *
     * @param s the id.
     * @return T if found, otherwise null.
     * @throws NullPointerException     if the given s is null.
     * @throws IllegalArgumentException if the given s is invalid.
     */
    @Nullable T get(S s);
}
