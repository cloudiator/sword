package de.uniulm.omi.cloudiator.sword.onestep.domain;

import com.oktawave.api.client.model.Instance;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InstanceWithAccessData {

    private Instance instance;
    private AccessData accessData;

}
