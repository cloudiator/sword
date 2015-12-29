package de.uniulm.omi.cloudiator.sword.api.extensions;

import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.api.exceptions.MigrationException;

/**
 * Created by daniel on 29.12.15.
 */
public interface MigrationService {

    void migrate(VirtualMachine virtualMachine, Location to) throws MigrationException;

}
