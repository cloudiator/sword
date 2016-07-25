/*
 * Copyright (c) 2014-2015 University of Ulm
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

import com.google.common.collect.Maps;
import de.uniulm.omi.cloudiator.sword.api.domain.TemplateOptions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A builder for {@link TemplateOptions} objects.
 * <p/>
 * Use newBuilder to get a new builder.
 */
public class TemplateOptionsBuilder {

    private String keyPairName;
    private Map<Object, Object> additionalOptions;
    private Set<Integer> inboundPorts;
    private Map<String, String> tags;

    private TemplateOptionsBuilder() {
        additionalOptions = new HashMap<>();
        inboundPorts = new HashSet<>();
        tags = Maps.newHashMap();
    }

    /**
     * Creates a new builder object.
     *
     * @return a new builder object.
     */
    public static TemplateOptionsBuilder newBuilder() {
        return new TemplateOptionsBuilder();
    }

    public static TemplateOptionsBuilder of(TemplateOptions templateOptions) {
        return newBuilder().keyPairName(templateOptions.keyPairName())
            .addOptions(templateOptions.additionalOptions())
            .inboundPorts(templateOptions.inboundPorts()).tags(templateOptions.tags());
    }

    /**
     * Sets the keypair.
     *
     * @param keyPairName the name of the keypair to use.
     * @return fluid interface
     */
    public TemplateOptionsBuilder keyPairName(String keyPairName) {
        this.keyPairName = keyPairName;
        return this;
    }

    /**
     * Adds a generic option to the template
     *
     * @param field key of the option
     * @param value value of the option
     * @return fluid interface
     */
    public TemplateOptionsBuilder addOption(Object field, Object value) {
        additionalOptions.put(field, value);
        return this;
    }

    /**
     * Adds multiple generic options to the template.
     * See {@link #addOption(Object, Object)}.
     *
     * @param options a map of key->value options.
     * @return fluid interface
     */
    public TemplateOptionsBuilder addOptions(Map<Object, Object> options) {
        additionalOptions.putAll(options);
        return this;
    }

    /**
     * Adds multiple inbound ports to the template.
     * <p/>
     * This methods is not idempotent.
     *
     * @param inboundPorts the inbound ports to add.
     * @return fluid interface
     */
    public TemplateOptionsBuilder inboundPorts(Set<Integer> inboundPorts) {
        this.inboundPorts.addAll(inboundPorts);
        return this;
    }

    /**
     * Adds tags to the template. A tag is a combination of key => value.
     * <p/>
     * If you call this method twice, existing tags may be overwritten.
     *
     * @param tags the tags to add.
     * @return fluid interface
     */
    public TemplateOptionsBuilder tags(Map<String, String> tags) {
        this.tags.putAll(tags);
        return this;
    }

    /**
     * Builds the template options object.
     *
     * @return the template options.
     */
    public TemplateOptions build() {
        return new TemplateOptionsImpl(keyPairName, additionalOptions, inboundPorts, tags);
    }


}
