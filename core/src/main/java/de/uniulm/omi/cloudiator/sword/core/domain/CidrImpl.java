package de.uniulm.omi.cloudiator.sword.core.domain;

import de.uniulm.omi.cloudiator.sword.api.domain.Cidr;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 04.07.16.
 */
public class CidrImpl implements Cidr {


    public static final Cidr ALL = new CidrImpl("0.0.0.0", 0);

    private final String address;
    private final int slash;

    private CidrImpl(String address, int slash) {
        validateAddress(address);
        this.address = address;

        checkArgument(slash >= 0 && slash <= 32);
        this.slash = slash;
    }

    public static Cidr of(String address, int slash) {
        return new CidrImpl(address, slash);
    }

    public static Cidr of(String cidr) {
        checkArgument(cidr.contains("/"),
            String.format("Expected cidr to contain a slash (/) but %s contains none.", cidr));
        String[] split = cidr.split("/");
        checkArgument(split.length == 2,
            String.format("Expected cidr to contain one address and one slash, but got %s.", cidr));
        try {
            int slash = Integer.parseInt(split[1]);
            return new CidrImpl(split[0], slash);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String
                .format("Expected second part (slash) to be parsable to integer but %s is not.",
                    split[1]));
        }
    }

    private void validateAddress(String address) {
        checkNotNull(address);
        checkArgument(!address.isEmpty());

        String[] split = address.split("\\.");
        checkArgument(split.length == 4, String
            .format("Expected address to consist of four octets, but %s has only %s octet(s)",
                address, split.length));
        for (String part : split) {
            try {
                Integer integer = Integer.valueOf(part);
                checkArgument(integer >= 0 && integer <= 255, String
                    .format("Octet needs to be between 0 and 255, one octet was %s.", integer));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Address contains illegal octet " + part, e);
            }
        }
    }


    @Override public String address() {
        return address;
    }

    @Override public int slash() {
        return slash;
    }

    @Override public String toString() {
        return address + "/" + slash;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        CidrImpl cidr = (CidrImpl) o;

        if (slash != cidr.slash)
            return false;
        return address.equals(cidr.address);
    }

    @Override public int hashCode() {
        int result = address.hashCode();
        result = 31 * result + slash;
        return result;
    }
}
