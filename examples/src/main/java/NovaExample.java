import de.uniulm.omi.cloudiator.sword.api.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.api.domain.Image;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.api.service.ComputeService;
import de.uniulm.omi.cloudiator.sword.service.ServiceBuilder;

/**
 * Created by daniel on 20.11.15.
 */
public class NovaExample {

    public static void main(String[] args) {

        final String tenant = "tenant";
        final String username = "me@example.com";

        final String apiUsername = tenant + ":" + username;

        final String password = "MySecretPassword";
        final String endpoint = "https://api.cloud.com/v2.0/";
        final String nodeGroup = "nodeGroup";

        ComputeService<HardwareFlavor, Image, Location, VirtualMachine> nova =
            ServiceBuilder.newServiceBuilder("openstack-nova").credentials(apiUsername, password)
                .endpoint(endpoint).nodeGroup(nodeGroup).build();

    }

}
