package de.uniulm.omi.cloudiator.sword.drivers.onestep.strategies;

import client.ApiException;
import client.api.ApiClient;
import client.api.InstancesApi;
import client.model.InstanceData;
import client.model.instances.EmptyResponse;
import de.uniulm.omi.cloudiator.sword.strategy.DeleteVirtualMachineStrategy;

import javax.inject.Inject;
import java.util.Objects;

public class OnestepDeleteVirtualMachineStrategy implements DeleteVirtualMachineStrategy {

    private final InstancesApi instancesApi;

    @Inject
    public OnestepDeleteVirtualMachineStrategy(InstancesApi instancesApi) {
        this.instancesApi = Objects.requireNonNull(instancesApi);
    }

    @Override
    public void apply(String s) {
        ApiClient apiClient = instancesApi.getApiClient();
        int id = Integer.parseInt(s);
        InstanceData instanceData = apiClient
                .getInstancesList()
                .stream()
                .filter(e -> e.getInstanceDetails().getId().equals(id))
                .findFirst()
                .get();
        try {
            EmptyResponse response = instancesApi.instancesDelete(Integer.parseInt(instanceData.getLocationId()), id);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
