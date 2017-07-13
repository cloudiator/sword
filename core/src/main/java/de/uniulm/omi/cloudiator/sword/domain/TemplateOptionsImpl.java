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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

/**
 * Basic implementation of the {@link TemplateOptions} interface.
 */
public class TemplateOptionsImpl implements TemplateOptions {

  @Nullable
  private final String keyPairName;
  private final Set<Integer> inboundPorts;
  private final Map<Object, Object> additionalOptions;
  private final Map<String, String> tags;
  @Nullable
  private final String userData;


  TemplateOptionsImpl(@Nullable String keyPairName, Map<Object, Object> additionalOptions,
      Set<Integer> inboundPorts, Map<String, String> tags, @Nullable String userData) {

    if (keyPairName != null) {
      checkArgument(!keyPairName.isEmpty());
    }
    checkNotNull(additionalOptions);
    checkNotNull(inboundPorts);
    checkNotNull(tags);

    this.keyPairName = keyPairName;
    this.inboundPorts = ImmutableSet.copyOf(inboundPorts);
    this.additionalOptions = ImmutableMap.copyOf(additionalOptions);
    this.tags = ImmutableMap.copyOf(tags);
    this.userData = userData;
  }

  @Nullable
  @Override
  @Deprecated
  public String keyPairName() {
    return keyPairName;
  }

  @Override
  public Set<Integer> inboundPorts() {
    return inboundPorts;
  }

  @Override
  public Map<String, String> tags() {
    return tags;
  }

  @Nullable
  @Override
  public String userData() {
    return userData;
  }

  @Override
  public Map<Object, Object> additionalOptions() {
    return additionalOptions;
  }
}
