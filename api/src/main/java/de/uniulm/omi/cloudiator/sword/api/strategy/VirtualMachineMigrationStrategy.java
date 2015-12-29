package de.uniulm.omi.cloudiator.sword.api.strategy;

import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.api.exceptions.MigrationException;

/**
 * Created by daniel on 29.12.15.
 */
public interface VirtualMachineMigrationStrategy {

    enum MigrationType {
        /**
         * a live migration strategy, no
         * downtime off the migrated entity.
         */
        LIVE,
        /**
         * a offline migration strategy, meaning
         * that the entity will be down at least for
         * a short time during the migration.
         */
        OFFLINE
    }

    /**
     * Returns the type of this migration strategy
     *
     * @return the migration type.
     */
    MigrationType migrationType();

    void apply(VirtualMachine virtualMachine, Location to) throws MigrationException;


}
