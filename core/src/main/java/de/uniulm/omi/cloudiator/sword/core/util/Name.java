package de.uniulm.omi.cloudiator.sword.core.util;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by daniel on 03.03.16.
 */
public class Name {

    private static final Random random = new Random();
    @Nullable private final String name;

    private Name(@Nullable String name) {
        this.name = name;
    }

    public static Name of(@Nullable String name) {
        return new Name(name);
    }

    public static Name of() {
        return new Name(null);
    }

    @Nullable public String name() {
        return name;
    }

    public String uniqueName() {
        return generateUniqueName(name);
    }

    private static String generateUniqueName(@Nullable String name) {
        String uniquePrefix = String.valueOf(Name.random.nextInt(999));
        if (name != null) {
            return uniquePrefix + "-" + name;
        }
        return uniquePrefix;
    }


}
