/*
 * Copyright (c) 2014-2018 University of Ulm
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.fail;

import java.util.HashMap;
import org.junit.Test;

/**
 * Created by daniel on 13.01.17.
 */
public class PropertiesImplTest {

  @Test
  public void getPropertyKeyTest() {
    Properties properties =
        PropertiesBuilder.newBuilder().putProperty("a", "b").putProperty("c", "a")
            .putProperty("c", "d").build();
    assertThat(properties.getProperty("a"), is(equalTo("b")));
    assertThat(properties.getProperty("c"), is(equalTo("d")));
    assertThat(properties.getProperty("f", "g"), is(equalTo("g")));
    assertThat(properties.getProperty("a", "f"), equalTo("b"));
  }

  @Test
  public void getPropertiesTest() {
    Properties properties =
        PropertiesBuilder.newBuilder().putProperties(new HashMap<String, String>() {{
          put("a", "b");
          put("c", "a");

        }}).putProperties(new HashMap<String, String>() {{
          put("c", "d");
        }}).build();

    assertThat(properties.getProperties().get("a"), is(equalTo("b")));
    assertThat(properties.getProperties().get("c"), is(equalTo("d")));
  }

  @Test
  public void rejectsNullKey() {
    Properties properties = PropertiesBuilder.newBuilder().build();
    try {
      properties.getProperty(null);
      fail("Expected properties.getProperty(null) to throw NullPointerException.");
    } catch (NullPointerException ignored) {
    }

    try {
      properties.getProperty(null, "Hello, World!");
      fail("Expected properties.getProperty(null,String) to throw NullPointerException");
    } catch (NullPointerException ignored) {
    }

  }

}
