package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.strategy;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.uniulm.omi.cloudiator.sword.drivers.openstack4j.internal.Openstack4JConstants;
import javax.annotation.Nullable;

/**
 * Created by daniel on 21.11.16.
 */
public class OpenstackConfiguredNetworkStrategy implements OpenstackNetworkStrategy {


  @Inject(optional = true)
  @Named(Openstack4JConstants.DEFAULT_NETWORK)
  @Nullable
  private String
      defaultNetwork;

  @Nullable
  @Override
  public String get() {
    return defaultNetwork;
  }
}
