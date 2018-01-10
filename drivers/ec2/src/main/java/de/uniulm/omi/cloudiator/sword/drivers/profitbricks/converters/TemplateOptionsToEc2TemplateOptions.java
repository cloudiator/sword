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

package de.uniulm.omi.cloudiator.sword.drivers.profitbricks.converters;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.converters.AbstractTemplateOptionsToTemplateOptions;
import de.uniulm.omi.cloudiator.sword.drivers.profitbricks.EC2Constants;
import java.nio.charset.Charset;
import org.jclouds.aws.ec2.compute.AWSEC2TemplateOptions;
import org.jclouds.compute.options.TemplateOptions;

/**
 * Created by daniel on 28.10.15.
 */
public class TemplateOptionsToEc2TemplateOptions extends AbstractTemplateOptionsToTemplateOptions {

  @Inject(optional = true)
  @Named(EC2Constants.PROPERTY_EC2_DEFAULT_VPC)
  private String
      defaultVpc = null;

  @Override
  protected TemplateOptions convert(
      de.uniulm.omi.cloudiator.sword.domain.TemplateOptions templateOptions) {
    AWSEC2TemplateOptions ec2TemplateOptions = new AWSEC2TemplateOptions();
    final String keyPairName = templateOptions.keyPairName();
    if (keyPairName != null) {
      ec2TemplateOptions.authorizePublicKey(keyPairName);
    }
    if (defaultVpc != null) {
      ec2TemplateOptions.subnetId(defaultVpc);
    }
    if (templateOptions.userData() != null) {
      ec2TemplateOptions
          .userData(templateOptions.userData().getBytes(Charset.forName("UTF-8")));
    }
    return ec2TemplateOptions;
  }
}
