package de.uniulm.omi.cloudiator.sword.multicloud.pricing.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.pricing.AWSPricing;
import com.amazonaws.services.pricing.AWSPricingClientBuilder;
import com.amazonaws.services.pricing.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Supplier;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import de.uniulm.omi.cloudiator.domain.*;
import de.uniulm.omi.cloudiator.sword.domain.*;
import de.uniulm.omi.cloudiator.sword.multicloud.pricing.aws.converters.AWSPricingLocationConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

public class AWSPricingSupplier implements Supplier<Set<Pricing>> {

    private final AWSCredentialsProvider credentialsProvider;
    private final AWSPricing client;
    private final AWSPricingLocationConverter awsPricingLocationConverter = new AWSPricingLocationConverter();
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AWSPricingSupplier.class);
    private static final List<String> productFamiliesFilter = Arrays.asList("Compute Instance", "Compute Instance (bare metal)");
    private static final List<String> tenanciesFilter = Arrays.asList("Shared", "Dedicated");
    private static final List<String> capacityStatusesFilter = Arrays.asList("Used");
    private static final String operationsPatternFilter = "RunInstances";

    @Inject
    public AWSPricingSupplier(@Assisted CloudCredential cloudCredential) {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(cloudCredential.user(), cloudCredential.password());
        credentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
        client = AWSPricingClientBuilder.standard().withRegion("us-east-1").withCredentials(credentialsProvider).build();
        LOGGER.info("AWSPricing client (with region us-east-1) created");
    }

    private AWSPricingPack getRawAWSPricingPaginated(String nextToken) {
        GetProductsRequest getProductsRequest = new GetProductsRequest().withServiceCode("AmazonEC2");
        GetProductsResult getProductsResult;

        if (nextToken == null || nextToken.isEmpty()){
            getProductsResult = client.getProducts(getProductsRequest);
        }
        else {
            getProductsResult = client.getProducts(getProductsRequest.withNextToken(nextToken));
        }

        ObjectMapper mapper = new ObjectMapper();

        Set<AWSProductAndTerms> awsProductAndTermsSet = new HashSet<>(100);

        try {
            for(int i=0; i < getProductsResult.getPriceList().size(); i++) {
                awsProductAndTermsSet.add(mapper.readValue(getProductsResult.getPriceList().get(i), AWSProductAndTerms.class));
            }
        } catch (Exception e) {
            LOGGER.error("Exception caught while retrieving AWS Price List", e);
            e.printStackTrace();
        }
        return new AWSPricingPack(awsProductAndTermsSet, getProductsResult.getNextToken());
    }

    @Override
    public Set<Pricing> get() {
        String nextToken = null;
        AWSPricingPack awsPricingPack = null;
        Set<Pricing> pricings = new HashSet<>();

        int awsPricingPackCounter = 0;
        LOGGER.info("Starting fetching AWS Pricing Information");

        do {
            awsPricingPack = getRawAWSPricingPaginated(nextToken);

            /*if(!awsPricingPack.getAwsProductAndTerms().isEmpty()) {
                LOGGER.info(String.format("AWSPricingPack no: %d", ++awsPricingPackCounter));
            }
            else {
                LOGGER.info("AWSPricingPack is empty");
            }*/

            for(AWSProductAndTerms awsProductAndTerms : awsPricingPack.getAwsProductAndTerms()) {
                if(!productFamiliesFilter.contains(awsProductAndTerms.product.productFamily)
                        || !tenanciesFilter.contains(awsProductAndTerms.product.attributes.get("tenancy"))
                        || !capacityStatusesFilter.contains(awsProductAndTerms.product.attributes.get("capacitystatus"))
                        || !awsProductAndTerms.product.attributes.get("operation").startsWith(operationsPatternFilter)) {
                    continue;
                }

                OperatingSystemArchitecture operatingSystemArchitecture;
                switch (awsProductAndTerms.product.attributes.get("processorArchitecture")) {
                    case "64-bit": {
                        operatingSystemArchitecture = OperatingSystemArchitecture.AMD64;
                        break;
                    }
                    case "32-bit or 64-bit": {
                        operatingSystemArchitecture = OperatingSystemArchitecture.I368;
                        break;
                    }
                    default: {
                        operatingSystemArchitecture = OperatingSystemArchitecture.UNKNOWN;
                        break;
                    }
                }

                OperatingSystemFamily operatingSystemFamily;
                switch (awsProductAndTerms.product.attributes.get("operatingSystem")) {
                    case "Linux": {
                        operatingSystemFamily = OperatingSystemFamily.UBUNTU; // UBUNTU as a generalization of Linux OS distributions
                        break;
                    }
                    case "RHEL": {
                        operatingSystemFamily = OperatingSystemFamily.RHEL;
                        break;
                    }
                    case "SUSE": {
                        operatingSystemFamily = OperatingSystemFamily.SUSE;
                        break;
                    }
                    case "Windows": {
                        operatingSystemFamily = OperatingSystemFamily.WINDOWS;
                        break;
                    }
                    default: {
                        operatingSystemFamily = OperatingSystemFamily.UNKNOWN;
                        break;
                    }
                }

                OperatingSystemBuilder operatingSystemBuilder = OperatingSystemBuilder.newBuilder()
                        .architecture(operatingSystemArchitecture)
                        .family(operatingSystemFamily)
                        .version(OperatingSystemVersions.unknown()); // AWS Pricing API exposes no information about version

                List<PricingTerms> pricingOnDemandTerms = new ArrayList<>();
                List<PricingTerms> pricingReservedTerms = new ArrayList<>();
                Optional<String> currency = Optional.empty();

                if(awsProductAndTerms.terms != null && awsProductAndTerms.terms.get("OnDemand") != null) {
                    for (Map.Entry<String, AWSPriceDimensionsRates> entry : awsProductAndTerms.terms.get("OnDemand").entrySet()) {
                        for (Map.Entry<String, AWSDimensionRates> entry2 : entry.getValue().getAwsDimensionRates().entrySet()) {
                            List<PricingDimensions> pricingDimensions = new ArrayList<>();
                            for (Map.Entry<String, AWSBaseTerm> entry3 : entry2.getValue().entrySet()) {
                                Map.Entry<String, BigDecimal> price = entry3.getValue().getPricePerUnit().entrySet().iterator().next();
                                pricingDimensions.add(new PricingDimensions(price.getValue(), entry3.getValue().getUnit(), entry3.getValue().getDescription(), entry3.getValue().getBeginRange(), entry3.getValue().getEndRange()));
                            }
                            pricingOnDemandTerms.add(new PricingTerms(pricingDimensions, entry.getValue().termAttributes.get("LeaseContractLength"), entry.getValue().termAttributes.get("OfferingClass"), entry.getValue().termAttributes.get("PurchaseOption")));
                        }
                    }
                    currency = Optional.ofNullable(awsProductAndTerms.terms.get("OnDemand").entrySet().iterator().next().getValue().getAwsDimensionRates().entrySet().iterator().next().getValue().entrySet().iterator().next().getValue().getPricePerUnit().entrySet().iterator().next().getKey());
                }

                // currently only OnDemand offers are being presented
                /*if (awsProductAndTerms.terms != null && awsProductAndTerms.terms.get("Reserved") != null) {
                    for (Map.Entry<String, AWSPriceDimensionsRates> entry : awsProductAndTerms.terms.get("Reserved").entrySet()) {
                        for (Map.Entry<String, AWSDimensionRates> entry2 : entry.getValue().getAwsDimensionRates().entrySet()) {
                            List<PricingDimensions> pricingDimensions = new ArrayList<>();
                            for (Map.Entry<String, AWSBaseTerm> entry3 : entry2.getValue().entrySet()) {
                                Map.Entry<String, BigDecimal> price = entry3.getValue().getPricePerUnit().entrySet().iterator().next();
                                pricingDimensions.add(new PricingDimensions(price.getValue(), entry3.getValue().getUnit(), entry3.getValue().getDescription(), entry3.getValue().getBeginRange(), entry3.getValue().getEndRange()));
                            }
                            pricingReservedTerms.add(new PricingTerms(pricingDimensions, entry.getValue().termAttributes.get("LeaseContractLength"), entry.getValue().termAttributes.get("OfferingClass"), entry.getValue().termAttributes.get("PurchaseOption")));
                        }
                    }
                    if(!currency.isPresent()) {
                        currency = Optional.ofNullable(awsProductAndTerms.terms.get("Reserved").entrySet().iterator().next().getValue().getAwsDimensionRates().entrySet().iterator().next().getValue().entrySet().iterator().next().getValue().getPricePerUnit().entrySet().iterator().next().getKey());
                    }
                }*/

                Pricing pricing = PricingBuilder.newBuilder()
                        .id(awsProductAndTerms.product.sku)
                        .providerId(awsProductAndTerms.product.sku)
                        .name(awsProductAndTerms.product.attributes.get("usagetype"))
                        .location(null)
                        .locationProviderId(awsPricingLocationConverter.apply(awsProductAndTerms.product.attributes.get("location")))
                        .instanceName(awsProductAndTerms.product.attributes.get("instanceType"))
                        .cloudServiceProviderName("aws")
                        .api(ApiBuilder.newBuilder().providerName("aws-ec2").build())
                        .currency(currency.isPresent() ? currency.get() : "N/A")
                        .tenancy(awsProductAndTerms.product.attributes.get("tenancy"))
                        .operatingSystem(operatingSystemBuilder.build())
                        .onDemandTerms(pricingOnDemandTerms)
                        .reservedTerms(pricingReservedTerms)
                        .licenseModel(awsProductAndTerms.product.attributes.get("licenseModel"))
                        .capacityStatus(awsProductAndTerms.product.attributes.get("capacitystatus"))
                        .preInstalledSw(awsProductAndTerms.product.attributes.get("preInstalledSw"))
                        .productFamily(awsProductAndTerms.product.productFamily)
                        .operation(awsProductAndTerms.product.attributes.get("operation"))
                        .build();
                pricings.add(pricing);
            }

            nextToken = awsPricingPack.getNextToken();
        } while (nextToken != null);
        LOGGER.info(String.format("AWS products (with terms) processed (= size of returned Set<Pricing>): %d", pricings.size()));

        return pricings;
    }
}
