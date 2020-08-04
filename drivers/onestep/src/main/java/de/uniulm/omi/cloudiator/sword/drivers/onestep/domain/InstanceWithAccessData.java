package de.uniulm.omi.cloudiator.sword.drivers.onestep.domain;

import client.model.instances.Instance;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InstanceWithAccessData {

    private Instance instance;
    private AccessData accessData;

}
