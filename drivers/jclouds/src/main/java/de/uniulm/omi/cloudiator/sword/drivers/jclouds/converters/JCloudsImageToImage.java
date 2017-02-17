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

package de.uniulm.omi.cloudiator.sword.drivers.jclouds.converters;



import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import de.uniulm.omi.cloudiator.domain.OperatingSystems;
import de.uniulm.omi.cloudiator.domain.Image;
import de.uniulm.omi.cloudiator.domain.ImageBuilder;
import de.uniulm.omi.cloudiator.domain.Location;
import org.jclouds.compute.domain.OperatingSystem;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 01.12.14.
 */
public class JCloudsImageToImage
    implements OneWayConverter<org.jclouds.compute.domain.Image, Image> {

    private final OneWayConverter<org.jclouds.domain.Location, Location> locationConverter;
    private final OneWayConverter<OperatingSystem, de.uniulm.omi.cloudiator.domain.OperatingSystem>
        operatingSystemConverter;

    @Inject public JCloudsImageToImage(
        OneWayConverter<org.jclouds.domain.Location, Location> locationConverter,
        OneWayConverter<OperatingSystem, de.uniulm.omi.cloudiator.domain.OperatingSystem> operatingSystemConverter) {

        checkNotNull(locationConverter);
        checkNotNull(operatingSystemConverter);

        this.locationConverter = locationConverter;
        this.operatingSystemConverter = operatingSystemConverter;
    }

    @Override public Image apply(org.jclouds.compute.domain.Image image) {
        if(image == null) {
            return null;
        }
        de.uniulm.omi.cloudiator.domain.OperatingSystem os = OperatingSystems.unknown();
        if (image.getOperatingSystem() != null) {
            os = operatingSystemConverter.apply(image.getOperatingSystem());
        }

        return ImageBuilder.newBuilder().id(image.getId()).providerId(image.getProviderId())
            .name(forceName(image)).location(locationConverter.apply(image.getLocation())).os(os)
            .build();
    }

    private String forceName(org.jclouds.compute.domain.Image image) {
        if (image.getName() == null) {
            return image.getId();
        }
        return image.getName();
    }
}
