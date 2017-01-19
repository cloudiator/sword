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

package de.uniulm.omi.cloudiator.sword.core.properties;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import de.uniulm.omi.cloudiator.sword.api.properties.Properties;

import java.util.Collections;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 19.01.15.
 */
public class PropertiesImpl implements Properties {

    public static final Properties EMPTY = new PropertiesImpl(Collections.emptyMap());

    private final Map<String, String> propertiesHolder;

    public PropertiesImpl(Map<String, String> propertiesHolder) {
        checkNotNull(propertiesHolder);
        this.propertiesHolder = propertiesHolder;
    }

    @Override public String getProperty(String key) {
        checkNotNull(key, "key is null");
        return this.propertiesHolder.get(key);
    }

    @Override public String getProperty(String key, String defaultValue) {
        String value = this.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    @Override public Map<String, String> getProperties() {
        return ImmutableMap.copyOf(propertiesHolder);
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        PropertiesImpl that = (PropertiesImpl) o;

        return propertiesHolder.equals(that.propertiesHolder);
    }

    @Override public int hashCode() {
        return propertiesHolder.hashCode();
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this).add("properties", propertiesHolder).toString();
    }
}
