package de.uniulm.omi.cloudiator.sword.drivers.onestep.converters;

import de.uniulm.omi.cloudiator.domain.*;
import de.uniulm.omi.cloudiator.sword.domain.Image;
import de.uniulm.omi.cloudiator.sword.domain.ImageBuilder;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.domain.ImageTemplate;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageTemplateToImage implements OneWayConverter<ImageTemplate, Image> {

    private static Logger LOGGER = LoggerFactory.getLogger(ImageTemplateToImage.class);

    @Override
    public Image apply(ImageTemplate template) {

        String name = template.getOperatingSystemName();
        return ImageBuilder.newBuilder()
                .id(String.valueOf(template.getOperatingSystemId()))
                .providerId(name)
                .name(name)
                .os(buildOs(template))
                .build();
    }

    private de.uniulm.omi.cloudiator.domain.OperatingSystem buildOs(ImageTemplate template) {
        return OperatingSystemBuilder.newBuilder()
                .architecture(OperatingSystemArchitecture.UNKNOWN)
                .family(buildFamily(template.getOperatingSystemName()))
                .version(buildVersion(template.getVersionName()))
                .build();
    }

    //ToDO Change SUSE to OpenSuse?
    private OperatingSystemFamily buildFamily(String osName) {
        for (OperatingSystemFamily value : OperatingSystemFamily.values()) {
            if (value.name().equalsIgnoreCase(osName)) {
                return OperatingSystemFamily.fromValue(osName.toUpperCase());
            }
        }
        return OperatingSystemFamily.UNKNOWN;
    }

    private OperatingSystemVersion buildVersion(String versionName) {
        int versionNo = getVersionNo(versionName);
        String versionSubName = getVersionSubName(versionName);

        try {
            return OperatingSystemVersions.ofNameAndVersion(versionNo, versionSubName);
        } catch (NumberFormatException nfe) {
            LOGGER.warn("Could not parse version: " + versionNo + " from template: " + versionName + ". OperatingSystemVersions.unknown will be returned");
            return OperatingSystemVersions.unknown();
        } catch (Exception e) {
            LOGGER.warn("Exception, OperatingSystemVersions.unknown will be returned " + e.getMessage());
            return OperatingSystemVersions.unknown();
        }
    }

    private int getVersionNo(String version) {
        Pattern p = Pattern.compile("-?(\\d+\\.*)+");
        Matcher m = p.matcher(version);
        int result = 0;
        if (m.find()) {
            result =  Integer.parseInt((m.group()).replace(".", ""));
        }

        return result;
    }

    private String getVersionSubName(String version) {
        return version.replaceAll("-?(\\d+\\.*)+", " ");
    }

}
