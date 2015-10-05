package de.uniulm.omi.cloudiator.sword.core.domain;

import de.uniulm.omi.cloudiator.sword.api.domain.Credentials;

/**
 * Created by daniel on 18.08.15.
 */
public class CredentialsBuilder {

    private String user;
    private String password;

    public static CredentialsBuilder newBuilder() {
        return new CredentialsBuilder();
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



}
