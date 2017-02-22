/*
 * Copyright (c) 2014-2017 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.multicloud;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.uniulm.omi.cloudiator.sword.logging.AbstractLoggingModule;
import de.uniulm.omi.cloudiator.sword.multicloud.config.MultiCloudModule;
import de.uniulm.omi.cloudiator.sword.remote.internal.ExtendedRemoteModule;

/**
 * Created by daniel on 23.01.17.
 */
public class MultiCloudBuilder {

    private ExtendedRemoteModule remoteModule;
    private AbstractLoggingModule loggingModule;

    private MultiCloudBuilder() {

    }

    public static MultiCloudBuilder newBuilder() {
        return new MultiCloudBuilder();
    }

    public MultiCloudBuilder loggingModule(AbstractLoggingModule loggingModule) {
        this.loggingModule = loggingModule;
        return this;
    }

    public MultiCloudBuilder remoteModule(ExtendedRemoteModule abstractRemoteModule) {
        this.remoteModule = abstractRemoteModule;
        return this;
    }

    public MultiCloudService build() {
        final Injector injector =
            Guice.createInjector(new MultiCloudModule(loggingModule, remoteModule));
        return injector.getInstance(MultiCloudService.class);
    }



}
