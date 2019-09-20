package de.uniulm.omi.cloudiator.sword.multicloud.pricing.aws.converters;

import de.uniulm.omi.cloudiator.util.OneWayConverter;

import javax.annotation.Nullable;

public class AWSPricingLocationConverter implements OneWayConverter<String, String> {
    public AWSPricingLocationConverter() {
    }

    @Nullable
    @Override
    public String apply(@Nullable String awsPricingLocationName) {
        if (awsPricingLocationName == null) {
            return null;
        }

        switch (awsPricingLocationName) {
            case "EU (Frankfurt)": {
                return "eu-central-1";
            }
            case "EU (Ireland)": {
                return "eu-west-1";
            }
            case "EU (London)": {
                return "eu-west-2";
            }
            case "EU (Paris)": {
                return "eu-west-3";
            }
            case "EU (Stockholm)": {
                return "eu-north-1";
            }
            case "US East (N. Virginia)": {
                return "us-east-1";
            }
            case "US East (Ohio)": {
                return "us-east-2";
            }
            case "US West (N. California)": {
                return "us-west-1";
            }
            case "US West (Oregon)": {
                return "us-west-2";
            }
            case "Canada (Central)": {
                return "ca-central-1";
            }
            case "South America (Sao Paulo)": {
                return "sa-east-1";
            }
            case "Asia Pacific (Hong Kong)": {
                return "ap-east-1";
            }
            case "Asia Pacific (Mumbai)": {
                return "ap-south-1";
            }
            case "Asia Pacific (Osaka-Local)": {
                return "ap-northeast-3";
            }
            case "Asia Pacific (Seoul)": {
                return "ap-northeast-2";
            }
            case "Asia Pacific (Singapore)": {
                return "ap-southeast-1";
            }
            case "Asia Pacific (Sydney)": {
                return "ap-southeast-2";
            }
            case "Asia Pacific (Tokyo)": {
                return "ap-northeast-1";
            }
            case "AWS GovCloud (US)": {
                return "us-gov-west-1";
            }
            case "AWS GovCloud (US-West)": {
                return "us-gov-west-1";
            }
            case "AWS GovCloud (US-East)": {
                return "us-gov-east-1";
            }
            case "Middle East (Bahrain)": {
                return "me-south-1";
            }
            default: {
                return awsPricingLocationName;
            }
        }
    }
}
