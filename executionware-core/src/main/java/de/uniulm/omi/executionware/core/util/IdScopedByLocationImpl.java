package de.uniulm.omi.executionware.core.util;

import de.uniulm.omi.executionware.api.util.IdScopedByLocation;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

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
