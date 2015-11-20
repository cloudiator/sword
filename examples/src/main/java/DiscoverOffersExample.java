import de.uniulm.omi.cloudiator.sword.api.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.api.domain.Image;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.api.service.ComputeService;
import de.uniulm.omi.cloudiator.sword.api.service.DiscoveryService;

/**
 * An example showing the discovery service.
 */
public class DiscoverOffersExample {

    private final ComputeService<HardwareFlavor, Image, Location, VirtualMachine> computeService;

    public DiscoverOffersExample(
        ComputeService<HardwareFlavor, Image, Location, VirtualMachine> computeService) {
        this.computeService = computeService;
    }

    public void discover() {
        DiscoveryService<HardwareFlavor, Image, Location, VirtualMachine> discoveryService =
            computeService.discoveryService();

        //print hardware offers
        discoveryService.listHardwareFlavors().forEach(System.out::println);

        //print locations
        discoveryService.listLocations().forEach(System.out::println);

        //print images
        discoveryService.listImages().forEach(System.out::println);

        //prints the virtual machines in the node group managed by sword
        discoveryService.listVirtualMachines().forEach(System.out::println);
    }
}
