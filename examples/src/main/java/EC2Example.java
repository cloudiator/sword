import de.uniulm.omi.cloudiator.sword.api.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.api.domain.Image;
import de.uniulm.omi.cloudiator.sword.api.domain.Location;
import de.uniulm.omi.cloudiator.sword.api.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.api.service.ComputeService;
import de.uniulm.omi.cloudiator.sword.service.ServiceBuilder;

/**
 * Example depicting the information needed
 * to build an Amazon Web Services - EC2 compute service.
 */
public class EC2Example {

    /**
     * The access key of your AWS user.
     */
    final String accessKeyId = "AMAZONACCESSKEYID";
    /**
     * The secret of your AWS user.
     */
    final String secretAccessKey = "SecretAccessKey";
    /**
     * A string depicting your node group. Used to identify the machines
     * management by sword.
     */
    final String nodeGroup = "nodeGroup";

    /**
     * Builds the compute service.
     */
    ComputeService<HardwareFlavor, Image, Location, VirtualMachine> nova =
        ServiceBuilder.newServiceBuilder("aws-ec2").credentials(accessKeyId, secretAccessKey)
            .nodeGroup(nodeGroup).build();

}