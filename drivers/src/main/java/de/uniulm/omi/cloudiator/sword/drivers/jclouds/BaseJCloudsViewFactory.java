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

package de.uniulm.omi.cloudiator.sword.drivers.jclouds;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.api.ServiceConfiguration;
import de.uniulm.omi.cloudiator.sword.api.logging.LoggerFactory;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.logging.JCloudsLoggingModule;
import org.jclouds.ContextBuilder;
import org.jclouds.View;
import org.jclouds.aws.ec2.reference.AWSEC2Constants;

import java.io.Closeable;
import java.util.Properties;

/**
 * Created by daniel on 14.12.15.
 */
public class BaseJCloudsViewFactory implements JCloudsViewFactory {

    private final ContextBuilder contextBuilder;

    @Inject public BaseJCloudsViewFactory(ServiceConfiguration serviceConfiguration,
        LoggerFactory loggerFactory) {

        //todo ugly hack
        final Properties properties = new Properties();
        properties.setProperty(AWSEC2Constants.PROPERTY_EC2_AMI_QUERY,
            "owner-id=amazon,self;state=available;image-type=machine");

        //todo duplicates code from NovaApiProvider
        this.contextBuilder = ContextBuilder.newBuilder(serviceConfiguration.getProvider());
        contextBuilder.credentials(serviceConfiguration.getCredentials().user(),
            serviceConfiguration.getCredentials().password())
            .modules(ImmutableSet.of(new JCloudsLoggingModule(loggerFactory)))
            .overrides(properties);


        // setting optional endpoint, check for present first
        // as builder does not allow null values...
        if (serviceConfiguration.getEndpoint().isPresent()) {
            contextBuilder.endpoint(serviceConfiguration.getEndpoint().get());
        }
    }

    @Override public <V extends View> V buildJCloudsView(Class<V> viewType) {
        return contextBuilder.buildView(viewType);
    }

    @Override public <A extends Closeable> A buildJCloudsApi(Class<A> api) {
        return contextBuilder.buildApi(api);
    }
}
