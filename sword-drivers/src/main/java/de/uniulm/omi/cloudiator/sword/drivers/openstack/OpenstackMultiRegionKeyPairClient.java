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

package de.uniulm.omi.cloudiator.sword.drivers.openstack;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.KeyPair;
import org.jclouds.openstack.nova.v2_0.extensions.KeyPairApi;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by daniel on 19.05.15.
 */

public class OpenstackMultiRegionKeyPairClient implements OpenstackKeyPairClient {

    private OpenstackKeyPairSingleRegionClient openstackKeyPairSingleRegionClient;
    private Iterable<String> regions;

    @Inject public OpenstackMultiRegionKeyPairClient(NovaApi novaApi) {
        this.openstackKeyPairSingleRegionClient = new OpenstackKeyPairSingleRegionClient(novaApi);
        this.regions = novaApi.getConfiguredRegions();
    }

    @Override public boolean isAvailable() {
        for (String region : regions) {
            if (!openstackKeyPairSingleRegionClient.isAvailable(region)) {
                return false;
            }
        }
        return true;
    }

    @Override public KeyPair create(String name) {
        KeyPair keyPair = null;
        for (String region : regions) {
            keyPair = openstackKeyPairSingleRegionClient.create(name, region);
        }
        return keyPair;
    }

    @Override public KeyPair createWithPublicKey(String name, String publicKey) {
        KeyPair keyPair = null;
        for (String region : regions) {
            keyPair =
                openstackKeyPairSingleRegionClient.createWithPublicKey(name, publicKey, region);
        }
        return keyPair;
    }

    @Override public boolean delete(String name) {
        boolean success = true;
        for (String region : regions) {
            if (!openstackKeyPairSingleRegionClient.delete(name, region)) {
                success = false;
            }
        }
        return success;
    }

    @Override public KeyPair get(String name) {
        KeyPair keyPair = null;
        for (String region : regions) {
            if (openstackKeyPairSingleRegionClient.get(name, region) == null) {
                return null;
            } else {
                keyPair = openstackKeyPairSingleRegionClient.get(name, region);
            }
        }
        return keyPair;
    }

    @Override public Iterable<KeyPair> list() {
        Set<KeyPair> keyPairs = new HashSet<>();
        for (String region : regions) {
            Set<KeyPair> keysInRegion =
                Sets.newHashSet(openstackKeyPairSingleRegionClient.list(region));
            //remove all from keypairs, as those were not found in this region.
            keyPairs.retainAll(keysInRegion);
            // add ours, and hope that the set solves double entries....
            // TODO: check this
            keyPairs.addAll(keysInRegion);
        }
        return keyPairs;
    }

    private class OpenstackKeyPairSingleRegionClient {

        private final NovaApi novaApi;

        private OpenstackKeyPairSingleRegionClient(NovaApi novaApi) {
            this.novaApi = novaApi;
        }

        public boolean isAvailable(String region) {
            return novaApi.getKeyPairApi(region).isPresent();
        }

        private KeyPairApi getKeyPairApi(String region) {
            checkArgument(isAvailable(region),
                "KeyPair Api is not available for region " + region + ".");
            return novaApi.getKeyPairApi(region).get();
        }

        public KeyPair create(String name, String region) {
            return getKeyPairApi(region).create(name);
        }

        public KeyPair createWithPublicKey(String name, String publicKey, String region) {
            return getKeyPairApi(region).createWithPublicKey(name, publicKey);
        }

        public boolean delete(String name, String region) {
            return getKeyPairApi(region).delete(name);
        }

        public KeyPair get(String name, String region) {
            return getKeyPairApi(region).get(name);
        }

        public Iterable<KeyPair> list(String region) {
            return getKeyPairApi(region).list();
        }
    }
}
