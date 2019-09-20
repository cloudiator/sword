package de.uniulm.omi.cloudiator.sword.multicloud.pricing.aws;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AWSTermRates extends HashMap<String, AWSPriceDimensionsRates> {}