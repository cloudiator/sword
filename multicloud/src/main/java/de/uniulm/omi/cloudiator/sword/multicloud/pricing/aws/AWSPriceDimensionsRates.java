package de.uniulm.omi.cloudiator.sword.multicloud.pricing.aws;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AWSPriceDimensionsRates {

    @JsonIgnore
    private Map<String, AWSDimensionRates> awsDimensionRates = new HashMap<String, AWSDimensionRates>();

    @JsonAnyGetter
    public Map<String, AWSDimensionRates> getAwsDimensionRates() {
        return this.awsDimensionRates;
    }

    @JsonAnySetter
    public void setAwsDimensionRates(String name, AWSDimensionRates value) {
        this.awsDimensionRates.put(name, value);
    }

    public String sku;
    public String effectiveDate;
    public String offerTermCode;
    public Map<String, String> termAttributes; // Object
}