package de.uniulm.omi.cloudiator.sword.drivers.profitbricks.strategy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.uniulm.omi.cloudiator.sword.api.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.api.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachineBuilder;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachineTemplate;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by daniel on 23.11.16.
 */
public class Ec2CreateVirtualMachineStrategyTest {

  private CreateVirtualMachineStrategy delegateMock;
  private GetStrategy<String, VirtualMachine> getStrategyMock;
  private VirtualMachineTemplate vmtMock = mock(VirtualMachineTemplate.class);
  private VirtualMachine withIp;
  private VirtualMachine withoutIp;
  private CreateVirtualMachineStrategy createVirtualMachineStrategy;


  @Before
  public void before() {
    delegateMock = mock(CreateVirtualMachineStrategy.class);
    withoutIp =
        VirtualMachineBuilder.newBuilder().id("testId").location(null).loginCredential(null)
            .name("testName").providerId("testProviderId").build();
    withIp = VirtualMachineBuilder.of(withoutIp).addPublicIpAddress("141.5.6.23").build();
    getStrategyMock = mock(GetStrategy.class);
    when(getStrategyMock.get("testId")).thenReturn(withIp);
    createVirtualMachineStrategy =
        new Ec2CreateVirtualMachineStrategy(delegateMock, getStrategyMock);
  }

  @Test
  public void testVirtualMachineInitiallyHasIp() {
    when(delegateMock.apply(any())).thenReturn(withIp);
    VirtualMachine actual = createVirtualMachineStrategy.apply(vmtMock);
    assertThat(actual.publicAddresses(), contains("141.5.6.23"));
  }

  @Test
  public void testVirtualMachineHasNoIp() {
    when(delegateMock.apply(any())).thenReturn(withoutIp);
    VirtualMachine actual = createVirtualMachineStrategy.apply(vmtMock);
    assertThat(actual.publicAddresses(), contains("141.5.6.23"));
  }


}
