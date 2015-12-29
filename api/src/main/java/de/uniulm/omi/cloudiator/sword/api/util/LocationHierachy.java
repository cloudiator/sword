package de.uniulm.omi.cloudiator.sword.api.util;

import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.LocationScope;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 29.12.15.
 */
public class LocationHierachy {

    Location location;

    private LocationHierachy(Location location) {
        this.location = location;
    }

    public static LocationHierachy of(Location location) {
        checkNotNull(location);
        return new LocationHierachy(location);
    }

    public Optional<Location> findParentOfScope(LocationScope scope) {
        Optional<Location> toFind = Optional.of(location);
        while (toFind.isPresent()) {
            if (toFind.get().locationScope().equals(scope)) {
                return toFind;
            }
            toFind = toFind.get().parent();
        }
        return Optional.empty();
    }

}
