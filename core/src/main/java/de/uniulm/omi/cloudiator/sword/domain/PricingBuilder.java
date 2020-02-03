package de.uniulm.omi.cloudiator.sword.domain;

import de.uniulm.omi.cloudiator.domain.OperatingSystem;

import javax.annotation.Nullable;
import java.util.List;

public class PricingBuilder {
    private String currency;
    private String tenancy;
    private OperatingSystem operatingSystem;
    private String instanceName;
    private String licenseModel;
    private List<PricingTerms> pricingOnDemandTerms;
    @Nullable
    private List<PricingTerms> pricingReservedTerms;
    private String cloudServiceProviderName;
    private String locationProviderId;
    private Api api;
    private String productFamily;
    private String preInstalledSw;
    private String capacityStatus;
    private String operation;

    private String id;
    private String providerId;
    private String name;
    @Nullable
    private Location location;

    private PricingBuilder(){

    }

    public static PricingBuilder newBuilder() {
        return new PricingBuilder();
    }

    public PricingBuilder currency(String currency) {
        this.currency = currency;
        return this;
    }
    public PricingBuilder tenancy(String tenancy) {
        this.tenancy = tenancy;
        return this;
    }
    public PricingBuilder operatingSystem(OperatingSystem operatingSystem) {
        this.operatingSystem = operatingSystem;
        return this;
    }
    public PricingBuilder onDemandTerms(List<PricingTerms> pricingOnDemandTerms) {
        this.pricingOnDemandTerms = pricingOnDemandTerms;
        return this;
    }
    public PricingBuilder reservedTerms(List<PricingTerms> pricingReservedTerms) {
        this.pricingReservedTerms = pricingReservedTerms;
        return this;
    }

    public PricingBuilder id(String id) {
        this.id = id;
        return this;
    }
    public PricingBuilder providerId(String providerId) {
        this.providerId = providerId;
        return this;
    }
    public PricingBuilder name(String name) {
        this.name = name;
        return this;
    }
    public PricingBuilder location(Location location) {
        this.location = location;
        return this;
    }
    public PricingBuilder instanceName(String instanceName) {
        this.instanceName = instanceName;
        return this;
    }

    public PricingBuilder licenseModel(String licenseModel) {
        this.licenseModel = licenseModel;
        return this;
    }

    public PricingBuilder cloudServiceProviderName(String cloudServiceProviderName) {
        this.cloudServiceProviderName = cloudServiceProviderName;
        return this;
    }

    public PricingBuilder locationProviderId(String locationProviderId) {
        this.locationProviderId = locationProviderId;
        return this;
    }

    public PricingBuilder api(Api api) {
        this.api = api;
        return this;
    }

    public PricingBuilder productFamily(String productFamily) {
        this.productFamily = productFamily;
        return this;
    }

    public PricingBuilder preInstalledSw(String preInstalledSw) {
        this.preInstalledSw = preInstalledSw;
        return this;
    }

    public PricingBuilder capacityStatus(String capacityStatus) {
        this.capacityStatus = capacityStatus;
        return this;
    }

    public PricingBuilder operation(String operation) {
        this.operation = operation;
        return this;
    }

    public Pricing build() {
        return new PricingImpl(id, providerId, name, location, currency, tenancy, operatingSystem, pricingOnDemandTerms, pricingReservedTerms, instanceName, licenseModel, cloudServiceProviderName, locationProviderId, api, productFamily, preInstalledSw, capacityStatus, operation);
    }
}
