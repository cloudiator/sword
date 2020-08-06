package client.model;

import client.model.instances.InstanceDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InstanceData {

    private InstanceDetails instanceDetails;
    private String hardwareFlavourId;
    private String locationId;
    private String imageId;
    private String privateKey;

}
