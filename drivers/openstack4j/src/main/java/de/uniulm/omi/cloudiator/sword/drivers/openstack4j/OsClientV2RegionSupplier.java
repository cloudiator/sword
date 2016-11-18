package de.uniulm.omi.cloudiator.sword.drivers.openstack4j;

import com.google.inject.Inject;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.types.ServiceType;
import org.openstack4j.model.identity.v2.Access;
import org.openstack4j.model.identity.v2.Endpoint;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Created by daniel on 18.11.16.
 */
public class OsClientV2RegionSupplier implements RegionSupplier {

    private final OSClient.OSClientV2 osClientV2;

    @Inject public OsClientV2RegionSupplier(OSClient osClient) {
        checkNotNull(osClient, "osClient is null");
        checkState(osClient instanceof OSClient.OSClientV2,
            "Illegal version of OSClient supplied.");
        this.osClientV2 = (OSClient.OSClientV2) osClient;
    }

    /**
     * @todo returns all regions based on compute service. Probably needs to be refined per service type.
     */
    private static class AccessToRegionSet implements Function<Access, Set<String>> {
        @Override public Set<String> apply(Access access) {
            return access.getServiceCatalog().stream().filter(
                (Predicate<Access.Service>) service -> service.getServiceType()
                    .equals(ServiceType.COMPUTE)).flatMap(
                (Function<Access.Service, Stream<? extends Endpoint>>) service -> service
                    .getEndpoints().stream()).map(Endpoint::getRegion).collect(Collectors.toSet());
        }
    }

    @Override public Set<String> get() {
        return new AccessToRegionSet().apply(osClientV2.getAccess());
    }
}
