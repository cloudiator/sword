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

package de.uniulm.omi.cloudiator.sword.core.domain;

import de.uniulm.omi.cloudiator.sword.api.domain.Configuration;
import de.uniulm.omi.cloudiator.sword.api.properties.Properties;
import de.uniulm.omi.cloudiator.sword.core.properties.PropertiesImpl;

/**
 * Created by daniel on 18.01.17.
 */
public class ConfigurationBuilder {

    private Properties properties = PropertiesImpl.EMPTY;
    private String nodeGroup;

    private ConfigurationBuilder() {
    }

    public static ConfigurationBuilder newBuilder() {
        return new ConfigurationBuilder();
    }

    public ConfigurationBuilder properties(Properties properties) {
        this.properties = properties;
        return this;
    }

    public ConfigurationBuilder nodeGroup(String nodeGroup) {
        this.nodeGroup = nodeGroup;
        return this;
    }

    public Configuration build() {
        return new ConfigurationImpl(nodeGroup, properties);
    }

}
