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

package de.uniulm.omi.cloudiator.sword.multicloud.config;

import com.google.common.base.Supplier;
import com.google.inject.*;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import com.google.inject.util.Providers;
import de.uniulm.omi.cloudiator.sword.annotations.Base;
import de.uniulm.omi.cloudiator.sword.config.DefaultMetaModule;
import de.uniulm.omi.cloudiator.sword.domain.Pricing;
import de.uniulm.omi.cloudiator.sword.multicloud.MultiCloudService;
import de.uniulm.omi.cloudiator.sword.multicloud.MultiCloudServiceImpl;
import de.uniulm.omi.cloudiator.sword.multicloud.pricing.PricingSupplierFactory;
import de.uniulm.omi.cloudiator.sword.multicloud.pricing.aws.AWSPricingSupplier;
import de.uniulm.omi.cloudiator.sword.multicloud.service.*;
import de.uniulm.omi.cloudiator.sword.remote.AbstractRemoteModule;
import de.uniulm.omi.cloudiator.sword.service.ComputeService;
import de.uniulm.omi.cloudiator.sword.service.PricingService;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * Created by daniel on 23.01.17.
 */
public class MultiCloudModule extends AbstractModule {

  @Nullable
  private final AbstractRemoteModule remoteModule;
  private final DefaultMetaModule metaModule;

  public MultiCloudModule(
      @Nullable AbstractRemoteModule remoteModule, DefaultMetaModule metaModule) {
    this.remoteModule = remoteModule;
    this.metaModule = metaModule;
  }

  @Override
  protected void configure() {
    bind(AbstractRemoteModule.class).toProvider(Providers.of(remoteModule));
    bind(DefaultMetaModule.class).toProvider(Providers.of(metaModule));
    bind(MultiCloudService.class).to(MultiCloudServiceImpl.class).in(Singleton.class);
    bind(ComputeService.class).to(MultiCloudComputeService.class).in(Singleton.class);
    bind(ComputeServiceProvider.class).to(CloudRegistryComputeServiceProvider.class)
        .in(Singleton.class);
    bind(CloudRegistry.class).to(CloudRegistryComputeServiceProvider.class).in(Singleton.class);
    bind(PricingService.class).to(MultiCloudPricingService.class).in(Singleton.class);

    install(new FactoryModuleBuilder()
            .implement(new TypeLiteral<Supplier<Set<Pricing>>>(){}, Names.named(PricingSupplierFactory.awsName), AWSPricingSupplier.class)
            .build(PricingSupplierFactory.class));
  }

  @Provides
  @Base
  ComputeServiceFactory provideBaseComputeServiceFactory(Injector injector) {
    return injector.getInstance(BaseComputeServiceFactory.class);
  }

  @Provides
  @Singleton
  ComputeServiceFactory provideComputeServiceFactory(Injector injector) {
    return injector.getInstance(CachingComputeServiceFactory.class);
  }


}
