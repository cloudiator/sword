package de.uniulm.omi.cloudiator.sword.drivers.onestep.suppliers;

import client.api.ApiClient;
import com.google.common.base.Supplier;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import client.model.InstanceData;
import de.uniulm.omi.cloudiator.util.OneWayConverter;

import javax.inject.Inject;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class VirtualMachineSupplier implements Supplier<Set<VirtualMachine>> {

    private final ApiClient apiClient;
    private final OneWayConverter<InstanceData, VirtualMachine> instanceConverter;

    @Inject
    public VirtualMachineSupplier(ApiClient apiClient, OneWayConverter<InstanceData, VirtualMachine> instanceConverter) {
        this.apiClient = Objects.requireNonNull(apiClient, "apiClient could not be null");
        this.instanceConverter = Objects.requireNonNull(instanceConverter, "instanceConverter could not be null");

    }

    @Override
    public Set<VirtualMachine> get() {

        return apiClient
                .getInstancesList()
                .stream()
                .map(instanceConverter)
                .collect(Collectors.toSet());
    }
}
