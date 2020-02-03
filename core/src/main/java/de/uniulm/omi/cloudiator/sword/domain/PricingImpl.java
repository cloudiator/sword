package de.uniulm.omi.cloudiator.sword.domain;

import com.google.common.base.MoreObjects;
import de.uniulm.omi.cloudiator.domain.OperatingSystem;

import javax.annotation.Nullable;
import java.util.List;

public class PricingImpl extends ResourceImpl implements Pricing {
    private final String currency;
    private final String tenancy;
    private final OperatingSystem operatingSystem;
    private final List<PricingTerms> pricingOnDemandTerms;
    private final List<PricingTerms> pricingReservedTerms;
    private final String instanceName;
    private final String licenseModel;
    private final String cloudServiceProviderName;
    private final String locationProviderId;
    private final Api api;
    private String productFamily;
    private String preInstalledSw;
    private String capacityStatus;
    private String operation;

    public PricingImpl(String id, String providerId, String name, @Nullable Location location, String currency, String tenancy,
                       OperatingSystem operatingSystem, List<PricingTerms> pricingOnDemandTerms, @Nullable List<PricingTerms> pricingReservedTerms,
                       String instanceName, String licenseModel, String cloudServiceProviderName, String locationProviderId, Api api,
                       String productFamily, String preInstalledSw, String capacityStatus, String operation) {
        super(id, providerId, name, location);
        this.currency = currency;
        this.tenancy = tenancy;
        this.operatingSystem = operatingSystem;
        this.pricingOnDemandTerms = pricingOnDemandTerms;
        this.pricingReservedTerms = pricingReservedTerms;
        this.instanceName = instanceName;
        this.licenseModel = licenseModel;
        this.cloudServiceProviderName = cloudServiceProviderName;
        this.locationProviderId = locationProviderId;
        this.api = api;
        this.productFamily = productFamily;
        this.preInstalledSw = preInstalledSw;
        this.capacityStatus = capacityStatus;
        this.operation = operation;
    }

    @Override
    public String getCurrency() {
        return currency;
    }

    @Override
    public String getTenancy() {
        return tenancy;
    }

    @Override
    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    @Override
    public List<PricingTerms> getPricingOnDemandTerms() {
        return pricingOnDemandTerms;
    }

    @Override
    public List<PricingTerms> getPricingReservedTerms() {
        return pricingReservedTerms;
    }

    @Override
    public String getCloudServiceProviderName() {
        return cloudServiceProviderName;
    }

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    @Override
    public String getLicenseModel() {
        return licenseModel;
    }

    @Override
    public String getLocationProviderId() {
        return locationProviderId;
    }

    @Override
    public Api getApi() {
        return api;
    }

    @Override
    public String getProductFamily() {
        return productFamily;
    }

    @Override
    public String getPreInstalledSw() {
        return preInstalledSw;
    }

    @Override
    public String getCapacityStatus() {
        return capacityStatus;
    }

    @Override
    public String getOperation() {
        return operation;
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("instanceName", this.instanceName)
                .add("cloudServiceProviderName", this.cloudServiceProviderName)
                .add("locationProviderId", this.locationProviderId)
                .add("api", this.api.providerName())
                .add("currency", this.currency)
                .add("tenancy", this.tenancy)
                .add("licenseModel", this.licenseModel)
                .add("operatingSystem", this.operatingSystem)
                .add("pricingOnDemandTerms.size", (this.pricingOnDemandTerms != null ? this.pricingOnDemandTerms.size() : -1))
                .add("pricingReservedTerms.size", (this.pricingReservedTerms != null ? this.pricingReservedTerms.size() : -1))
                .add("productFamily", this.productFamily)
                .add("operation", this.operation)
                .add("preInstalledSw", this.preInstalledSw)
                .add("capacityStatus", this.capacityStatus);
    }

    @Override
    public String toString() {
        return toStringHelper().toString();
    }
}
