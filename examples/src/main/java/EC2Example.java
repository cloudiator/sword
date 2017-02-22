import de.uniulm.omi.cloudiator.sword.service.ComputeService;
import de.uniulm.omi.cloudiator.domain.ApiBuilder;
import de.uniulm.omi.cloudiator.domain.CloudBuilder;
import de.uniulm.omi.cloudiator.domain.ConfigurationBuilder;
import de.uniulm.omi.cloudiator.domain.CredentialsBuilder;
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
    ComputeService ec2 =
        ServiceBuilder.newServiceBuilder().cloud(CloudBuilder.newBuilder().endpoint(null)
            .credentials(
                CredentialsBuilder.newBuilder().user(accessKeyId).password(secretAccessKey).build())
            .api(ApiBuilder.newBuilder().providerName("aws-ec2").build()).build())
            .configuration(ConfigurationBuilder.newBuilder().nodeGroup(nodeGroup).build()).build();

}
