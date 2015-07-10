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

package de.uniulm.omi.cloudiator.sword.drivers.openstack.config;

import com.google.common.base.Optional;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import de.uniulm.omi.cloudiator.sword.api.converters.OneWayConverter;
import de.uniulm.omi.cloudiator.sword.api.domain.TemplateOptions;
import de.uniulm.omi.cloudiator.sword.api.extensions.KeyPairService;
import de.uniulm.omi.cloudiator.sword.api.extensions.PublicIpService;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.config.JCloudsComputeModule;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.converters.NovaKeyPairToKeypair;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.converters.TemplateOptionsToNovaTemplateOptions;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.extendsions.OpenstackKeyPairService;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.extendsions.OpenstackPublicIpService;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.KeyPair;


/**
 * Created by daniel on 19.01.15.
 */
public class OpenstackComputeModule extends JCloudsComputeModule {

    @Override
    protected Class<? extends OneWayConverter<TemplateOptions, org.jclouds.compute.options.TemplateOptions>> templateOptionsConverter() {
        return TemplateOptionsToNovaTemplateOptions.class;
    }

    @Override protected void configure() {
        super.configure();
        bind(NovaApi.class).toProvider(NovaApiProvider.class);
        bind(
            new TypeLiteral<OneWayConverter<KeyPair, de.uniulm.omi.cloudiator.sword.api.domain.KeyPair>>() {
            }).to(NovaKeyPairToKeypair.class);
    }

    @Override protected Optional<PublicIpService> provideFloatingIpService(Injector injector) {
        return Optional.fromNullable(injector.getInstance(OpenstackPublicIpService.class));
    }

    @Override protected Optional<KeyPairService> provideKeyPairService(Injector injector) {
        return Optional.fromNullable(injector.getInstance(OpenstackKeyPairService.class));
    }
}
