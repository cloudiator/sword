package de.uniulm.omi.cloudiator.sword.drivers.openstack4j.strategy;

import java.util.function.Supplier;
import javax.annotation.Nullable;

/**
 * Created by daniel on 21.11.16.
 */
public interface OpenstackNetworkStrategy extends Supplier<String> {

  @Nullable
  @Override
  String get();
}
