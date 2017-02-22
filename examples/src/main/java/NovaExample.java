import de.uniulm.omi.cloudiator.sword.service.ComputeService;
import de.uniulm.omi.cloudiator.domain.ApiBuilder;
import de.uniulm.omi.cloudiator.domain.CloudBuilder;
import de.uniulm.omi.cloudiator.domain.ConfigurationBuilder;
import de.uniulm.omi.cloudiator.domain.CredentialsBuilder;
import de.uniulm.omi.cloudiator.sword.service.ServiceBuilder;

/**
 * Example depicting the information needed
 * to build a compute service for the Openstack Nova Compute API.
 */
public class NovaExample {

    public static void main(String[] args) {

        /**
         * The tenant you want to use.
         */
        final String tenant = "tenant";
        /**
         * Your Openstack username.
         */
        final String username = "me@example.com";

        /**
         * Constructs the api username by concatenating the tenant
         * and the username.
         */
        final String apiUsername = tenant + ":" + username;

        /**
         * The password used for login.
         */
        final String password = "MySecretPassword";
        /**
         * The endpoint of your Openstack installation, listed under Endpoints -> Keystone.
         */
        final String endpoint = "https://api.cloud.com/v2.0/";

        /**
         * A string depicting your node group. Used to identify the machines
         * management by sword.
         */
        final String nodeGroup = "nodeGroup";

        /**
         * Builds the compute service.
         */
        ComputeService nova =
            ServiceBuilder.newServiceBuilder().cloud(CloudBuilder.newBuilder().endpoint(endpoint)
                .credentials(
                    CredentialsBuilder.newBuilder().user(apiUsername).password(password).build())
                .api(ApiBuilder.newBuilder().providerName("openstack-nova").build()).build())
                .configuration(ConfigurationBuilder.newBuilder().nodeGroup(nodeGroup).build())
                .build();

    }

}
