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

import com.google.common.base.MoreObjects;
import de.uniulm.omi.cloudiator.sword.api.domain.Configuration;
import de.uniulm.omi.cloudiator.sword.api.properties.Properties;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 18.01.17.
 */
public class ConfigurationImpl implements Configuration {

    private final String nodeGroup;
    private final Properties properties;

    public ConfigurationImpl(String nodeGroup, Properties properties) {
        checkNotNull(nodeGroup, "nodeGroup is null");
        checkNotNull(properties, "properties is null");
        this.nodeGroup = nodeGroup;
        this.properties = properties;
    }

    @Override public String nodeGroup() {
        return nodeGroup;
    }

    @Override public Properties properties() {
        return properties;
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this).add("nodeGroup", nodeGroup)
            .add("properties", properties).toString();
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ConfigurationImpl that = (ConfigurationImpl) o;

        if (!nodeGroup.equals(that.nodeGroup))
            return false;
        return properties.equals(that.properties);
    }

    @Override public int hashCode() {
        int result = nodeGroup.hashCode();
        result = 31 * result + properties.hashCode();
        return result;
    }
}
