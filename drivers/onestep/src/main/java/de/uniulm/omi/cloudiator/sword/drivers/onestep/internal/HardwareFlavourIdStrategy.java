package de.uniulm.omi.cloudiator.sword.drivers.onestep.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/** In Onestep, cluster has various possibilities of setting hardware (i.e. disc, ram, cores). In consequence
 *  We need to create unique id for hardware flavour for each combination of disc/ram/cores. During wecreating
 *  virtual machines we need to be able to cast those ids to one of few clusterIds recognisable by OneStep
 */
public class HardwareFlavourIdStrategy {

    public static String createHardwareFlavourIdromClusterId(int clusterId, int hardwareFlavourNumber) {
        return clusterId + "-" + hardwareFlavourNumber;
    }

    public static int getClusterIdFromHardwareFlavourId(String name) {
        Pattern p = Pattern.compile("-?(\\d)+");
        Matcher m = p.matcher(name);
        int result = 0;
        if (m.find()) {
            result =  Integer.parseInt(m.group());
        }

        return result;
    }
}
