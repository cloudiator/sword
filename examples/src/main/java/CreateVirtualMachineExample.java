import de.uniulm.omi.cloudiator.sword.api.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.api.domain.Image;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.api.service.ComputeService;

import java.util.function.Supplier;

/**
 * An example showing the steps needed to create a virtual machine
 * using the compute service.
 */
public class CreateVirtualMachineExample implements Supplier<VirtualMachine> {

    private final ComputeService<HardwareFlavor, Image, Location, VirtualMachine> computeService;

    public CreateVirtualMachineExample(
        ComputeService<HardwareFlavor, Image, Location, VirtualMachine> computeService) {
        this.computeService = computeService;
    }

    @Override public VirtualMachine get() {
        //todo fix example
        return computeService.createVirtualMachine(null);
    }
}
