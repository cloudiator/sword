package de.uniulm.omi.cloudiator.sword.drivers.jclouds.converters;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.domain.OperatingSystemArchitecture;
import de.uniulm.omi.cloudiator.domain.OperatingSystemBuilder;
import de.uniulm.omi.cloudiator.domain.OperatingSystemFamily;
import de.uniulm.omi.cloudiator.domain.OperatingSystemVersion;
import de.uniulm.omi.cloudiator.domain.OperatingSystemVersions;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import org.jclouds.compute.domain.OperatingSystem;
import org.jclouds.compute.domain.OsFamily;

/**
 * Created by daniel on 10.03.16.
 */
public class JCloudsOperatingSystemConverter
    implements OneWayConverter<OperatingSystem, de.uniulm.omi.cloudiator.domain.OperatingSystem> {

  private final OneWayConverter<OsFamily, OperatingSystemFamily> operatingSystemFamilyConverter;

  @Inject
  JCloudsOperatingSystemConverter(
      OneWayConverter<OsFamily, OperatingSystemFamily> operatingSystemFamilyConverter) {
    checkNotNull(operatingSystemFamilyConverter);
    this.operatingSystemFamilyConverter = operatingSystemFamilyConverter;
  }

  @Override
  public de.uniulm.omi.cloudiator.domain.OperatingSystem apply(OperatingSystem operatingSystem) {

    OperatingSystemFamily operatingSystemFamily =
        operatingSystemFamilyConverter.apply(operatingSystem.getFamily());

    OperatingSystemVersion operatingSystemVersion;
    try {
      operatingSystemVersion = operatingSystemFamily.operatingSystemVersionFormat()
          .parse(operatingSystem.getVersion());
    } catch (IllegalArgumentException e) {
      operatingSystemVersion = OperatingSystemVersions.unknown();
    }

    return OperatingSystemBuilder.newBuilder().version(operatingSystemVersion).
        architecture(OperatingSystemArchitecture.UNKNOWN).family(operatingSystemFamily).build();
  }
}
