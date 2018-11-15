/*
 * Copyright (c) 2014-2018 University of Ulm
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.uniulm.omi.cloudiator.sword.drivers.azure.strategies;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.inject.Inject;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.compute.KnownLinuxVirtualMachineImage;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.LoginCredentialBuilder;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachineBuilder;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachineTemplate;
import de.uniulm.omi.cloudiator.sword.drivers.azure.internal.ResourceGroupNamingStrategy;
import de.uniulm.omi.cloudiator.sword.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import java.util.Arrays;
import java.util.List;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

/**
 * Created by daniel on 22.05.17.
 */
public class AzureCreateVirtualMachineStrategy implements
    CreateVirtualMachineStrategy {

  private final Azure azure;
  private final GetStrategy<String, HardwareFlavor> hardwareGetStrategy;
  private final OneWayConverter<com.microsoft.azure.management.compute.VirtualMachine, VirtualMachine> virtualMachineConverter;
  private final ResourceGroupNamingStrategy resourceGroupNamingStrategy;

  @Inject
  public AzureCreateVirtualMachineStrategy(Azure azure,
      GetStrategy<String, HardwareFlavor> hardwareGetStrategy,
      OneWayConverter<com.microsoft.azure.management.compute.VirtualMachine, VirtualMachine> virtualMachineConverter,
      ResourceGroupNamingStrategy resourceGroupNamingStrategy) {

    checkNotNull(resourceGroupNamingStrategy, "resourceGroupNamingStrategy is null");
    this.resourceGroupNamingStrategy = resourceGroupNamingStrategy;

    checkNotNull(virtualMachineConverter, "virtualMachineConverter is null");
    this.virtualMachineConverter = virtualMachineConverter;

    checkNotNull(hardwareGetStrategy, "hardwareGetStrategy is null");
    this.hardwareGetStrategy = hardwareGetStrategy;

    checkNotNull(azure, "azure is null");
    this.azure = azure;

  }

  @Override
  public VirtualMachine apply(VirtualMachineTemplate virtualMachineTemplate) {

    HardwareFlavor hardwareFlavor = hardwareGetStrategy
        .get(virtualMachineTemplate.hardwareFlavorId());
    checkState(hardwareFlavor != null, String.format("hardwareFlavor with id %s does not exist",
        virtualMachineTemplate.hardwareFlavorId()));

    final String resourceGroupName = resourceGroupNamingStrategy
        .apply(virtualMachineTemplate.locationId());

    final boolean nodeGroupResourceGroupExists = azure.resourceGroups()
        .checkExistence(resourceGroupName);
    if (!nodeGroupResourceGroupExists) {
      azure.resourceGroups().define(resourceGroupName)
          .withRegion(virtualMachineTemplate.locationId())
          .create();
    }

    handleSecurityGroups(virtualMachineTemplate);

    String password = generatePassword();

    final com.microsoft.azure.management.compute.VirtualMachine virtualMachine = azure
        .virtualMachines().define(virtualMachineTemplate.name())
        .withRegion(virtualMachineTemplate.locationId())
        .withExistingResourceGroup(resourceGroupName)
        .withNewPrimaryNetwork("10.0.0.0/28").withPrimaryPrivateIPAddressDynamic()
        .withNewPrimaryPublicIPAddress(virtualMachineTemplate.name().toLowerCase())
        .withPopularLinuxImage(
            KnownLinuxVirtualMachineImage.valueOf(virtualMachineTemplate.imageId()))
        .withRootUsername("azure").withRootPassword(password)
        .withSize(hardwareFlavor.providerId()).create();

    VirtualMachine startedVM = virtualMachineConverter.apply(virtualMachine);

    return VirtualMachineBuilder.of(startedVM)
        .loginCredential(
            LoginCredentialBuilder.newBuilder().password(password).username("azure").build())
        .build();

  }

  private void handleSecurityGroups(VirtualMachineTemplate virtualMachineTemplate) {
    if (!virtualMachineTemplate.templateOptions().isPresent()) {
      return;
    }
    virtualMachineTemplate.templateOptions().get().inboundPorts()
        .forEach(port -> azure.networkSecurityGroups()
            .define(
                resourceGroupNamingStrategy.apply(virtualMachineTemplate.locationId()) + "SecGroup")
            .withRegion(virtualMachineTemplate.locationId())
            .withExistingResourceGroup(
                resourceGroupNamingStrategy.apply(virtualMachineTemplate.locationId()))
            .defineRule("Allow " + port).allowInbound().fromAnyAddress().fromAnyPort()
            .toAnyAddress().toPort(port).withAnyProtocol().attach());
  }

  private String generatePassword() {
    PasswordGenerator passwordGenerator = new PasswordGenerator();

    List<CharacterRule> rules = Arrays.asList(

        // at least one upper-case character
        new CharacterRule(EnglishCharacterData.UpperCase, 1),

        // at least one lower-case character
        new CharacterRule(EnglishCharacterData.LowerCase, 1),

        // at least one digit character
        new CharacterRule(EnglishCharacterData.Digit, 1),

        // at least one symbol (special character)
        new CharacterRule(SpecialCharacterData.INSTANCE, 1));

    return passwordGenerator.generatePassword(8, rules);
  }

  private static class SpecialCharacterData implements CharacterData {

    private static final SpecialCharacterData INSTANCE = new SpecialCharacterData();

    @Override
    public String getErrorCode() {
      return "INSUFFICIENT_SPECIAL";
    }

    @Override
    public String getCharacters() {
      return "!$%&/()=?;,;.:-_<>@^";
    }
  }
}
