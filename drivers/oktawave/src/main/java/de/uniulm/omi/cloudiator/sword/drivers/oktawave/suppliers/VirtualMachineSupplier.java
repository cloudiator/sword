package de.uniulm.omi.cloudiator.sword.drivers.oktawave.suppliers;

import com.google.common.base.Supplier;
import com.oktawave.api.client.handler.ApiException;
import com.oktawave.api.client.handler.OciApi;
import com.oktawave.api.client.model.Instance;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.drivers.oktawave.domain.InstanceWithAccessData;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class VirtualMachineSupplier implements Supplier<Set<VirtualMachine>> {

    private static Logger LOGGER = LoggerFactory.getLogger(VirtualMachineSupplier.class);

    private final OciApi ociApi;
    private final OneWayConverter<InstanceWithAccessData, VirtualMachine> instanceConverter;

    @Inject
    public VirtualMachineSupplier(OciApi ociApi,
                                  OneWayConverter<InstanceWithAccessData, VirtualMachine> instanceConverter) {
        this.ociApi = Objects.requireNonNull(ociApi, "ociApi could not be null");
        this.instanceConverter = Objects.requireNonNull(instanceConverter, "instanceConverter could not be null");
    }

    @Override
    public Set<VirtualMachine> get() {

        Set<Instance> templates = new HashSet<>();

        try {
            templates = new HashSet<>(ociApi.instancesGet(null, true, null, null, null, null, null, null, null)
                    .getItems());
        } catch (ApiException e) {
            LOGGER.error("Could not get Instances from Oktawave", e);
        }

        return templates.stream()
                .map(instance -> new InstanceWithAccessData(instance, null))
                .map(instanceConverter)
                .collect(Collectors.toSet());
    }
}
