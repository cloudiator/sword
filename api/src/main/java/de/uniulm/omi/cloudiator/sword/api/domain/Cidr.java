package de.uniulm.omi.cloudiator.sword.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by daniel on 04.07.16.
 */
public interface Cidr {

    /**
     * Network address in dotted form (e.g. 0.0.0.0)
     *
     * @return the network address
     */
    @JsonProperty
    String address();

    /**
     * The slash value
     *
     * @return the value behind the slash
     */
    @JsonProperty
    int slash();

}
