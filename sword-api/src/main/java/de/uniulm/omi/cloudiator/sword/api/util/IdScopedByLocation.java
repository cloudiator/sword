package de.uniulm.omi.cloudiator.sword.api.util;

/**
 * Created by daniel on 28.01.15.
 */
public interface IdScopedByLocation {

    String getId();

    String getLocationId();

    String getIdWithLocation();

}
