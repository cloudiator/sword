package de.uniulm.omi.executionware.drivers.jclouds.converters;

import de.uniulm.omi.executionware.api.converters.OneWayConverter;
import de.uniulm.omi.executionware.api.domain.LoginCredential;
import de.uniulm.omi.executionware.core.domain.builders.LoginCredentialBuilder;
import org.jclouds.domain.LoginCredentials;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 27.01.15.
 */
public class JCloudsLoginCredentialsToLoginCredential implements OneWayConverter<LoginCredentials,LoginCredential> {

    @Override
    public LoginCredential apply(LoginCredentials loginCredentials) {

        checkNotNull(loginCredentials);

        final LoginCredentialBuilder loginCredentialBuilder = LoginCredentialBuilder.newBuilder();
        loginCredentialBuilder.username(loginCredentials.getUser());
        if(loginCredentials.getOptionalPassword().isPresent()) {
            loginCredentialBuilder.password(loginCredentials.getOptionalPassword().get());
        }
        if(loginCredentials.getOptionalPrivateKey().isPresent()) {
            loginCredentialBuilder.privateKey(loginCredentials.getOptionalPrivateKey().get());
        }

        return loginCredentialBuilder.build();
    }
}
