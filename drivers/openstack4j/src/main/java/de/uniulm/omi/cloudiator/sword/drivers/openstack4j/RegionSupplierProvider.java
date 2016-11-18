package de.uniulm.omi.cloudiator.sword.drivers.openstack4j;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 18.11.16.
 */
public class RegionSupplierProvider implements Provider<RegionSupplier> {

    private final KeyStoneVersion keyStoneVersion;
    private final Injector injector;

    @Inject
    public RegionSupplierProvider(KeyStoneVersion keyStoneVersion, Injector injector) {
        checkNotNull(injector, "injector is null");
        this.injector = injector;
        checkNotNull(keyStoneVersion, "keyStoneVersion is null");
        this.keyStoneVersion = keyStoneVersion;
    }

    @Override
    public RegionSupplier get() {
        return injector.getInstance(keyStoneVersion.regionSupplierClass());
    }
}
