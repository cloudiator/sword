package de.uniulm.omi.cloudiator.sword.drivers.azure.strategies;

import static com.google.common.base.Preconditions.checkNotNull;

import com.microsoft.azure.management.Azure;
import de.uniulm.omi.cloudiator.sword.drivers.azure.internal.ResourceGroupNamingStrategy;
import de.uniulm.omi.cloudiator.sword.strategy.DeleteVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.util.IdScopeByLocations;
import de.uniulm.omi.cloudiator.sword.util.IdScopedByLocation;
import javax.inject.Inject;

/**
 * Created by daniel on 07.06.17.
 */
public class AzureDeleteVirtualMachineStrategy implements DeleteVirtualMachineStrategy {

  private final Azure azure;
  private final ResourceGroupNamingStrategy resourceGroupNamingStrategy;

  @Inject
  public AzureDeleteVirtualMachineStrategy(Azure azure,
      ResourceGroupNamingStrategy resourceGroupNamingStrategy) {

    checkNotNull(resourceGroupNamingStrategy, "resourceGroupNamingStrategy is null");
    this.resourceGroupNamingStrategy = resourceGroupNamingStrategy;
    
    checkNotNull(azure, "azure is null");
    this.azure = azure;
  }

  @Override
  public void apply(String id) {

    checkNotNull(id, "id is null");

    IdScopedByLocation idScopedByLocation = IdScopeByLocations.from(id);
    final String resourceGroup = resourceGroupNamingStrategy
        .apply(idScopedByLocation.getLocationId());

    azure.virtualMachines().powerOff(resourceGroup, idScopedByLocation.getId());

    azure.virtualMachines().deleteByResourceGroup(resourceGroup, idScopedByLocation.getId());
  }
}
