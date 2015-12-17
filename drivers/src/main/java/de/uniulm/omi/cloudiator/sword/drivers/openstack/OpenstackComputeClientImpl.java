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

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.sword.api.ServiceConfiguration;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.JCloudsComputeClientImpl;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.JCloudsViewFactory;
import org.jclouds.domain.Location;
import org.jclouds.domain.LocationBuilder;
import org.jclouds.domain.LocationScope;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.Host;
import org.jclouds.openstack.nova.v2_0.domain.regionscoped.RegionAndId;
import org.jclouds.openstack.nova.v2_0.domain.zonescoped.AvailabilityZone;
import org.jclouds.rest.AuthorizationException;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A custom implementation of the jclouds compute client for Openstack.
 * <p/>
 * Adds the availability zones of Openstack to the list assignable locations method.
 */
public class OpenstackComputeClientImpl extends JCloudsComputeClientImpl {

    private final NovaApi novaApi;

    @Inject public OpenstackComputeClientImpl(JCloudsViewFactory jCloudsViewFactory,
        ServiceConfiguration serviceConfiguration, NovaApi novaApi) {
        super(jCloudsViewFactory, serviceConfiguration);
        this.novaApi = novaApi;
    }

    @Override public Set<? extends Location> listAssignableLocations() {
        Set<Location> locations = new HashSet<>();

        for (Location location : super.listAssignableLocations()) {
            locations.add(location);
            if (location.getScope().equals(LocationScope.REGION)) {
                final Set<Location> availabilityZones =
                    new AvailabilityZoneSupplierForRegion(location).get();
                locations.addAll(availabilityZones);
                for (Location availabilityZone : availabilityZones) {
                    locations.addAll(new HostSupplierForAvailabilityZone(availabilityZone).get());
                }
            }
        }
        return locations;
    }

    private class AvailabilityZoneSupplierForRegion implements Supplier<Set<Location>> {

        private final Location region;

        private AvailabilityZoneSupplierForRegion(Location region) {
            checkArgument(region.getScope().equals(LocationScope.REGION));
            this.region = region;
        }

        @Override public Set<Location> get() {

            Set<AvailabilityZone> availabilityZones = new HashSet<>();
            if (novaApi.getAvailabilityZoneApi(region.getId()).isPresent()) {
                availabilityZones.addAll(availabilityZones =
                    novaApi.getAvailabilityZoneApi(region.getId()).get().list().toSet());
            }
            return availabilityZones.stream().map(
                availabilityZone -> new AvailabilityZoneToJCloudsLocationConverter()
                    .apply(availabilityZone)).collect(Collectors.toSet());
        }

        private class AvailabilityZoneToJCloudsLocationConverter
            implements OneWayConverter<AvailabilityZone, Location> {

            @Override public Location apply(AvailabilityZone availabilityZone) {
                //todo: do we need to check the zone state?
                return new LocationBuilder().scope(LocationScope.ZONE)
                    .id(RegionAndId.fromRegionAndId(region.getId(), availabilityZone.getName())
                        .slashEncode()).parent(region).description(availabilityZone.getName())
                    .build();
            }
        }
    }


    private class HostSupplierForAvailabilityZone implements Supplier<Set<Location>> {

        private final Location availabilityZone;

        private HostSupplierForAvailabilityZone(Location availabilityZone) {
            this.availabilityZone = availabilityZone;
        }

        @Override public Set<Location> get() {
            Set<Host> hosts = new HashSet<>();
            try {
                if (novaApi.getHostAdministrationApi(availabilityZone.getParent().getId())
                    .isPresent()) {
                    hosts.addAll(StreamSupport.stream(
                        novaApi.getHostAdministrationApi(availabilityZone.getParent().getId()).get()
                            .list().spliterator(), false)
                        .filter(host -> host.getService().equals("compute")).filter(
                            host -> host.getZone().equals(
                                RegionAndId.fromSlashEncoded(availabilityZone.getId()).getId()))
                        .collect(Collectors.toList()));
                }
            } catch (AuthorizationException ignored) {
                //if we are not allowed to retrieve the hosts, we ignore them
            }
            return hosts.stream().map(host -> new HostToJCloudsLocationConverter().apply(host))
                .collect(Collectors.toSet());
        }

        private class HostToJCloudsLocationConverter implements OneWayConverter<Host, Location> {

            @Override public Location apply(Host host) {
                return new LocationBuilder().scope(LocationScope.HOST).id(RegionAndId
                    .fromRegionAndId(availabilityZone.getParent().getId(), host.getName())
                    .slashEncode()).description(host.getName()).parent(availabilityZone).build();
            }
        }



    }
}
