package de.uniulm.omi.cloudiator.sword.drivers.azure.strategies;

import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.compute.KnownLinuxVirtualMachineImage;
import com.microsoft.azure.management.compute.KnownWindowsVirtualMachineImage;
import com.microsoft.azure.management.compute.VirtualMachine;
import com.microsoft.azure.management.network.PublicIPAddress;
import de.uniulm.omi.cloudiator.domain.OperatingSystem;
import de.uniulm.omi.cloudiator.domain.OperatingSystemType;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachineTemplate;
import de.uniulm.omi.cloudiator.sword.strategy.NameSubstringBasedOperatingSystemDetectionStrategy;
import de.uniulm.omi.cloudiator.sword.strategy.OperatingSystemDetectionStrategy;

import java.util.Arrays;

public class AzureVirtualMachineBuilder {

  private static final OperatingSystemDetectionStrategy osDetection =
      new NameSubstringBasedOperatingSystemDetectionStrategy();

  private Azure azure;
  private VirtualMachineTemplate template;
  private OperatingSystem os;
  private String hardware;
  private String resourceGroup;
  private String username;
  private String password;

  private AzureVirtualMachineBuilder() {
  }

  public static AzureVirtualMachineBuilder newBuilder() {
    return new AzureVirtualMachineBuilder();
  }

  public AzureVirtualMachineBuilder setAzure(Azure azure) {
    this.azure = azure;
    return this;
  }

  public AzureVirtualMachineBuilder setTemplate(VirtualMachineTemplate template) {
    this.template = template;
    return this;
  }

  public AzureVirtualMachineBuilder setOs(OperatingSystem os) {
    this.os = os;
    return this;
  }

  public AzureVirtualMachineBuilder setHardware(String hardware) {
    this.hardware = hardware;
    return this;
  }

  public AzureVirtualMachineBuilder setResourceGroup(String resourceGroup) {
    this.resourceGroup = resourceGroup;
    return this;
  }

  public AzureVirtualMachineBuilder setUsername(String username) {
    this.username = username;
    return this;
  }

  public AzureVirtualMachineBuilder setPassword(String password) {
    this.password = password;
    return this;
  }

  public VirtualMachine build() {
    PublicIPAddress publicIPAddress = azure.publicIPAddresses()
        .define("IP")
        .withRegion(template.locationId())
        .withExistingResourceGroup(resourceGroup)
        .withDynamicIP()
        .withoutLeafDomainLabel()
        .create();

    VirtualMachine.DefinitionStages.WithOS commonVmConfig = azure
        .virtualMachines().define(template.name())
        .withRegion(template.locationId())
        .withExistingResourceGroup(resourceGroup)
        .withNewPrimaryNetwork("10.0.0.0/28").withPrimaryPrivateIPAddressDynamic()
        .withExistingPrimaryPublicIPAddress(publicIPAddress);

    // if ImageId is in KnownLinuxVirtualMachineImage
    if (Arrays.stream(KnownLinuxVirtualMachineImage.values())
        .anyMatch(value -> value.name().equals(template.imageId()))) {
      return createLinuxVm(commonVmConfig);
    }
    // if ImageId is in KnownWindowsVirtualMachineImage
    if (Arrays.stream(KnownWindowsVirtualMachineImage.values())
        .anyMatch(value -> value.name().equals(template.imageId()))) {
      return createWindowsVm(commonVmConfig);
    }
    // If it's not builtin image, then it's custom
    if (os.operatingSystemFamily().operatingSystemType().equals(OperatingSystemType.LINUX)) {
      return createCustomLinuxVm(commonVmConfig);
    }
    if (os.operatingSystemFamily().operatingSystemType().equals(OperatingSystemType.WINDOWS)) {
      return createCustomWindowsVm(commonVmConfig);
    }
    throw new IllegalStateException("Unable to create Virtual Machine with image " + template.imageId());
  }

  // There are four very similar functions because creation of VMs
  // is different for Windows/Linux os and custom/builtin image
  private VirtualMachine createLinuxVm(VirtualMachine.DefinitionStages.WithOS virtualMachineBuilder) {
    return virtualMachineBuilder
        .withPopularLinuxImage(KnownLinuxVirtualMachineImage.valueOf(template.imageId()))
        .withRootUsername(username)
        .withRootPassword(password)
        .withSize(hardware)
        .create();
  }

  private VirtualMachine createWindowsVm(VirtualMachine.DefinitionStages.WithOS virtualMachineBuilder) {
    return virtualMachineBuilder
        .withPopularWindowsImage(KnownWindowsVirtualMachineImage.valueOf(template.imageId()))
        .withAdminUsername(username)
        .withAdminPassword(password)
        .withSize(hardware)
        .create();
  }

  private VirtualMachine createCustomLinuxVm(VirtualMachine.DefinitionStages.WithOS virtualMachineBuilder) {
    return virtualMachineBuilder
        .withLinuxCustomImage(template.imageId())
        .withRootUsername(username)
        .withRootPassword(password)
        .withSize(hardware)
        .create();
  }

  private VirtualMachine createCustomWindowsVm(VirtualMachine.DefinitionStages.WithOS virtualMachineBuilder) {
    return virtualMachineBuilder
        .withWindowsCustomImage(template.imageId())
        .withAdminUsername(username)
        .withAdminPassword(password)
        .withSize(hardware)
        .create();
  }

}
