import de.uniulm.omi.cloudiator.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.domain.Image;
import de.uniulm.omi.cloudiator.domain.Location;
import de.uniulm.omi.cloudiator.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.api.service.ComputeService;

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

    @Override public VirtualMachine get() {
        //todo fix example
        return computeService.createVirtualMachine(null);
    }
}
