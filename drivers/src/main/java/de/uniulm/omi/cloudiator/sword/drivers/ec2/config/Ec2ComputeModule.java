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

package de.uniulm.omi.cloudiator.sword.drivers.ec2.config;

import com.google.inject.Injector;
import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.sword.api.domain.TemplateOptions;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.drivers.ec2.EC2JCloudsViewFactory;
import de.uniulm.omi.cloudiator.sword.drivers.ec2.converters.TemplateOptionsToEc2TemplateOptions;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.JCloudsViewFactory;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.config.JCloudsComputeModule;
import org.jclouds.compute.domain.ComputeMetadata;

/**
 * Created by daniel on 02.12.14.
 */
public class Ec2ComputeModule extends JCloudsComputeModule {

    @Override protected JCloudsViewFactory overrideJCloudsViewFactory(Injector injector,
        JCloudsViewFactory originalFactory) {
        return injector.getInstance(EC2JCloudsViewFactory.class);
    }

    @Override
    protected Class<? extends OneWayConverter<TemplateOptions, org.jclouds.compute.options.TemplateOptions>> templateOptionsConverter() {
        return TemplateOptionsToEc2TemplateOptions.class;
    }
}
