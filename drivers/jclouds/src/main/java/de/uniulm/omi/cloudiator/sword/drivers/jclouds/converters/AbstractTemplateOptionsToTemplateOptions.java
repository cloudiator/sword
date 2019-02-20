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

package de.uniulm.omi.cloudiator.sword.drivers.jclouds.converters;


import com.google.common.primitives.Ints;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.uniulm.omi.cloudiator.sword.domain.TemplateOptions;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.config.JCloudsConstants;
import de.uniulm.omi.cloudiator.sword.properties.Constants;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import javax.annotation.Nullable;

/**
 * Created by daniel on 10.07.15.
 */
public abstract class AbstractTemplateOptionsToTemplateOptions
    implements OneWayConverter<TemplateOptions, org.jclouds.compute.options.TemplateOptions> {

  private @Inject(optional = true)
  @Named(Constants.DEFAULT_SECURITY_GROUP)
  String defaultSecurityGroup = null;

  private @Inject(optional = true)
  @Named(JCloudsConstants.OVERRIDE_LOGIN_PASSWORD)
  String overrideLoginPassword = null;

  private @Inject(optional = true)
  @Named(JCloudsConstants.OVERRIDE_LOGIN_USER)
  String overrideLoginUser = null;

  @Nullable
  @Override
  public org.jclouds.compute.options.TemplateOptions apply(TemplateOptions templateOptions) {

    org.jclouds.compute.options.TemplateOptions jcloudsOptions = convert(templateOptions);
    jcloudsOptions.inboundPorts(Ints.toArray(templateOptions.inboundPorts()));
    jcloudsOptions.userMetadata(templateOptions.tags());

    if (defaultSecurityGroup != null) {
      jcloudsOptions.securityGroups(defaultSecurityGroup);
    }

    if (overrideLoginPassword != null) {
      jcloudsOptions.overrideLoginPassword(overrideLoginPassword);
    }

    if (overrideLoginUser != null) {
      jcloudsOptions.overrideLoginUser(overrideLoginUser);
    }

    return jcloudsOptions;
  }

  protected abstract org.jclouds.compute.options.TemplateOptions convert(
      TemplateOptions templateOptions);
}
