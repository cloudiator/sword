package de.uniulm.omi.cloudiator.sword.drivers.onestep.domain;

import client.model.regions.Region;
import client.model.templates.Cluster;
import de.uniulm.omi.cloudiator.sword.domain.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ImageTemplate {
    private int operatingSystemId;
    private String operatingSystemName;
    private int versionId;
    private String versionName;
    private Location location;
    private Cluster cluster;
}
