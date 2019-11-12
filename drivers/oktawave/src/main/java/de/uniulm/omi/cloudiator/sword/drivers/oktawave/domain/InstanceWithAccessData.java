package de.uniulm.omi.cloudiator.sword.drivers.oktawave.domain;

import com.oktawave.api.client.model.Instance;

public class InstanceWithAccessData {

    private Instance instance;
    private AccessData accessData;

    public InstanceWithAccessData(Instance instance, AccessData accessData) {
        this.instance = instance;
        this.accessData = accessData;
    }

    public Instance getInstance() {
        return instance;
    }

    public AccessData getAccessData() {
        return accessData;
    }



}
