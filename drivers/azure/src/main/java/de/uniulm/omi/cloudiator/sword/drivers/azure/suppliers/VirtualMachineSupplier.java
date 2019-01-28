package de.uniulm.omi.cloudiator.sword.drivers.azure.suppliers;

import com.google.common.base.Supplier;
import com.google.inject.Inject;
import com.microsoft.azure.management.Azure;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.util.OneWayConverter;

import java.util.Set;
import java.util.stream.Collectors;

public class VirtualMachineSupplier implements Supplier<Set<VirtualMachine>> {

  private final Azure azure;
  private final OneWayConverter<com.microsoft.azure.management.compute.VirtualMachine, VirtualMachine> virtualMachineConverter;


  @Inject
  VirtualMachineSupplier(Azure azure,
      OneWayConverter<com.microsoft.azure.management.compute.VirtualMachine, VirtualMachine> virtualMachineConverter) {
    this.azure = azure;
    this.virtualMachineConverter = virtualMachineConverter;
  }

  @Override
  public Set<VirtualMachine> get() {
    return azure.virtualMachines().list().stream()
        .map(virtualMachineConverter)
        .collect(Collectors.toSet());
  }
}
