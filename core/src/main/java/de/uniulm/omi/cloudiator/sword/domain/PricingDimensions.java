package de.uniulm.omi.cloudiator.sword.domain;

import java.math.BigDecimal;

public class PricingDimensions {
    private final BigDecimal pricePerUnit;
    private final String unit;
    private final String description;
    private final String beginRange;
    private final String endRange;

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public String getUnit() {
        return unit;
    }

    public String getDescription() {
        return description;
    }

    public String getBeginRange() {
        return beginRange;
    }

    public String getEndRange() {
        return endRange;
    }

    public PricingDimensions(BigDecimal pricePerUnit, String unit, String description, String beginRange, String endRange) {
        this.pricePerUnit = pricePerUnit;
        this.unit = unit;
        this.description = description;
        this.beginRange = beginRange;
        this.endRange = endRange;
    }
}
