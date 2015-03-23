package de.uniulm.omi.cloudiator.sword.api.util;

/**
 * Created by daniel on 28.01.15.
 */
public interface IdScopedByLocation {

    public String getId();

    public String getLocationId();

    public String getIdWithLocation();

}
