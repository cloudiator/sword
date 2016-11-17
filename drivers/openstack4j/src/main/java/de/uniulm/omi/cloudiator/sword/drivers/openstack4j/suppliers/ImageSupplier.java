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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.suppliers;

import com.google.common.base.Supplier;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.sword.api.domain.Image;
import org.openstack4j.api.OSClient;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by daniel on 14.11.16.
 */
public class ImageSupplier implements Supplier<Set<Image>> {

    private final OSClient osClient;
    private final OneWayConverter<org.openstack4j.model.compute.Image, Image> converter;

    @Inject public ImageSupplier(OSClient osClient,
        OneWayConverter<org.openstack4j.model.compute.Image, Image> converter) {
        this.osClient = osClient;
        this.converter = converter;
    }

    @Override public Set<Image> get() {
        return osClient.compute().images().list().stream()
            .map((Function<org.openstack4j.model.compute.Image, Image>) converter::apply)
            .collect(Collectors.toSet());
    }
}
