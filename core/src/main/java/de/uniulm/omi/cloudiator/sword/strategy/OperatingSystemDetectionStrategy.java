package de.uniulm.omi.cloudiator.sword.strategy;

import de.uniulm.omi.cloudiator.domain.OperatingSystem;
import de.uniulm.omi.cloudiator.sword.domain.Image;

/**
 * Created by daniel on 20.04.17.
 */
public interface OperatingSystemDetectionStrategy {

  OperatingSystem detectOperatingSystem(Image image);

}
