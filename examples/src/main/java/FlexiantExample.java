import de.uniulm.omi.cloudiator.sword.domain.ApiBuilder;
import de.uniulm.omi.cloudiator.sword.domain.CloudBuilder;
import de.uniulm.omi.cloudiator.sword.domain.ConfigurationBuilder;
import de.uniulm.omi.cloudiator.sword.domain.CredentialsBuilder;
import de.uniulm.omi.cloudiator.sword.service.ComputeService;
import de.uniulm.omi.cloudiator.sword.service.ServiceBuilder;

/**
 * Example depicting the information needed
 * to build a compute service for the Flexiant Cloud Orchestrator.
 */
public class FlexiantExample {

  public static void main(String[] args) {

    /**
     * The login name used for FCO.
     */
    final String mail = "me@example.com";
    /**
     * The uuid of your user, can be retrieved via
     * user details.
     */
    final String uuid = "07a77df9-04f9-49ba-9112-c903f1661c50";

    /**
     * The api username. Concatenation of mail and uuid. Can
     * also be retrieved via user details.
     */
    final String apiUsername = mail + "/" + uuid;

    /**
     * Your FCO login password.
     */
    final String password = "MySecretPassword";
    /**
     * The api endpoint of your FCO installation.
     */
    final String endpoint = "https://endpoint.flexiant.net";
    /**
     * A string depicting your node group. Used to identify the machines
     * management by sword.
     */
    final String nodeGroup = "nodeGroup";

    /**
     * Builds the compute service
     */
    ComputeService flexiant = ServiceBuilder.newServiceBuilder().cloud(
        CloudBuilder.newBuilder().endpoint(endpoint)
            .configuration(ConfigurationBuilder.newBuilder().nodeGroup(nodeGroup).build())
            .credentials(
                CredentialsBuilder.newBuilder().user(apiUsername).password(password).build())
            .api(ApiBuilder.newBuilder().providerName("flexiant").build()).build()).build();
  }

}
