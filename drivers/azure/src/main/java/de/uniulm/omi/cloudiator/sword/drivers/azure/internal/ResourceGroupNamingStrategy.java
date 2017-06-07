package de.uniulm.omi.cloudiator.sword.drivers.azure.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import de.uniulm.omi.cloudiator.sword.domain.Cloud;
import java.util.function.Function;
import javax.inject.Inject;

/**
 * Created by daniel on 07.06.17.
 */
public class ResourceGroupNamingStrategy implements Function<String, String> {

  private final String nodeGroup;

  @Inject
  public ResourceGroupNamingStrategy(Cloud cloud) {
    checkNotNull(cloud, "cloud is null");
    nodeGroup = cloud.configuration().nodeGroup();
  }

  @Override
  public String apply(String region) {
    return region + nodeGroup;
  }
}
