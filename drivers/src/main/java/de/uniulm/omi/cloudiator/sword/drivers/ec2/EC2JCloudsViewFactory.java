/*
 * Copyright (c) 2014-2016 University of Ulm
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
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import de.uniulm.omi.cloudiator.sword.api.ServiceConfiguration;
import de.uniulm.omi.cloudiator.sword.api.logging.LoggerFactory;
import de.uniulm.omi.cloudiator.sword.api.properties.Constants;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.BaseJCloudsViewFactory;
import org.jclouds.aws.ec2.reference.AWSEC2Constants;

import java.util.Properties;

/**
 * Created by daniel on 26.04.16.
 */
@Singleton public class EC2JCloudsViewFactory extends BaseJCloudsViewFactory {

    @Inject(optional = true) @Named(Constants.AWSConstants.PROPERTY_EC2_AMI_QUERY) private String
        amiQuery = "owner-id=amazon,self;state=available;image-type=machine";

    @Inject public EC2JCloudsViewFactory(ServiceConfiguration serviceConfiguration,
        LoggerFactory loggerFactory) {
        super(serviceConfiguration, loggerFactory);
    }

    @Override protected Properties overrideProperties(Properties properties) {
        properties.setProperty(AWSEC2Constants.PROPERTY_EC2_AMI_QUERY, amiQuery);
        return properties;
    }
}
