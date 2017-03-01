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

package de.uniulm.omi.cloudiator.sword.domain;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Created by daniel on 19.01.15.
 */
public interface Properties {

    /**
     * Returns the property identified by the key. If the property does not exist
     * null is returned.
     *
     * @param key the identifier
     * @return the value stored at the key or null
     * @throws NullPointerException if key is null.
     */
    @Nullable String getProperty(String key);

    /**
     * Returns the property identified by the key. If the property does not exisit
     * the value given as default value is returned.
     *
     * @param key          the identifier
     * @param defaultValue default value to return
     * @return value stored or defaultValue
     * @throws NullPointerException if key is null.
     */
    @Nullable String getProperty(String key, String defaultValue);

    Map<String, String> getProperties();

}
