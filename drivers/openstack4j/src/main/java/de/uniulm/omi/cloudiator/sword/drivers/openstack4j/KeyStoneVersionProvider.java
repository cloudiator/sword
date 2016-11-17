package de.uniulm.omi.cloudiator.sword.drivers.openstack4j;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import de.uniulm.omi.cloudiator.sword.api.ServiceConfiguration;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Created by daniel on 17.11.16.
 */
public class KeyStoneVersionProvider implements Provider<KeyStoneVersion> {

    @Inject(optional = true)
    @Named(Openstack4JConstants.KEYSTONE_VERSION)
    private String
            keystoneVersionConfiguration;
    private final ServiceConfiguration serviceConfiguration;

    @Inject
    public KeyStoneVersionProvider(ServiceConfiguration serviceConfiguration) {
        checkNotNull(serviceConfiguration);
        this.serviceConfiguration = serviceConfiguration;
    }

    @Override
    public KeyStoneVersion get() {

        if (keystoneVersionConfiguration != null) {
            try {
                return KeyStoneVersion.valueOf(keystoneVersionConfiguration);
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException("Illegal configuration for " + Openstack4JConstants.KEYSTONE_VERSION);
            }
        }

        checkState(serviceConfiguration.getEndpoint().isPresent(), "Endpoint is mandatory.");
        return checkNotNull(KeyStoneVersion.fromEndpoint(serviceConfiguration.getEndpoint().get()), "Unable to resolve keyStone version from endpoint.");
    }
}
