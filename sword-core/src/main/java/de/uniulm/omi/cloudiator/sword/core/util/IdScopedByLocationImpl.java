package de.uniulm.omi.cloudiator.sword.core.util;

import de.uniulm.omi.cloudiator.sword.api.util.IdScopedByLocation;

/**
 * Created by daniel on 28.01.15.
 */
public class IdScopedByLocationImpl implements IdScopedByLocation {

    private final String locationId;
    private final String id;
    static final String DELIMITER = "/";

    IdScopedByLocationImpl(String locationId, String id) {
        this.locationId = locationId;
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getLocationId() {
        return this.locationId;
    }

    @Override
    public String getIdWithLocation() {
        return this.getLocationId() + DELIMITER + this.getId();
    }
}
