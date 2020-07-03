package de.uniulm.omi.cloudiator.sword.drivers.oktawave.converters;

import com.oktawave.api.client.model.DictionaryItem;
import com.oktawave.api.client.model.Software;
import com.oktawave.api.client.model.Template;
import de.uniulm.omi.cloudiator.domain.*;
import de.uniulm.omi.cloudiator.sword.domain.Image;
import de.uniulm.omi.cloudiator.sword.domain.ImageBuilder;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TemplateToImage implements OneWayConverter<Template, Image> {

    private static Logger LOGGER = LoggerFactory.getLogger(TemplateToImage.class);

    @Override
    public Image apply(Template template) {

        String name = template.getName();
        return ImageBuilder.newBuilder()
                .id(String.valueOf(template.getId()))
                .providerId(name)
                .name(name)
                .os(buildOs(template))
                .build();
    }

    private OperatingSystem buildOs(Template template) {
        return OperatingSystemBuilder.newBuilder()
                .architecture(buildIs64bit(template.getSoftware()))
                .family(buildFamily(template.getSystemCategory()))
                .version(buildVersion(template))
                .build();
    }

    private OperatingSystemArchitecture buildIs64bit(List<Software> software) {
        if (software != null && !software.isEmpty()) {
            String name = software.get(0).getName();
            if (name != null && name.contains("64-bit")) {
                return OperatingSystemArchitecture.AMD64;
            }
        }
        return OperatingSystemArchitecture.UNKNOWN;
    }

    private OperatingSystemFamily buildFamily(DictionaryItem systemCategory) {
        for (OperatingSystemFamily value : OperatingSystemFamily.values()) {
            if (value.name().equalsIgnoreCase(systemCategory.getLabel())) {
                return OperatingSystemFamily.fromValue(systemCategory.getLabel().toUpperCase());
            }
        }
        return OperatingSystemFamily.UNKNOWN;
    }

    private OperatingSystemVersion buildVersion(Template template) {
        try {
            return OperatingSystemVersions.ofNameAndVersion(getVersion(template.getVersion()), template.getName());
        } catch (NumberFormatException nfe) {
            LOGGER.warn("Could not parse version: " + template.getVersion() + " from template: " + template.getName() + ". OperatingSystemVersions.unknown will be returned");
            return OperatingSystemVersions.unknown();
        } catch (Exception e) {
            LOGGER.warn("Exception, OperatingSystemVersions.unknown will be returned " + e.getMessage());
            return OperatingSystemVersions.unknown();
        }
    }

    private Integer getVersion(String version) {
        return Integer.valueOf(version.replace(".", ""));
    }

}
