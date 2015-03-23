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

import com.google.common.collect.ImmutableMap;
import de.uniulm.omi.cloudiator.sword.api.properties.Properties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by daniel on 19.01.15.
 */
public class PropertiesBuilder {

    private final Map<String, String> serviceProperties;

    private PropertiesBuilder() {
        serviceProperties = new HashMap<>();
    }

    public static PropertiesBuilder create() {
        return new PropertiesBuilder();
    }

    public void setProperty(String key, String value) {
        this.serviceProperties.put(key, value);
    }

    public Properties build() {
        return new PropertiesImpl(ImmutableMap.<String, String>builder().putAll(this.serviceProperties).build());
    }
}
