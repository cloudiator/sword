package de.uniulm.omi.cloudiator.sword.multicloud.pricing.aws;

import com.fasterxml.jackson.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AWSBaseTerm {
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBeginRange() {
        return beginRange;
    }

    public void setBeginRange(String beginRange) {
        this.beginRange = beginRange;
    }

    public String getEndRange() {
        return endRange;
    }

    public void setEndRange(String endRange) {
        this.endRange = endRange;
    }

    public Map<String, BigDecimal> getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(Map<String, BigDecimal> pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    private String unit;
    private String description;
    private String beginRange;
    private String endRange;
    private Map<String, BigDecimal> pricePerUnit;

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @JsonCreator
    public AWSBaseTerm(@JsonProperty("unit") String unit, @JsonProperty("description") String description, @JsonProperty("beginRange") String beginRange,
                       @JsonProperty("endRange") String endRange, @JsonProperty("pricePerUnit") Map<String, BigDecimal> pricePerUnit) {
        this.unit = unit;
        this.beginRange = beginRange;
        this.description = description;
        this.endRange = endRange;
        this.pricePerUnit = pricePerUnit;
    }
}
