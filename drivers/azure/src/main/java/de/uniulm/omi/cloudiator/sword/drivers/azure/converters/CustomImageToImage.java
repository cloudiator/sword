package de.uniulm.omi.cloudiator.sword.drivers.azure.converters;

import com.google.inject.Inject;
import com.microsoft.azure.management.compute.VirtualMachineCustomImage;
import com.microsoft.azure.management.resources.fluentcore.arm.Region;
import de.uniulm.omi.cloudiator.domain.OperatingSystems;
import de.uniulm.omi.cloudiator.sword.domain.Image;
import de.uniulm.omi.cloudiator.sword.domain.ImageBuilder;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import de.uniulm.omi.cloudiator.util.OneWayConverter;

public class CustomImageToImage implements OneWayConverter<VirtualMachineCustomImage, Image> {

  private final OneWayConverter<Region, Location> locationConverter;

  @Inject
  public CustomImageToImage(OneWayConverter<Region, Location> locationConverter) {
    this.locationConverter = locationConverter;
  }

  @Override
  public Image apply(VirtualMachineCustomImage virtualMachineCustomImage) {
    String name = virtualMachineCustomImage.name();
    Location loc = locationConverter.apply(virtualMachineCustomImage.region());
    return ImageBuilder.newBuilder()
        .id(virtualMachineCustomImage.id())
        .providerId(name)
        .name(name)
        .location(loc)
        .os(OperatingSystems.unknown())
        .build();
  }
}
