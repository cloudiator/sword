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

package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.internal;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 17.11.16.
 */
public enum KeyStoneVersion {
    V2("v2", OsClientV2Factory.class, OsClientV2RegionSupplier.class), V3("v3", OsClientV3Factory.class, OsClientV3RegionSupplier.class);

    private final String keyWord;
    private final Class<? extends OsClientFactory> clientFactoryClass;
    private final Class<? extends RegionSupplier> regionSupplierClass;

    KeyStoneVersion(String keyWord, Class<? extends OsClientFactory> clientFactoryClass, Class<? extends RegionSupplier> regionSupplierClass) {
        this.keyWord = keyWord;
        this.clientFactoryClass = clientFactoryClass;
        this.regionSupplierClass = regionSupplierClass;
    }

    public static KeyStoneVersion fromEndpoint(String endpoint) {
        checkNotNull(endpoint, "endpoint is null.");
        for (KeyStoneVersion keystoneVersion : values()) {
            if (endpoint.contains(keystoneVersion.keyWord)) {
                return keystoneVersion;
            }
        }
        return null;
    }

    public Class<? extends OsClientFactory> clientFactoryClass() {
        return clientFactoryClass;
    }

    public Class<? extends RegionSupplier> regionSupplierClass() {
        return regionSupplierClass;
    }
}
