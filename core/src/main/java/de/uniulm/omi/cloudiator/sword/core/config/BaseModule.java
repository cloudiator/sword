/*
 * Copyright (c) 2014-2015 University of Ulm
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.uniulm.omi.cloudiator.sword.core.config;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import de.uniulm.omi.cloudiator.sword.api.ServiceContext;
import de.uniulm.omi.cloudiator.domain.Properties;
import de.uniulm.omi.cloudiator.sword.api.service.ConnectionService;
import de.uniulm.omi.cloudiator.sword.api.util.NamingStrategy;
import de.uniulm.omi.cloudiator.sword.core.base.BaseConnectionService;
import de.uniulm.omi.cloudiator.sword.core.util.GroupEncodedIntoNameNamingStrategy;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 02.12.14.
 */
public class BaseModule extends AbstractModule {

    private final ServiceContext serviceContext;
    private final Properties properties;

    public BaseModule(ServiceContext serviceContext, @Nullable Properties properties) {

        checkNotNull(serviceContext);

        this.serviceContext = serviceContext;
        this.properties = properties;
    }

    @Override protected void configure() {
        bind(ServiceContext.class).toInstance(this.serviceContext);
        bind(ConnectionService.class).to(BaseConnectionService.class);
        if (this.properties != null) {
            Names.bindProperties(binder(), this.properties.getProperties());
        }
        bind(NamingStrategy.class).to(GroupEncodedIntoNameNamingStrategy.class);
    }
}
