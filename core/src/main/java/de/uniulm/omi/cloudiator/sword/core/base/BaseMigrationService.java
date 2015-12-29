package de.uniulm.omi.cloudiator.sword.core.base;

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.api.exceptions.MigrationException;
import de.uniulm.omi.cloudiator.sword.api.extensions.MigrationService;
import de.uniulm.omi.cloudiator.sword.api.strategy.VirtualMachineMigrationStrategy;

import java.util.Set;

/**
 * Created by daniel on 29.12.15.
 */
public class BaseMigrationService implements MigrationService {

    private final Set<VirtualMachineMigrationStrategy> virtualMachineMigrationStrategies;

    @Inject public BaseMigrationService(
        Set<VirtualMachineMigrationStrategy> virtualMachineMigrationStrategies) {
        this.virtualMachineMigrationStrategies = virtualMachineMigrationStrategies;
    }

    @Override public void migrate(VirtualMachine virtualMachine, Location to)
        throws MigrationException {
        for (VirtualMachineMigrationStrategy virtualMachineMigrationStrategy : virtualMachineMigrationStrategies) {
            try {
                virtualMachineMigrationStrategy.apply(virtualMachine, to);
                break;
            } catch (MigrationException ignored) {
            }
        }
        throw new MigrationException("Tried all available migration strategies.");
    }
}
