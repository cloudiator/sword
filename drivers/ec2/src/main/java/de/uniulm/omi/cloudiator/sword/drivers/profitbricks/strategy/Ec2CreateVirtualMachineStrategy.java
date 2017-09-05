package de.uniulm.omi.cloudiator.sword.drivers.profitbricks.strategy;

import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import de.uniulm.omi.cloudiator.sword.domain.IpAddress.IpAddressType;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachineBuilder;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachineTemplate;
import de.uniulm.omi.cloudiator.sword.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.strategy.GetStrategy;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by daniel on 23.11.16.
 */
public class Ec2CreateVirtualMachineStrategy implements CreateVirtualMachineStrategy {

  private final CreateVirtualMachineStrategy delegate;
  private final GetStrategy<String, VirtualMachine> virtualMachineGetStrategy;

  public Ec2CreateVirtualMachineStrategy(CreateVirtualMachineStrategy delegate,
      GetStrategy<String, VirtualMachine> virtualMachineGetStrategy) {
    this.delegate = delegate;
    this.virtualMachineGetStrategy = virtualMachineGetStrategy;
  }

  @Override
  public VirtualMachine apply(VirtualMachineTemplate vmt) {
    VirtualMachine virtualMachine = delegate.apply(vmt);

    //sometimes virtual machines on amazon do not have a public ip at startup.
    //we have to wait until we know it and then add it

    //return early if ip is present
    if (virtualMachine.ipAddresses().stream().anyMatch(ipAddress -> ipAddress.type().equals(
        IpAddressType.PUBLIC))) {
      return virtualMachine;
    }

    Retryer<VirtualMachine> retryer = RetryerBuilder.<VirtualMachine>newBuilder()
        .retryIfResult(input -> input.ipAddresses().stream()
            .anyMatch(ipAddress -> ipAddress.type().equals(IpAddressType.PUBLIC)))
        .withStopStrategy(StopStrategies.stopAfterDelay(2, TimeUnit.MINUTES))
        .withWaitStrategy(WaitStrategies.fixedWait(10, TimeUnit.SECONDS)).build();

    try {
      VirtualMachine call =
          retryer.call(() -> virtualMachineGetStrategy.get(virtualMachine.id()));
      return VirtualMachineBuilder.of(virtualMachine).addIpAddressess(call.ipAddresses()).build();
    } catch (ExecutionException e) {
      throw new RuntimeException(e.getCause());
    } catch (RetryException e) {
      //todo do we want to return the initial virtual machine here? probably yes
      return virtualMachine;
    }

  }
}
