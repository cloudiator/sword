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

import de.uniulm.omi.cloudiator.domain.OperatingSystemFamily;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import org.jclouds.compute.domain.OsFamily;

/**
 * Created by daniel on 10.03.16.
 */
public class JCloudsOperatingSystemFamilyConverter
    implements OneWayConverter<OsFamily, OperatingSystemFamily> {

  @Override
  public OperatingSystemFamily apply(OsFamily osFamily) {
    //todo: we probably need a map conversion here
    return OperatingSystemFamily.fromValue(osFamily.value());
  }
}
