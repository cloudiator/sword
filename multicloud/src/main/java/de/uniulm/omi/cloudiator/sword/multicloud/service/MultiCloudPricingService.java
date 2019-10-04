package de.uniulm.omi.cloudiator.sword.multicloud.service;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.domain.Cloud;
import de.uniulm.omi.cloudiator.sword.domain.Pricing;
import de.uniulm.omi.cloudiator.sword.multicloud.pricing.PricingSupplierFactory;
import de.uniulm.omi.cloudiator.sword.service.PricingService;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

public class MultiCloudPricingService implements PricingService {
    private final CloudRegistry cloudRegistry;
    @Inject
    private PricingSupplierFactory pricingSupplierFactory;

    @Inject
    public MultiCloudPricingService(CloudRegistry cloudRegistry) {
        this.cloudRegistry = checkNotNull(cloudRegistry, "cloudRegistry is null");
    }

    @Override
    public Iterable<Pricing> listPricing() {
        /*final ImmutableSet.Builder<Pricing> builder = ImmutableSet.builder();
        Optional<Cloud> awsCloud = cloudRegistry.list().stream().filter(cloud -> cloud.api().providerName().equals("aws-ec2")).findFirst();

        if(awsCloud.isPresent()) {
            Supplier<Set<Pricing>> awsPricingSupplier = pricingSupplierFactory.createAWSPricingSupplier(awsCloud.get().credential());
            builder.addAll(awsPricingSupplier.get());
        }
        return builder.build();*/
        final ImmutableSet.Builder<Pricing> builder = ImmutableSet.builder();
        cloudRegistry
                .list()
                .stream()
                .filter(cloud -> cloud.api().providerName().equals("aws-ec2"))
                .findFirst()
                .ifPresent(cloud -> builder.addAll(pricingSupplierFactory.createAWSPricingSupplier(cloud.credential()).get()));
        return builder.build();
    }
}
