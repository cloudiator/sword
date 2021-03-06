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

package de.uniulm.omi.cloudiator.sword.drivers.google.converters;

import de.uniulm.omi.cloudiator.sword.drivers.jclouds.converters.AbstractTemplateOptionsToTemplateOptions;
import org.jclouds.compute.options.TemplateOptions;

/**
 * Created by daniel on 28.10.15.
 */
public class TemplateOptionsToGoogleTemplateOptions
    extends AbstractTemplateOptionsToTemplateOptions {

  @Override
  protected TemplateOptions convert(
      de.uniulm.omi.cloudiator.sword.domain.TemplateOptions templateOptions) {
    return new TemplateOptions();
  }
}
