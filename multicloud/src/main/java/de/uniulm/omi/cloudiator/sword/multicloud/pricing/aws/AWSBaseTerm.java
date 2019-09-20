package de.uniulm.omi.cloudiator.sword.multicloud.pricing.aws;

import com.fasterxml.jackson.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AWSBaseTerm {
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("unit")
    public String getUnit() {
        return unit;
    }

    @JsonProperty("unit")
    public void setUnit(String unit) {
        this.unit = unit;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("beginRange")
    public String getBeginRange() {
        return beginRange;
    }

    @JsonProperty("beginRange")
    public void setBeginRange(String beginRange) {
        this.beginRange = beginRange;
    }

    @JsonProperty("endRange")
    public String getEndRange() {
        return endRange;
    }

    @JsonProperty("endRange")
    public void setEndRange(String endRange) {
        this.endRange = endRange;
    }

    @JsonProperty("pricePerUnit")
    public Map<String, BigDecimal> getPricePerUnit() {
        return pricePerUnit;
    }

    @JsonProperty("pricePerUnit")
    public void setPricePerUnit(Map<String, BigDecimal> pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    @JsonProperty("unit")
    private String unit;
    @JsonProperty("description")
    private String description;
    @JsonProperty("beginRange")
    private String beginRange;
    @JsonProperty("endRange")
    private String endRange;
    @JsonProperty("pricePerUnit")
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
