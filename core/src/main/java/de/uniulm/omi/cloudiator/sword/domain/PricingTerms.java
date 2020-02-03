package de.uniulm.omi.cloudiator.sword.domain;

import java.util.List;

public class PricingTerms {
    private final List<PricingDimensions> pricingDimensions;
    private final String leaseContractLength;
    private final String offeringClass;
    private final String purchaseOption;

    public List<PricingDimensions> getPricingDimensions() {
        return pricingDimensions;
    }

    public String getLeaseContractLength() {
        return leaseContractLength;
    }

    public String getOfferingClass() {
        return offeringClass;
    }

    public String getPurchaseOption() {
        return purchaseOption;
    }

    public PricingTerms(List<PricingDimensions> pricingDimensions, String leaseContractLength, String offeringClass, String purchaseOption) {
        this.pricingDimensions = pricingDimensions;
        this.leaseContractLength = leaseContractLength;
        this.offeringClass = offeringClass;
        this.purchaseOption = purchaseOption;
    }
}
