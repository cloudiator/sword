package de.uniulm.omi.cloudiator.sword.onestep.domain;

import client.model.template_response.Cluster;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ImageTemplate {
    private int operatingSystemId;
    private String operatingSystemName;
    private String versionName;
    private Cluster cluster;
}
