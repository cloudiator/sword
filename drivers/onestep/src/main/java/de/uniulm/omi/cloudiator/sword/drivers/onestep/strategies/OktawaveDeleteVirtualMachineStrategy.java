package de.uniulm.omi.cloudiator.sword.drivers.onestep.strategies;

import de.uniulm.omi.cloudiator.sword.strategy.DeleteVirtualMachineStrategy;

import javax.inject.Inject;

public class OktawaveDeleteVirtualMachineStrategy implements DeleteVirtualMachineStrategy {

    //private final OciApi ociApi;

    @Inject
    public OktawaveDeleteVirtualMachineStrategy() {
      //  this.ociApi = Objects.requireNonNull(ociApi);
    }

    @Override
    public void apply(String s) {
/*
        try {
            Ticket ticket = ociApi.instancesDelete(null, true);
            DictionaryItem operationType = ticket.getOperationType();
        } catch (ApiException e) {
            e.printStackTrace();
        }*/
    }
}
