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

package org.cloudiator.meta.cloudharmony;

import java.util.function.Function;

/**
 * Created by daniel on 09.03.17.
 */
public class ProviderToCloudHarmony implements Function<String, String> {

  @Override
  public String apply(String swordProviderName) {

    switch (swordProviderName) {
      case "aws-ec2":
        return "aws:ec2";
      case "google-compute-engine":
        return "google:compute";
      default:
        throw new UnsupportedOperationException(
            String.format("%s is not supported by CloudHarmony", swordProviderName));

    }
  }
}
