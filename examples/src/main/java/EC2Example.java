import de.uniulm.omi.cloudiator.sword.api.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.api.domain.Image;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.api.service.ComputeService;
import de.uniulm.omi.cloudiator.sword.service.ServiceBuilder;

/**
 * Created by daniel on 20.11.15.
 */
public class EC2Example {

    final String accessKeyId = "AMAZONACCESSKEYID";
    final String secretAccessKey = "SecretAccessKey";
    final String nodeGroup = "nodeGroup";

    ComputeService<HardwareFlavor, Image, Location, VirtualMachine> nova =
        ServiceBuilder.newServiceBuilder("aws-ec2").credentials(accessKeyId, secretAccessKey)
            .nodeGroup(nodeGroup).build();

}
