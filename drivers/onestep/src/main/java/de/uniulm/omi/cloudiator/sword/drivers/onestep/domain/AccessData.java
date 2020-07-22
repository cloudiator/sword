package de.uniulm.omi.cloudiator.sword.drivers.onestep.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccessData {

    private String user;
    private String password;
    private String sshKey;

}
