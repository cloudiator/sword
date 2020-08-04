package de.uniulm.omi.cloudiator.sword.drivers.onestep.domain;

import client.model.regions.Region;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Getter
public class ActiveRegionsSet {
    private Set<Region> regions;
}
