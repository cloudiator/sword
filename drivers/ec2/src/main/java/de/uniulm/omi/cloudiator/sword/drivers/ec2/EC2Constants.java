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

package de.uniulm.omi.cloudiator.sword.drivers.ec2;

/**
 * Created by daniel on 08.09.15.
 */
public class EC2Constants {

  public final static String PROPERTY_EC2_AMI_QUERY = "sword.ec2.ami.query";
  public final static String PROPERTY_EC2_CC_AMI_QUERY = "sword.ec2.ami.cc.query";
  public final static String PROPERTY_EC2_DEFAULT_VPC = "sword.ec2.default.vpc";
  public final static String PROPERTY_EC2_AMI_OWNERS = "sword.ec2.ami.owners";
  //overrides quotas in the format sword.ec2.quota.location.hardware
  //e.g. sword.ec2.quota.eu-west-1.m5.2xlarge
  public final static String PROPERTY_EC2_QUOTA_PREFIX = "sword.ec2.quota.";

  private EC2Constants() {
    throw new AssertionError("intentionally left empty");
  }

}
