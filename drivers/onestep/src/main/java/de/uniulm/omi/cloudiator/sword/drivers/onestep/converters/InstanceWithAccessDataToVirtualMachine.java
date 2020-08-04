package de.uniulm.omi.cloudiator.sword.drivers.onestep.converters;

import client.model.instances.Instance;
import de.uniulm.omi.cloudiator.sword.domain.*;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.domain.AccessData;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.domain.InstanceWithAccessData;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.internal.HardwareFlavourNamingStrategy;
import de.uniulm.omi.cloudiator.sword.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.util.OneWayConverter;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Objects;

public class InstanceWithAccessDataToVirtualMachine implements OneWayConverter<InstanceWithAccessData, VirtualMachine> {

    private final GetStrategy<String, Image> imageGetStrategy;
    private final GetStrategy<String, Location> locationGetStrategy;
    private final GetStrategy<String, HardwareFlavor> hardwareGetStrategy;

    @Inject
    public InstanceWithAccessDataToVirtualMachine(GetStrategy<String, Image> imageGetStrategy,
                                                  GetStrategy<String, Location> locationGetStrategy,
                                                  GetStrategy<String, HardwareFlavor> hardwareGetStrategy
    ) {
        this.imageGetStrategy = Objects.requireNonNull(imageGetStrategy);
        this.locationGetStrategy = Objects.requireNonNull(locationGetStrategy);
        this.hardwareGetStrategy = Objects.requireNonNull(hardwareGetStrategy);
    }

    @Nullable
    @Override
    public VirtualMachine apply(@Nullable InstanceWithAccessData instanceWithAccessData) {

        Instance instance = instanceWithAccessData.getInstance();
        HardwareFlavor hardwareFlavor = hardwareGetStrategy.get(String.valueOf(HardwareFlavourNamingStrategy.get));
        Image image = imageGetStrategy.get(String.valueOf(instance.getTemplate().getId()));
        Location location = locationGetStrategy.get(String.valueOf(instance.getSubregion().getId()));

        AccessData accessData = instanceWithAccessData.getAccessData();
        LoginCredential loginCredential = accessData !=  null ? LoginCredentialBuilder.newBuilder()
//                .username(accessData.getUser())
                .username("root")
                .privateKey(accessData.getSshKey())
                .password(accessData.getPassword())
                .build() : null;

        return VirtualMachineBuilder
                .newBuilder()
                .id(String.valueOf(instance.getId()))
                .name(instance.getName())
                .hardware(hardwareFlavor)
                .image(image)
                .location(location)
                .providerId(hardwareFlavor.providerId())
                .addIpString(instance.getIpAddress())
                .loginCredential(loginCredential)
                .build();
    }

}
