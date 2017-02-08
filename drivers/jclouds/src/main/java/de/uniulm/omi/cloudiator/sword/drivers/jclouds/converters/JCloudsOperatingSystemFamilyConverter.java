package de.uniulm.omi.cloudiator.sword.drivers.jclouds.converters;

import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.domain.OperatingSystemFamily;
import org.jclouds.compute.domain.OsFamily;

/**
 * Created by daniel on 10.03.16.
 */
public class JCloudsOperatingSystemFamilyConverter
    implements OneWayConverter<OsFamily, OperatingSystemFamily> {

    @Override public OperatingSystemFamily apply(OsFamily osFamily) {
        //todo: we probably need a map conversion here
        return OperatingSystemFamily.fromValue(osFamily.value());
    }
}
