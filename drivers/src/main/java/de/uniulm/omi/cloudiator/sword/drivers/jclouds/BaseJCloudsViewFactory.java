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
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import de.uniulm.omi.cloudiator.sword.api.ServiceConfiguration;
import de.uniulm.omi.cloudiator.sword.api.logging.LoggerFactory;
import de.uniulm.omi.cloudiator.sword.api.properties.Constants;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.logging.JCloudsLoggingModule;
import org.jclouds.ContextBuilder;
import org.jclouds.View;
import org.jclouds.googlecloud.config.GoogleCloudProperties;
import org.jclouds.ssh.jsch.config.JschSshClientModule;

import java.io.Closeable;
import java.util.Collections;
import java.util.Properties;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 14.12.15.
 */
@Singleton public class BaseJCloudsViewFactory implements JCloudsViewFactory {

    private final ServiceConfiguration serviceConfiguration;
    private final LoggerFactory loggerFactory;
    @Inject(optional = true) @Named(Constants.SWORD_REGIONS) private String regions = null;

    @Inject public BaseJCloudsViewFactory(ServiceConfiguration serviceConfiguration,
        LoggerFactory loggerFactory) {

        checkNotNull(serviceConfiguration);
        checkNotNull(loggerFactory);

        this.serviceConfiguration = serviceConfiguration;
        this.loggerFactory = loggerFactory;
    }

    /**
     * Extension point to override the username.
     *
     * @param username the original username.
     * @return the changed username.
     */
    protected String overrideUsername(String username) {
        return username;
    }

    /**
     * Extension point for loading additional jclouds modules
     *
     * @return additional modules to load during the creation of the jclouds client
     */
    protected Iterable<? extends Module> overrideModules() {
        return Collections.emptyList();
    }

    /**
     * Extension point for setting additional properties.
     *
     * @param properties the properties object for setting additional properties.
     * @return fluent interface
     */
    protected Properties overrideProperties(Properties properties) {
        return properties;
    }

    @Override public final <V extends View> V buildJCloudsView(Class<V> viewType) {
        return buildContext().buildView(viewType);
    }

    @Override public final <A extends Closeable> A buildJCloudsApi(Class<A> api) {
        return buildContext().buildApi(api);
    }

    private ContextBuilder buildContext() {
        //todo ugly hack
        final Properties properties = new Properties();
        if (regions != null) {
            properties.setProperty("jclouds.regions", regions);
        }

        //todo more ugly hack to workaround wrong parsing in jclouds
        if (serviceConfiguration.getProvider().equals("google-compute-engine")) {
            String userName = serviceConfiguration.getCredentials().user();
            String projectName =
                userName.substring(userName.indexOf("@") + 1, userName.indexOf(".iam"));
            properties.setProperty(GoogleCloudProperties.PROJECT_NAME, projectName);
        }

        //todo duplicates code from NovaApiProvider
        // loading ssh module of jclouds as it seems to be required for google,
        // we are using the jschsshclient as the sshj conflicts with the overthere bouncy castle impl.
        ContextBuilder contextBuilder =
            ContextBuilder.newBuilder(serviceConfiguration.getProvider());
        contextBuilder.credentials(serviceConfiguration.getCredentials().user(),
            serviceConfiguration.getCredentials().password())
            .modules(ImmutableSet.of(new JCloudsLoggingModule(loggerFactory)))
            .modules(Collections.singletonList(new JschSshClientModule()))
            .overrides(overrideProperties(properties));


        // setting optional endpoint, check for present first
        // as builder does not allow null values...
        if (serviceConfiguration.getEndpoint().isPresent()) {
            contextBuilder.endpoint(serviceConfiguration.getEndpoint().get());
        }

        return contextBuilder;
    }
}
