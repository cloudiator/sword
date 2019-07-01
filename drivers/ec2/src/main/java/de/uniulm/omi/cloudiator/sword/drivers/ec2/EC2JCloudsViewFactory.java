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

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.uniulm.omi.cloudiator.sword.domain.Cloud;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.BaseJCloudsViewFactory;
import java.util.Properties;
import org.jclouds.aws.ec2.reference.AWSEC2Constants;

/**
 * Created by daniel on 26.04.16.
 */
public class EC2JCloudsViewFactory extends BaseJCloudsViewFactory {

  @Inject(optional = true)
  @Named(EC2Constants.PROPERTY_EC2_AMI_QUERY)
  private String amiQuery =
      null;

  @Inject(optional = true)
  @Named(EC2Constants.PROPERTY_EC2_CC_AMI_QUERY)
  private String
      amiCcQuery = null;

  @Inject(optional = true)
  @Named(EC2Constants.PROPERTY_EC2_AMI_OWNERS)
  private String
      amiOwners = null;


  @Inject
  public EC2JCloudsViewFactory(Cloud cloud) {
    super(cloud);
  }

  @Override
  protected Properties overrideProperties(Properties properties) {
    if (amiQuery != null) {
      properties.setProperty(AWSEC2Constants.PROPERTY_EC2_AMI_QUERY, amiQuery);
    }
    if (amiCcQuery != null) {
      properties.setProperty(AWSEC2Constants.PROPERTY_EC2_CC_AMI_QUERY, amiCcQuery);
    }
    if (amiOwners != null) {
      properties.setProperty(AWSEC2Constants.PROPERTY_EC2_AMI_OWNERS, amiOwners);
    }
    return properties;
  }
}
