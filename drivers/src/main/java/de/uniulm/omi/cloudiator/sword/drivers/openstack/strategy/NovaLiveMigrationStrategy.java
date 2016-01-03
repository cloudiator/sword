package de.uniulm.omi.cloudiator.sword.drivers.openstack.strategy;

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.LocationScope;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.api.exceptions.MigrationException;
import de.uniulm.omi.cloudiator.sword.api.strategy.VirtualMachineMigrationStrategy;
import de.uniulm.omi.cloudiator.sword.api.util.LocationHierachy;
import org.jclouds.http.HttpResponseException;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.regionscoped.RegionAndId;
import org.jclouds.openstack.nova.v2_0.extensions.ServerAdminApi;

import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A {@link VirtualMachineMigrationStrategy} using Nova's live migration capabilities.
 *
 * @link http://docs.openstack.org/admin-guide-cloud/compute-configuring-migrations.html
 */
public class NovaLiveMigrationStrategy implements VirtualMachineMigrationStrategy {

    private final NovaApi novaApi;

    @Inject public NovaLiveMigrationStrategy(NovaApi novaApi) {
        checkNotNull(novaApi);
        this.novaApi = novaApi;
    }

    @Override public MigrationType migrationType() {
        return MigrationType.LIVE;
    }

    @Override public void apply(VirtualMachine virtualMachine, Location to)
        throws MigrationException {

        checkNotNull(virtualMachine, "VirtualMachine must not be null.");
        checkNotNull(to, "To must not be null.");

        //no lambda, see https://bugs.openjdk.java.net/browse/JDK-8066974
        Location from = virtualMachine.location().orElseThrow(new Supplier<RuntimeException>() {
            @Override public RuntimeException get() {
                throw new IllegalStateException(
                    String.format("Expected location to be available for %s", virtualMachine));
            }
        });

        if (!(from.locationScope().equals(LocationScope.HOST) && to.locationScope()
            .equals(LocationScope.HOST))) {
            throw new MigrationException(String.format(
                "%s is only supported if both location have %s scope, however from has scope %s and to has scope %s",
                this, LocationScope.HOST, from.locationScope(), to.locationScope()));
        }

        //check for the same region
        //no lambda, see https://bugs.openjdk.java.net/browse/JDK-8066974
        Location fromRegion = LocationHierachy.of(from).findParentOfScope(LocationScope.REGION)
            .orElseThrow(new Supplier<RuntimeException>() {
                @Override public RuntimeException get() {
                    throw new IllegalStateException(String.format(
                        "Expected from %s to have a parent of scope region, but none was found.",
                        from));
                }
            });
        //no lambda, see https://bugs.openjdk.java.net/browse/JDK-8066974
        Location toRegion = LocationHierachy.of(to).findParentOfScope(LocationScope.REGION)
            .orElseThrow(new Supplier<RuntimeException>() {
                @Override public RuntimeException get() {
                    throw new IllegalStateException(String.format(
                        "Expected to %s to have a parent of scope region, but none was found.",
                        to));
                }
            });

        if (!fromRegion.equals(toRegion)) {
            throw new MigrationException(String.format(
                "%s is only supported if both from and to reside in the same region, however %s is in region %s and to %s is in region %s",
                this, from, fromRegion, to, toRegion));
        }

        //retrieve the server admin api
        ServerAdminApi serverAdminApi = novaApi.getServerAdminApi(fromRegion.id()).orNull();

        if (serverAdminApi == null) {
            throw new MigrationException(
                String.format("%s requires the nova server admin api to be present", this));
        }

        //todo: find a way to derive if we need to use block-migrate, currently defaulting to true
        try {
            serverAdminApi.liveMigrate(RegionAndId.fromSlashEncoded(virtualMachine.id()).getId(),
                RegionAndId.fromSlashEncoded(to.id()).getId(), true, true);
        } catch (HttpResponseException e) {
            throw new MigrationException("Migration failed.", e);
        }
    }
}
