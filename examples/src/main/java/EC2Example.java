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

import de.uniulm.omi.cloudiator.sword.domain.ApiBuilder;
import de.uniulm.omi.cloudiator.sword.domain.CloudBuilder;
import de.uniulm.omi.cloudiator.sword.domain.ConfigurationBuilder;
import de.uniulm.omi.cloudiator.sword.domain.CredentialsBuilder;
import de.uniulm.omi.cloudiator.sword.service.ComputeService;
import de.uniulm.omi.cloudiator.sword.service.ServiceBuilder;

/**
 * Example depicting the information needed to build an Amazon Web Services - EC2 compute service.
 */
public class EC2Example {

  /**
   * The access key of your AWS user.
   */
  final String accessKeyId = "AMAZONACCESSKEYID";
  /**
   * The secret of your AWS user.
   */
  final String secretAccessKey = "SecretAccessKey";
  /**
   * A string depicting your node group. Used to identify the machines management by sword.
   */
  final String nodeGroup = "nodeGroup";

  /**
   * Builds the compute service.
   */
  ComputeService ec2 = ServiceBuilder.newServiceBuilder().cloud(
      CloudBuilder.newBuilder().endpoint(null)
          .configuration(ConfigurationBuilder.newBuilder().nodeGroup(nodeGroup).build())
          .credentials(
              CredentialsBuilder.newBuilder().user(accessKeyId).password(secretAccessKey).build())
          .api(ApiBuilder.newBuilder().providerName("aws-ec2").build()).build()).build();

}
