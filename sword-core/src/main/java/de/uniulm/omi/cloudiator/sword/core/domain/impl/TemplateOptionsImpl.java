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

package de.uniulm.omi.cloudiator.sword.core.domain.impl;

import com.google.common.collect.ImmutableMap;
import de.uniulm.omi.cloudiator.sword.api.domain.TemplateOptions;

import javax.annotation.Nullable;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Basic implementation of the {@link TemplateOptions} interface.
 */
public class TemplateOptionsImpl implements TemplateOptions {

    @Nullable private final String keyPairName;
    private final Map<Object, Object> additionalOptions;

    /**
     * Constructor.
     * <p/>
     * Use {@link de.uniulm.omi.cloudiator.sword.core.domain.builders.TemplateOptionsBuilder}
     * to create a new instance.
     *
     * @param keyPairName       the name of the keypair to use (optional).
     * @param additionalOptions a key->value map for addition options (mandatory).
     * @throws NullPointerException     if a mandatory argument is null.
     * @throws IllegalArgumentException if the keyPairName is an empty string.
     */
    public TemplateOptionsImpl(@Nullable String keyPairName,
        Map<Object, Object> additionalOptions) {

        if (keyPairName != null) {
            checkArgument(!keyPairName.isEmpty());
        }
        checkNotNull(additionalOptions);

        this.keyPairName = keyPairName;
        this.additionalOptions = ImmutableMap.copyOf(additionalOptions);
    }

    @Nullable @Override public String keyPairName() {
        return keyPairName;
    }

    @Override public Map<Object, Object> additionalOptions() {
        return additionalOptions;
    }
}
