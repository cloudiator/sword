package de.uniulm.omi.cloudiator.sword.drivers.oktawave.domain;

public class AccessData {

    private String user;
    private String password;
    private String sshKey;

    public AccessData(String user, String password, String sshKey) {
        this.user = user;
        this.password = password;
        this.sshKey = sshKey;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSshKey() {
        return sshKey;
    }

    public void setSshKey(String sshKey) {
        this.sshKey = sshKey;
    }
}
