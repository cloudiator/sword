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

package de.uniulm.omi.cloudiator.sword.drivers.openstack.converters;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.converters.AbstractTemplateOptionsToTemplateOptions;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.OpenstackConstants;
import java.nio.charset.Charset;
import org.jclouds.compute.options.TemplateOptions;
import org.jclouds.openstack.nova.v2_0.compute.options.NovaTemplateOptions;

/**
 * Created by daniel on 10.07.15.
 */
public class TemplateOptionsToNovaTemplateOptions extends AbstractTemplateOptionsToTemplateOptions {

  private @Inject(optional = true)
  @Named(OpenstackConstants.DEFAULT_AVAILABILITY_ZONE_PROPERTY)
  String availabilityZone = null;

  @Override
  protected TemplateOptions convert(
      de.uniulm.omi.cloudiator.sword.domain.TemplateOptions templateOptions) {

    NovaTemplateOptions novaTemplateOptions = new NovaTemplateOptions();
    final String keyPairName = templateOptions.keyPairName();
    if (keyPairName != null) {
      novaTemplateOptions.keyPairName(keyPairName);
    }
    novaTemplateOptions.generateKeyPair(true);
    if (availabilityZone != null) {
      novaTemplateOptions.availabilityZone(availabilityZone);
    }
    if (templateOptions.userData() != null) {
      novaTemplateOptions
          .userData(templateOptions.userData().getBytes(Charset.forName("UTF-8")));
    }
    return novaTemplateOptions;
  }


}
