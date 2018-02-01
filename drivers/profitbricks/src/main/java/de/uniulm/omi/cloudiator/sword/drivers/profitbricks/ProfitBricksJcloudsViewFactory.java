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

package de.uniulm.omi.cloudiator.sword.drivers.profitbricks;

import de.uniulm.omi.cloudiator.sword.domain.Cloud;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.BaseJCloudsViewFactory;
import de.uniulm.omi.cloudiator.sword.logging.LoggerFactory;
import java.util.Properties;
import javax.inject.Inject;
import org.jclouds.Constants;

public class ProfitBricksJcloudsViewFactory extends BaseJCloudsViewFactory {

  @Inject
  public ProfitBricksJcloudsViewFactory(Cloud cloud,
      LoggerFactory loggerFactory) {
    super(cloud, loggerFactory);
  }

  @Override
  protected Properties overrideProperties(Properties properties) {
    Properties superProperties = super.overrideProperties(properties);
    superProperties.setProperty(Constants.PROPERTY_USER_AGENT, "jclouds/1.0 urlfetch/1.4.3");
    return superProperties;
  }
}
