package de.uniulm.omi.cloudiator.sword.core.domain;

import com.google.common.base.MoreObjects;
import de.uniulm.omi.cloudiator.sword.api.domain.Credentials;

import javax.annotation.Nullable;

/**
 * Created by daniel on 18.08.15.
 */
public class CredentialsBuilder {

    @Nullable private String user;
    @Nullable private String password;

    private CredentialsBuilder() {

    }

    private CredentialsBuilder(Credentials credentials) {
        this.user = credentials.user();
        this.password = credentials.password();
    }

    public static CredentialsBuilder newBuilder() {
        return new CredentialsBuilder();
    }

    public static CredentialsBuilder of(Credentials credentials) {
        return new CredentialsBuilder(credentials);
    }


    public CredentialsBuilder user(String user) {
        this.user = user;
        return this;
    }

    public CredentialsBuilder password(String password) {
        this.password = password;
        return this;
    }

    public Credentials build() {
        return new CredentialsImpl(user, password);
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this).toString();
    }


}
