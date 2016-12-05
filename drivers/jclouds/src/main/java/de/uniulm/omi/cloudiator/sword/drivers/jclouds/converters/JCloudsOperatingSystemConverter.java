package de.uniulm.omi.cloudiator.sword.drivers.jclouds.converters;

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.common.os.OperatingSystemArchitecture;
import de.uniulm.omi.cloudiator.common.os.OperatingSystemBuilder;
import de.uniulm.omi.cloudiator.common.os.OperatingSystemFamily;
import de.uniulm.omi.cloudiator.common.os.OperatingSystemVersion;
import org.jclouds.compute.domain.OperatingSystem;
import org.jclouds.compute.domain.OsFamily;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 10.03.16.
 */
public class JCloudsOperatingSystemConverter implements
    OneWayConverter<OperatingSystem, de.uniulm.omi.cloudiator.common.os.OperatingSystem> {

    private final OneWayConverter<OsFamily, OperatingSystemFamily> operatingSystemFamilyConverter;

    @Inject JCloudsOperatingSystemConverter(
        OneWayConverter<OsFamily, OperatingSystemFamily> operatingSystemFamilyConverter) {
        checkNotNull(operatingSystemFamilyConverter);
        this.operatingSystemFamilyConverter = operatingSystemFamilyConverter;
    }

    @Override public de.uniulm.omi.cloudiator.common.os.OperatingSystem apply(
        OperatingSystem operatingSystem) {

        OperatingSystemFamily operatingSystemFamily =
            operatingSystemFamilyConverter.apply(operatingSystem.getFamily());

        OperatingSystemVersion operatingSystemVersion;
        try {
            operatingSystemVersion = operatingSystemFamily.operatingSystemVersionFormat()
                .parse(operatingSystem.getVersion());
        } catch (IllegalArgumentException e) {
            operatingSystemVersion = OperatingSystemVersion.unknown();
        }

        return OperatingSystemBuilder.newBuilder().version(operatingSystemVersion).
            architecture(OperatingSystemArchitecture.UNKNOWN).family(operatingSystemFamily).build();
    }
}
