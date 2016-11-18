package de.uniulm.omi.cloudiator.sword.drivers.openstack4j;

import org.openstack4j.api.OSClient;
import org.openstack4j.model.identity.v3.Region;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Created by daniel on 18.11.16.
 */
public class OsClientV3RegionSupplier implements RegionSupplier {

    private final OSClient.OSClientV3 osClientV3;

    private OsClientV3RegionSupplier(OSClient osClient) {
        checkNotNull(osClient, "osClient is null");
        checkState(osClient instanceof OSClient.OSClientV3,
            "Illegal version of OSClient supplied.");
        this.osClientV3 = (OSClient.OSClientV3) osClient;
    }

    @Override public Set<String> get() {
        return osClientV3.identity().regions().list().stream().map(new Function<Region, String>() {
            @Override public String apply(Region region) {
                return region.getId();
            }
        }).collect(Collectors.toSet());
    }
}
