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

package de.uniulm.omi.cloudiator.sword.drivers.jclouds;

import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import de.uniulm.omi.cloudiator.sword.domain.Cloud;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.logging.JCloudsLoggingModule;
import de.uniulm.omi.cloudiator.sword.logging.LoggerFactory;
import de.uniulm.omi.cloudiator.sword.properties.Constants;
import org.jclouds.ContextBuilder;
import org.jclouds.View;

import java.io.Closeable;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 14.12.15.
 */
@Singleton public class BaseJCloudsViewFactory implements JCloudsViewFactory {

    private final Cloud cloud;
    private final LoggerFactory loggerFactory;
    @Inject(optional = true) @Named(Constants.SWORD_REGIONS) private String regions = null;
    @Inject(optional = true) @Named(Constants.REQUEST_TIMEOUT) private String requestTimeout = null;

    @Inject public BaseJCloudsViewFactory(Cloud cloud, LoggerFactory loggerFactory) {

        checkNotNull(cloud, "cloud is null");
        checkNotNull(loggerFactory);

        this.cloud = cloud;
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
    protected Collection<Module> overrideModules() {
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
        if (requestTimeout != null) {
            properties.setProperty(org.jclouds.Constants.PROPERTY_REQUEST_TIMEOUT, requestTimeout);
        }

        //todo duplicates code from NovaApiProvider
        // loading ssh module of jclouds as it seems to be required for google,
        // we are using the jschsshclient as the sshj conflicts with the overthere bouncy castle impl.

        Set<Module> moduleSet = new HashSet<>();
        moduleSet.addAll(overrideModules());
        moduleSet.add(new JCloudsLoggingModule(loggerFactory));

        ContextBuilder contextBuilder = ContextBuilder.newBuilder(cloud.api().providerName());
        contextBuilder.credentials(cloud.credential().user(), cloud.credential().password())
            .modules(moduleSet).overrides(overrideProperties(properties));


        // setting optional endpoint, check for present first
        // as builder does not allow null values...
        if (cloud.endpoint().isPresent()) {
            contextBuilder.endpoint(cloud.endpoint().get());
        }

        return contextBuilder;
    }
}
