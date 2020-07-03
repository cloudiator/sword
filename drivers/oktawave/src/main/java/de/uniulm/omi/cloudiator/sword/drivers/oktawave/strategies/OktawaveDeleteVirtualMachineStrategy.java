package de.uniulm.omi.cloudiator.sword.drivers.oktawave.strategies;

import com.oktawave.api.client.ApiException;
import com.oktawave.api.client.api.OciApi;
import com.oktawave.api.client.model.DictionaryItem;
import com.oktawave.api.client.model.Ticket;
import de.uniulm.omi.cloudiator.sword.strategy.DeleteVirtualMachineStrategy;

import javax.inject.Inject;
import java.util.Objects;

public class OktawaveDeleteVirtualMachineStrategy implements DeleteVirtualMachineStrategy {

    private final OciApi ociApi;

    @Inject
    public OktawaveDeleteVirtualMachineStrategy(OciApi ociApi) {
        this.ociApi = Objects.requireNonNull(ociApi);
    }

    @Override
    public void apply(String s) {

        try {
            Ticket ticket = ociApi.instancesDelete(null, true);
            DictionaryItem operationType = ticket.getOperationType();
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
