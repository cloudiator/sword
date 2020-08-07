package client.model;

import client.model.instances.InstanceDetails;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class InstanceData {

    private InstanceDetails instanceDetails;
    private String hardwareFlavourId;
    private String locationId;
    private String imageId;
    private String privateKey;

}
