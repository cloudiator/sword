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
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by daniel on 30.07.15.
 */
public class TemplateOptionsImplTest {

    @Test(expected = NullPointerException.class)
    public void testConstructorDisallowsNullAdditionalOptions() {
        new TemplateOptionsImpl("test", null, Collections.singleton(1),
            Collections.singletonMap("test", "test"));
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorDisallowsNullInboundPorts() {
        new TemplateOptionsImpl("test", new HashMap<>(), null,
            Collections.singletonMap("test", "test"));
    }

    @Test(expected = NullPointerException.class) public void testConstructorDisallowsNullTags() {
        new TemplateOptionsImpl("test", new HashMap<>(), Collections.singleton(1), null);
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
        Map<Object, Object> map = Maps.newHashMapWithExpectedSize(1);
        map.put("key2", "value2");
        TemplateOptions templateOptions =
            TemplateOptionsBuilder.newBuilder().addOption("key", "value").addOptions(map).build();
        assertThat(templateOptions.additionalOptions().get("key"), equalTo("value"));
        assertThat(templateOptions.additionalOptions().get("key2"), equalTo("value2"));
    }

    @Test public void testInboundPorts() throws Exception {
        Set<Integer> inboundPorts = Collections.singleton(1);
        TemplateOptions templateOptions =
            TemplateOptionsBuilder.newBuilder().inboundPorts(inboundPorts).build();
        assertThat(templateOptions.inboundPorts().contains(1), equalTo(true));
    }

    @Test public void testTags() throws Exception {
        Map<String, String> tags = Collections.singletonMap("key", "value");
        TemplateOptions templateOptions = TemplateOptionsBuilder.newBuilder().tags(tags).build();
        assertThat(templateOptions.tags().containsKey("key"), equalTo(true));
        assertThat(templateOptions.tags().containsValue("value"), equalTo(true));
    }
}
