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

package de.uniulm.omi.cloudiator.sword.drivers.openstack.converters;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.sword.api.domain.LoginCredential;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.core.domain.VirtualMachineBuilder;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.converters.JCloudsComputeMetadataToVirtualMachine;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.domain.Location;
import org.jclouds.domain.LoginCredentials;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.Server;
import org.jclouds.openstack.nova.v2_0.domain.ServerExtendedAttributes;
import org.jclouds.openstack.nova.v2_0.domain.regionscoped.RegionAndId;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.Function;

/**
 * Overwrites functionality of the standard jclouds converter {@link de.uniulm.omi.cloudiator.sword.drivers.jclouds.converters.JCloudsComputeMetadataToVirtualMachine}
 * <p/>
 * The jclouds implementation for Openstack seems to guess the username for the
 * login credentials. This obviously causes problems when using the for logins.
 * <p/>
 * For this purpose, this custom converter removes the login credentials from
 * the object until we find a better solution for this issue.
 * <p/>
 * In addition we overwrite the jclouds location with the correct location.
 */
public class ComputeMetadataConverterOverwrite
    implements OneWayConverter<ComputeMetadata, VirtualMachine> {

    private final OneWayConverter<ComputeMetadata, VirtualMachine> baseJcloudsConverter;
    private final RetrieveLocationForComputeMetaData locationRetriever;

    @Inject public ComputeMetadataConverterOverwrite(
        OneWayConverter<LoginCredentials, LoginCredential> loginCredentialConverter,
        OneWayConverter<Location, de.uniulm.omi.cloudiator.sword.api.domain.Location> locationConverter,
        NovaApi novaApi,
        Supplier<Set<de.uniulm.omi.cloudiator.sword.api.domain.Location>> locationSupplier) {
        //todo directly inject dependency?
        this.baseJcloudsConverter =
            new JCloudsComputeMetadataToVirtualMachine(loginCredentialConverter, locationConverter);
        this.locationRetriever = new RetrieveLocationForComputeMetaData(novaApi, locationSupplier);
    }

    @Override public VirtualMachine apply(ComputeMetadata computeMetadata) {
        return VirtualMachineBuilder.of(baseJcloudsConverter.apply(computeMetadata))
            .loginCredential(null).location(locationRetriever.apply(computeMetadata)).build();
    }

    private static class RetrieveLocationForComputeMetaData
        implements Function<ComputeMetadata, de.uniulm.omi.cloudiator.sword.api.domain.Location> {

        private final NovaApi novaApi;
        private final Supplier<Set<de.uniulm.omi.cloudiator.sword.api.domain.Location>>
            locationSupplier;

        private RetrieveLocationForComputeMetaData(NovaApi novaApi,
            Supplier<Set<de.uniulm.omi.cloudiator.sword.api.domain.Location>> locationSupplier) {
            this.novaApi = novaApi;
            this.locationSupplier = locationSupplier;
        }

        @Override public de.uniulm.omi.cloudiator.sword.api.domain.Location apply(
            ComputeMetadata computeMetadata) {
            final RegionAndId regionAndId = RegionAndId.fromSlashEncoded(computeMetadata.getId());
            final Server server =
                novaApi.getServerApi(regionAndId.getRegion()).get(regionAndId.getId());
            final Optional<ServerExtendedAttributes> extendedAttributes =
                server.getExtendedAttributes();

            //first try the host
            if (extendedAttributes.isPresent()) {
                if (extendedAttributes.get().getHostName() != null) {
                    return getLocationById(RegionAndId.fromRegionAndId(regionAndId.getRegion(),
                        extendedAttributes.get().getHostName()).slashEncode());
                }
            }

            //then try the availability zone
            if (server.getAvailabilityZone().isPresent()) {
                return getLocationById(RegionAndId
                    .fromRegionAndId(regionAndId.getRegion(), server.getAvailabilityZone().get())
                    .slashEncode());
            }

            // fallback to region
            return getLocationById(regionAndId.getRegion());
        }

        @Nullable
        private de.uniulm.omi.cloudiator.sword.api.domain.Location getLocationById(String id) {
            return locationSupplier.get().stream().filter(location -> location.id().equals(id))
                .findFirst().orElseThrow(
                    () -> new IllegalArgumentException("Could not find location with id = " + id));
        }

    }
}
