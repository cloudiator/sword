package de.uniulm.omi.cloudiator.sword.drivers.onestep.domain;

import client.model.templates.Cluster;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ImageTemplate {
    private int operatingSystemId;
    private String operatingSystemName;
    private int versionId;
    private String versionName;
    private int RegionId;
    private Cluster cluster;
}
