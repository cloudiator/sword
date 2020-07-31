package de.uniulm.omi.cloudiator.sword.drivers.onestep.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HardwareFlavourNamingStrategy {

    public static String createHardwareFlavourNameFromClusterId(int clusterId, int hardwareFlavourNumber) {
        return Integer.toString(clusterId) + "-" + Integer.toString(hardwareFlavourNumber);
    }

    public static int getClusterIdFromHardwareFlavourName(String name) {
        Pattern p = Pattern.compile("-?(\\d)+");
        Matcher m = p.matcher(name);
        int result = 0;
        if (m.find()) {
            result =  Integer.parseInt(m.group());
        }

        return result;
    }
}
