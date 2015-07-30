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
import de.uniulm.omi.cloudiator.sword.core.domain.builders.TemplateOptionsBuilder;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * Created by daniel on 30.07.15.
 */
public class TemplateOptionsImplTest {

    @Test(expected = NullPointerException.class)
    public void testConstructorDisallowsNullAdditionalOptions() {
        new TemplateOptionsImpl("test", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorDisallowsEmptyKeyPairName() {
        TemplateOptionsBuilder.newBuilder().keyPairName("").build();
    }

    @Test public void testKeyPairName() throws Exception {
        TemplateOptions templateOptions =
            TemplateOptionsBuilder.newBuilder().keyPairName("keyPairName").build();
        assertThat(templateOptions.keyPairName(), equalTo("keyPairName"));
    }

    @Test public void testAdditionalOptions() throws Exception {
        TemplateOptions templateOptions =
            TemplateOptionsBuilder.newBuilder().addOption("key", "value").build();
        assertThat(templateOptions.additionalOptions().get("key"), equalTo("value"));
    }

    @Test public void testAdditionalOptionsIsImmutable() throws Exception {
        TemplateOptions templateOptions = TemplateOptionsBuilder.newBuilder().build();
        assertThat(templateOptions.additionalOptions(), instanceOf(ImmutableMap.class));
    }
}
