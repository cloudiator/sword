package de.uniulm.omi.cloudiator.sword.drivers.onestep.strategies;

import client.ApiException;
import client.api.AccountApi;
import client.api.InstancesApi;
import client.model.instances.ApiCollectionInstance;
import client.model.instances.CreateInstanceCommand;
import client.model.account.CreateSshKeyCommand;
import client.model.account.SshKeyId;
import client.model.instances.Instance;
import client.model.instances.InstanceId;
import de.uniulm.omi.cloudiator.sword.domain.HardwareFlavor;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachineTemplate;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.domain.InstanceWithAccessData;
import de.uniulm.omi.cloudiator.sword.drivers.onestep.internal.HardwareFlavourNamingStrategy;
import de.uniulm.omi.cloudiator.sword.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.strategy.GetStrategy;
import de.uniulm.omi.cloudiator.sword.util.NamingStrategy;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
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
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class OktawaveCreateVirtualMachineStrategy implements CreateVirtualMachineStrategy {

    private static Logger LOGGER = LoggerFactory.getLogger(OktawaveCreateVirtualMachineStrategy.class);

    private final InstancesApi instancesApi;
    private final AccountApi accountApi;
    private final TicketsApi ticketsApi;
    private final OciTemplatesApi ociTemplatesApi;
    private final NamingStrategy namingStrategy;
    private final OneWayConverter<InstanceWithAccessData, VirtualMachine> instanceConverter;
    private final KeyPairGenerator keyPairGenerator;
    private final GetStrategy<String, HardwareFlavor> hardwareGetStrategy;


    @Inject
    public OktawaveCreateVirtualMachineStrategy(InstancesApi instancesApi,
                                                AccountApi accountApi,
                                                TicketsApi ticketsApi,
                                                OciTemplatesApi ociTemplatesApi,
                                                NamingStrategy namingStrategy,
                                                OneWayConverter<InstanceWithAccessData, VirtualMachine> instanceConverter,
                                                GetStrategy<String, HardwareFlavor> hardwareGetStrategy
    ) {
        this.instancesApi = Objects.requireNonNull(instancesApi);
        this.accountApi = Objects.requireNonNull(accountApi);
        this.ticketsApi = Objects.requireNonNull(ticketsApi);
        this.ociTemplatesApi = Objects.requireNonNull(ociTemplatesApi);
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

        HardwareFlavor hardwareFlavor =  hardwareGetStrategy.get(Integer.valueOf(virtualMachineTemplate.hardwareFlavorId());

        CreateInstanceCommand createInstanceCommand = new CreateInstanceCommand();
        createInstanceCommand.setName(vmName);
        createInstanceCommand.setOperatingSystemVersionId(Integer.valueOf(virtualMachineTemplate.imageId())); // id 48 -> Tier 1
        createInstanceCommand.setCpuCores(hardwareFlavor.numberOfCores());
        createInstanceCommand.setClusterId(HardwareFlavourNamingStrategy.getClusterIdFromHardwareFlavourName(hardwareFlavor.name()));
        createInstanceCommand.setPrivateNetworkId(Integer.valueOf(virtualMachineTemplate.imageId()));
        createInstanceCommand.setAuthorisation(Integer.valueOf(virtualMachineTemplate.locationId()));
        createInstanceCommand.addDedicatedPublicIp(true);

        if (sshKey != null) {
            createInstanceCommand.setAdditionalDisks(Collections.singletonList(sshKey.getId()));
            createInstanceCommand.setName(1398);
        } else {
            createInstanceCommand.setName(1399);
        }

        try {
            InstanceId newInstance = instancesApi.instancesPost(createInstanceCommand);

            Instance instance = null;
            boolean isTurnedOn = false;
            while (!isTurnedOn) {
                ApiCollectionInstance apiCollectionInstance = instancesApi.instancesGet(null, null, null, null, null, null, null, null, null);

                String ids = apiCollectionInstance.getInstances().stream()
                        .map(Instance::getId)
                        .map(Integer::toString)
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
                    LOGGER.info(String.format("Instance: %s is in %s state. Waiting for initialization", instanceName, first.get().getState()));
                    sleep(3000);
                }
            }

            return instanceConverter.apply(new InstanceWithAccessData(instance, getAccessData(instance.getId(), instance.getTemplate().getId(), sshKey != null ? extendedKeyPair.getPrivateKey() : null)));

        } catch (ApiException e) {
            LOGGER.error("ApiException: " + e.getCode() + ", ResponseBody: " + e.getResponseBody());
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Something went wrong...");
    }

    private void sleep(long milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
        }
    }

    private AccessData getAccessData(Integer instanceId, Integer templateId, String privateKey) {

        try {
            // get octawaveAccessData with retry mechanism
            int maxAttempts = 3;
            int attemptNumber = 0;
            int sleepTime = 60000;
            com.oktawave.api.client.model.AccessData octawaveAccessData = instancesApi.instancesGetAccessData(instanceId, null);
            while (octawaveAccessData == null && attemptNumber < maxAttempts) {
                sleep(sleepTime);
                octawaveAccessData = instancesApi.instancesGetAccessData(instanceId, null);
                LOGGER.info("Sleep while waiting for availability of Oktawave access data");
                attemptNumber++;
            }

            if (octawaveAccessData == null) {
                throw new RuntimeException(String.format("Unable to gaining AccesessData. After %d attempts every %d millis is still null", maxAttempts, sleepTime));
            }
            Template template = ociTemplatesApi.templatesGet_0(templateId, null);

            final String login = template.getCreationUser().getLogin();
            final String password = octawaveAccessData.getPassword();
            return new AccessData(login, password, privateKey);

        } catch (ApiException e) {
            throw new RuntimeException("Error during gaining AccessData", e);
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
