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

package de.uniulm.omi.cloudiator.sword.multicloud.service;

/**
 * Created by daniel on 19.01.17.
 */
public class IdScopedByClouds {

  public static IdScopedByCloud from(String scopedId) {
    return new TildeDelimitedIdScopedByCloudImpl(scopedId);
  }

  public static IdScopedByCloud from(String id, String cloudId) {
    return new TildeDelimitedIdScopedByCloudImpl(id, cloudId);
  }
}
