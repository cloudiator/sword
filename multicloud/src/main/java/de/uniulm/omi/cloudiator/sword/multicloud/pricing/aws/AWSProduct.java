package de.uniulm.omi.cloudiator.sword.multicloud.pricing.aws;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AWSProduct {
    public String sku;
    public String productFamily;
    public Map<String, String> attributes;
}
