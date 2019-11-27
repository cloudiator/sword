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

package de.uniulm.omi.cloudiator.sword.drivers.jclouds.converters;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.domain.OperatingSystemArchitecture;
import de.uniulm.omi.cloudiator.domain.OperatingSystemBuilder;
import de.uniulm.omi.cloudiator.domain.OperatingSystemFamily;
import de.uniulm.omi.cloudiator.domain.OperatingSystemVersion;
import de.uniulm.omi.cloudiator.domain.OperatingSystemVersions;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import org.jclouds.compute.domain.OperatingSystem;
import org.jclouds.compute.domain.OsFamily;

/**
 * Created by daniel on 10.03.16.
 */
public class JCloudsOperatingSystemConverter
    implements OneWayConverter<OperatingSystem, de.uniulm.omi.cloudiator.domain.OperatingSystem> {

  private final OneWayConverter<OsFamily, OperatingSystemFamily> operatingSystemFamilyConverter;

  @Inject
  JCloudsOperatingSystemConverter(
      OneWayConverter<OsFamily, OperatingSystemFamily> operatingSystemFamilyConverter) {
    checkNotNull(operatingSystemFamilyConverter);
    this.operatingSystemFamilyConverter = operatingSystemFamilyConverter;
  }

  @Override
  public de.uniulm.omi.cloudiator.domain.OperatingSystem apply(OperatingSystem operatingSystem) {

    OperatingSystemFamily operatingSystemFamily =
        operatingSystemFamilyConverter.apply(operatingSystem.getFamily());

    OperatingSystemVersion operatingSystemVersion;
    try {
      operatingSystemVersion = operatingSystemFamily.operatingSystemVersionFormat()
          .parseName(operatingSystem.getVersion());
    } catch (IllegalArgumentException e) {
      operatingSystemVersion = OperatingSystemVersions.unknown();
    }

    return OperatingSystemBuilder.newBuilder().version(operatingSystemVersion).
        architecture(OperatingSystemArchitecture.UNKNOWN).family(operatingSystemFamily).build();
  }
}
