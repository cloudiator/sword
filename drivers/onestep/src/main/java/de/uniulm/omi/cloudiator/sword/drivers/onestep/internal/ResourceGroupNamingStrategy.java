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

package de.uniulm.omi.cloudiator.sword.drivers.onestep.internal;

import de.uniulm.omi.cloudiator.sword.domain.Cloud;

import javax.inject.Inject;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by pszkup on 08.05.2019
 */
public class ResourceGroupNamingStrategy implements Function<String, String> {

  private final String nodeGroup;

  @Inject
  public ResourceGroupNamingStrategy(Cloud cloud) {
    checkNotNull(cloud, "cloud is null");
    nodeGroup = cloud.configuration().nodeGroup();
  }

  @Override
  public String apply(String region) {
    return region + nodeGroup;
  }
}
