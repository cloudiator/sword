package de.uniulm.omi.cloudiator.sword.drivers.onestep.strategies;

import client.ApiException;
import client.api.AccountApi;
import client.api.InstancesApi;
import client.model.instances.*;
import client.model.account.CreateSshKeyCommand;
import client.model.account.SshKeyId;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachineTemplate;
import client.model.InstanceData;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.internal.HardwareFlavourNamingStrategy;
import de.uniulm.omi.cloudiator.sword.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.sword.util.NamingStrategy;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class OnestepCreateVirtualMachineStrategy implements CreateVirtualMachineStrategy {

    private static Logger LOGGER = LoggerFactory.getLogger(OnestepCreateVirtualMachineStrategy.class);

    private final InstancesApi instancesApi;
    private final AccountApi accountApi;
    private final NamingStrategy namingStrategy;
    private final OneWayConverter<InstanceData, VirtualMachine> instanceConverter;
    private final KeyPairGenerator keyPairGenerator;
    private final GetStrategy<String, HardwareFlavor> hardwareGetStrategy;


    @Inject
    public OnestepCreateVirtualMachineStrategy(InstancesApi instancesApi,
                                               AccountApi accountApi,
                                               NamingStrategy namingStrategy,
                                               OneWayConverter<InstanceData, VirtualMachine> instanceConverter,
                                               GetStrategy<String, HardwareFlavor> hardwareGetStrategy
    ) {
        this.instancesApi = Objects.requireNonNull(instancesApi);
        this.accountApi = Objects.requireNonNull(accountApi);
        this.namingStrategy = checkNotNull(namingStrategy);
        this.instanceConverter = instanceConverter;
        this.keyPairGenerator = new RSAKeyPairGenerator();
        this.hardwareGetStrategy = hardwareGetStrategy;
    }

    @Nullable
    @Override
    public VirtualMachine apply(@Nullable VirtualMachineTemplate virtualMachineTemplate) {
        LOGGER.info("Creating instance from VirtualMachineTemplate: " + virtualMachineTemplate);

        String vmName = namingStrategy.generateUniqueNameBasedOnName(virtualMachineTemplate.name(), 40);
        if (vmName.length() > 40) {
            throw new RuntimeException("InstanceName should be shorter than 40. Current value is " + vmName + ", size: " + vmName.length());
        }
        ExtendedKeyPair extendedKeyPair = keyPairGenerator.generate();

        CreateSshKeyCommand createSshKeyCommand = new CreateSshKeyCommand();
        createSshKeyCommand.setSshKey(extendedKeyPair.getPublicKey());
        createSshKeyCommand.setSshKeyName(UUID.randomUUID().toString());

        SshKeyId tempSshKey = null;
        try {
            tempSshKey = accountApi.accountPostSshKey(createSshKeyCommand);
        } catch (ApiException e) {
            LOGGER.warn("Could not create sshKey, User and password will be used");
            LOGGER.error("ApiException: " + e.getCode() + ", ResponseBody: " + e.getResponseBody());
        }

        SshKeyId sshKey = tempSshKey;

        HardwareFlavor hardwareFlavor =  hardwareGetStrategy.get(virtualMachineTemplate.hardwareFlavorId());

        Disk disk = new Disk();
        disk.setType("hdd");
        disk.setSize(20);

        CreateInstanceCommand createInstanceCommand = new CreateInstanceCommand();
        createInstanceCommand.setName(vmName);
        createInstanceCommand.setOperatingSystemVersionId(Integer.valueOf(virtualMachineTemplate.imageId())); // id 48 -> Tier 1
        createInstanceCommand.setClusterId(HardwareFlavourNamingStrategy.getClusterIdFromHardwareFlavourName(hardwareFlavor.name()));
        createInstanceCommand.setCpuCores(hardwareFlavor.numberOfCores());
        createInstanceCommand.setRam(hardwareFlavor.mbRam());
        createInstanceCommand.setPrimaryDisk(disk);
        createInstanceCommand.setAdditionalDisks(null);
        createInstanceCommand.setPrivateNetworkId(null);
        createInstanceCommand.addDedicatedPublicIp(true);

        Authorisation authorisation = new Authorisation();
        if (sshKey != null) {
            authorisation.setType("ssh");
            authorisation.addSshKey(sshKey.getId(), null);
        } else {
            authorisation.setType("password");
        }
        createInstanceCommand.setAuthorisation(authorisation);

        try {
            InstanceId newInstance = instancesApi.instancesPost(createInstanceCommand, Integer.parseInt(virtualMachineTemplate.locationId()));

            Instance instance = null;
            boolean isTurnedOn = false;
            while (!isTurnedOn) {
                ApiCollectionInstance apiCollectionInstance = instancesApi.instancesGet(Integer.parseInt(virtualMachineTemplate.locationId()));

                String ids = apiCollectionInstance.getInstances().stream()
                        .map(i -> Integer.toString(i.getId()))
                        .collect(Collectors.joining(",", "[", "]"));

                LOGGER.info(String.format("Looking for instance Id: %s in %s", newInstance.getId(), ids));

                Optional<Instance> first = apiCollectionInstance
                        .getInstances()
                        .stream()
                        .filter(i -> i.getId().equals(newInstance.getId()))
                        .findFirst();

                String instanceName = first.get().getName();

                if ("powered_on".equalsIgnoreCase(first.get().getState())) {
                    LOGGER.info(String.format("Instance: %s is in %s state", instanceName, first.get().getState()));
                    instance = first.get();
                    isTurnedOn = true;
                } else {
                    LOGGER.info(String.format("Instance: %s is in %s state. Waiting for initialization", instanceName, instance.getState()));
                    sleep(3000);
                }
            }

            //download instance details
            InstanceDetails instanceDetails = instancesApi.instanceDetailsGet(Integer.parseInt(virtualMachineTemplate.locationId()), instance.getId());
            InstanceData instanceData = new InstanceData(
                    instanceDetails,
                    virtualMachineTemplate.hardwareFlavorId(),
                    virtualMachineTemplate.imageId(),
                    virtualMachineTemplate.locationId(),
                    sshKey != null ? extendedKeyPair.getPrivateKey() : null
            );
            instancesApi.getApiClient().addInstance(instanceData);

            return instanceConverter.apply(instanceData);

        } catch (ApiException e) {
            LOGGER.error("ApiException: " + e.getCode() + ", ResponseBody: " + e.getResponseBody());
            throw new RuntimeException(e);
        }
    }

    private void sleep(long milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
        }
    }


    interface KeyPairGenerator {
        ExtendedKeyPair generate();
    }

    class RSAKeyPairGenerator implements KeyPairGenerator {

        private java.security.KeyPairGenerator kpg;

        RSAKeyPairGenerator() {

            try {
                kpg = java.security.KeyPairGenerator.getInstance("RSA");
                kpg.initialize(2048);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("No SuchAlgorithmException", e);
            }
        }

        @Override
        public ExtendedKeyPair generate() {
            return new RSAExtendedKeyPair(kpg.genKeyPair());
        }
    }

    interface ExtendedKeyPair {

        String getPrivateKey();
        String getPublicKey();
    }

    class RSAExtendedKeyPair implements ExtendedKeyPair {

        private KeyPair keyPair;

        RSAExtendedKeyPair(KeyPair keyPair) {
            this.keyPair = keyPair;
        }

        @Override
        public String getPrivateKey() {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try (PemWriter pemWriter = new PemWriter(new OutputStreamWriter(outputStream))) {
                //Need to convert to PKCS#1
                PrivateKeyInfo pkInfo = PrivateKeyInfo.getInstance(keyPair.getPrivate().getEncoded());
                ASN1Encodable privateKeyPKCS1ASN1Encodable = pkInfo.parsePrivateKey();
                ASN1Primitive privateKeyPKCS1ASN1 = privateKeyPKCS1ASN1Encodable.toASN1Primitive();

                pemWriter.writeObject(new PemObject("RSA PRIVATE KEY", privateKeyPKCS1ASN1.getEncoded()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return new String(outputStream.toByteArray());
        }

        @Override
        public String getPublicKey() {
            try {
                RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
                ByteArrayOutputStream byteOs = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(byteOs);
                dos.writeInt("ssh-rsa".getBytes().length);
                dos.write("ssh-rsa".getBytes());
                dos.writeInt(rsaPublicKey.getPublicExponent().toByteArray().length);
                dos.write(rsaPublicKey.getPublicExponent().toByteArray());
                dos.writeInt(rsaPublicKey.getModulus().toByteArray().length);
                dos.write(rsaPublicKey.getModulus().toByteArray());
                String publicKeyEncoded = new String(org.apache.commons.codec.binary.Base64.encodeBase64(byteOs.toByteArray()));
                return "ssh-rsa " + publicKeyEncoded + " comment";
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

}
