package de.uniulm.omi.cloudiator.sword.domain;

import de.uniulm.omi.cloudiator.domain.OperatingSystem;

import java.util.List;

public interface Pricing extends Resource {
    String getCurrency();

    String getTenancy();

    String getProductFamily();

    OperatingSystem getOperatingSystem();

    String getInstanceName();

    String getLicenseModel();

    String getPreInstalledSw();

    String getCapacityStatus();

    List<PricingTerms> getPricingOnDemandTerms();

    List<PricingTerms> getPricingReservedTerms();

    String getCloudServiceProviderName();

    String getLocationProviderId();

    String getOperation();

    Api getApi();
}
