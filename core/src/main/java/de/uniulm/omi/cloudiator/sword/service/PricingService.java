package de.uniulm.omi.cloudiator.sword.service;

import de.uniulm.omi.cloudiator.sword.domain.Pricing;

public interface PricingService {
    /**
     * Retrieves an {@link Iterable} of the pricing that the cloud provider uses.
     *
     * @return the pricing offered.
     */
    Iterable<Pricing> listPricing();
}
