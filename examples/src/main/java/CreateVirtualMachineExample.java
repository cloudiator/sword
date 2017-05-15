import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.service.ComputeService;
import java.util.function.Supplier;

/**
 * An example showing the steps needed to create a virtual machine
 * using the compute service.
 */
public class CreateVirtualMachineExample implements Supplier<VirtualMachine> {

  private final ComputeService computeService;

  public CreateVirtualMachineExample(
      ComputeService computeService) {
    this.computeService = computeService;
  }

  @Override
  public VirtualMachine get() {
    //todo fix example
    return computeService.createVirtualMachine(null);
  }
}
