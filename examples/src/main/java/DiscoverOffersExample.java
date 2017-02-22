import de.uniulm.omi.cloudiator.sword.service.ComputeService;
import de.uniulm.omi.cloudiator.sword.service.DiscoveryService;

/**
 * An example showing the discovery service.
 */
public class DiscoverOffersExample {

    private final ComputeService computeService;

    public DiscoverOffersExample(
        ComputeService computeService) {
        this.computeService = computeService;
    }

    public void discover() {
        DiscoveryService discoveryService =
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
