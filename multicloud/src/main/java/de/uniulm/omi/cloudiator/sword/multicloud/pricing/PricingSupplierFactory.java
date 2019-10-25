package de.uniulm.omi.cloudiator.sword.multicloud.pricing;

import com.google.common.base.Supplier;
import com.google.inject.name.Named;
import de.uniulm.omi.cloudiator.sword.domain.CloudCredential;
import de.uniulm.omi.cloudiator.sword.domain.Pricing;

import java.util.Set;

public interface PricingSupplierFactory {
    String awsName = "aws";
    @Named(awsName) Supplier<Set<Pricing>> createAWSPricingSupplier(CloudCredential cloudCredential);
}
