import de.uniulm.omi.cloudiator.sword.api.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.api.domain.Image;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.api.service.ComputeService;
import de.uniulm.omi.cloudiator.sword.service.ServiceBuilder;

/**
 * Created by daniel on 20.11.15.
 */
public class FlexiantExample {

    public static void main(String[] args) {

        final String mail = "me@example.com";
        final String uuid = "07a77df9-04f9-49ba-9112-c903f1661c50";

        final String apiUsername = mail + "/" + uuid;

        final String password = "MySecretPassword";
        final String endpoint = "https://endpoint.flexiant.net";
        final String nodeGroup = "nodeGroup";

        ComputeService<HardwareFlavor, Image, Location, VirtualMachine> flexiant =
            ServiceBuilder.newServiceBuilder("flexiant").credentials(apiUsername, password)
                .endpoint(endpoint).nodeGroup(nodeGroup).build();



    }

}
