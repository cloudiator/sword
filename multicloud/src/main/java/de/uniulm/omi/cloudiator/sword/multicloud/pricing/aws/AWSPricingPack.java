package de.uniulm.omi.cloudiator.sword.multicloud.pricing.aws;

import java.util.Set;

public final class AWSPricingPack {
    private Set<AWSProductAndTerms> awsProductAndTerms;
    private String nextToken;

    public AWSPricingPack(Set<AWSProductAndTerms> awsProductAndTerms, String nextToken) {
        this.awsProductAndTerms = awsProductAndTerms;
        this.nextToken = nextToken;
    }

    public Set<AWSProductAndTerms> getAwsProductAndTerms() {
        return awsProductAndTerms;
    }

    public void setAwsProductAndTerms(Set<AWSProductAndTerms> awsProductAndTerms) {
        this.awsProductAndTerms = awsProductAndTerms;
    }

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }
}
