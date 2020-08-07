package de.uniulm.omi.cloudiator.sword.drivers.onestep.converters;

import client.model.instances
        .InstanceDetails;
import de.uniulm.omi.cloudiator.sword.domain.*;
import client.model.InstanceData;
import de.uniulm.omi.cloudiator.sword.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.util.OneWayConverter;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Objects;

public class InstanceDataToVirtualMachine implements OneWayConverter<InstanceData, VirtualMachine> {

    private final GetStrategy<String, Image> imageGetStrategy;
    private final GetStrategy<String, Location> locationGetStrategy;
    private final GetStrategy<String, HardwareFlavor> hardwareGetStrategy;

    @Inject
    public InstanceDataToVirtualMachine(GetStrategy<String, Image> imageGetStrategy,
                                        GetStrategy<String, Location> locationGetStrategy,
                                        GetStrategy<String, HardwareFlavor> hardwareGetStrategy
    ) {
        this.imageGetStrategy = Objects.requireNonNull(imageGetStrategy);
        this.locationGetStrategy = Objects.requireNonNull(locationGetStrategy);
        this.hardwareGetStrategy = Objects.requireNonNull(hardwareGetStrategy);
    }

    @Nullable
    @Override
    public VirtualMachine apply(@Nullable InstanceData instanceData) {

        HardwareFlavor hardwareFlavor = hardwareGetStrategy.get(instanceData.getHardwareFlavourId());
        Image image = imageGetStrategy.get(instanceData.getImageId());
        Location location = locationGetStrategy.get(instanceData.getLocationId());
        //ToDo if endpoint to get password would be provided, set username and password
        LoginCredential loginCredential = LoginCredentialBuilder.newBuilder()
                .username(null)
                .privateKey(instanceData.getPrivateKey())
                .password(null)
                .build();

        InstanceDetails instanceDetails = instanceData.getInstanceDetails();

        return VirtualMachineBuilder
                .newBuilder()
                .id(String.valueOf(instanceDetails.getId()))
                .name(instanceDetails.getName())
                .hardware(hardwareFlavor)
                .image(image)
                .location(location)
                .providerId(hardwareFlavor.providerId())
                .addIpString(instanceDetails.getPublicAddresses().get(0).getIpAddress())
                .loginCredential(loginCredential)
                .build();
    }

}
