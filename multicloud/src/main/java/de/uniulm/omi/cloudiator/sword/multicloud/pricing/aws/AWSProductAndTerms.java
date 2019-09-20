package de.uniulm.omi.cloudiator.sword.multicloud.pricing.aws;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AWSProductAndTerms {
    public AWSProduct product;
    public AWSTerms terms;
    public String version;
}
