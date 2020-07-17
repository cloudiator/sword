package de.uniulm.omi.cloudiator.sword.onestep.strategies;

import com.oktawave.api.client.ApiException;
import com.oktawave.api.client.api.AccountApi;
import com.oktawave.api.client.api.OciApi;
import com.oktawave.api.client.api.OciTemplatesApi;
import com.oktawave.api.client.api.TicketsApi;
import com.oktawave.api.client.model.*;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachineTemplate;
import de.uniulm.omi.cloudiator.sword.onestep.domain.InstanceWithAccessData;
import de.uniulm.omi.cloudiator.sword.onestep.domain.AccessData;
import de.uniulm.omi.cloudiator.sword.strategy.CreateVirtualMachineStrategy;
import de.uniulm.omi.cloudiator.sword.util.NamingStrategy;
import de.uniulm.omi.cloudiator.util.OneWayConverter;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final OciApi ociApi;
    private final AccountApi accountApi;
    private final TicketsApi ticketsApi;
    private final OciTemplatesApi ociTemplatesApi;
    private final NamingStrategy namingStrategy;
    private final OneWayConverter<InstanceWithAccessData, VirtualMachine> instanceConverter;
    private final KeyPairGenerator keyPairGenerator;


    @Inject
    public OktawaveCreateVirtualMachineStrategy(OciApi ociApi,
                                                AccountApi accountApi,
                                                TicketsApi ticketsApi,
                                                OciTemplatesApi ociTemplatesApi,
                                                NamingStrategy namingStrategy,
                                                OneWayConverter<InstanceWithAccessData, VirtualMachine> instanceConverter
    ) {
        this.ociApi = Objects.requireNonNull(ociApi);
        this.accountApi = Objects.requireNonNull(accountApi);
        this.ticketsApi = Objects.requireNonNull(ticketsApi);
        this.ociTemplatesApi = Objects.requireNonNull(ociTemplatesApi);
        this.namingStrategy = checkNotNull(namingStrategy);
        this.instanceConverter = instanceConverter;
        this.keyPairGenerator = new RSAKeyPairGenerator();
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

        SshKey tempSshKey = null;
        try {
            tempSshKey = accountApi.accountPostSshKey(createSshKeyCommand);
        } catch (ApiException e) {
            LOGGER.warn("Could not create sshKey, User and password will be used");
            LOGGER.error("ApiException: " + e.getCode() + ", ResponseBody: " + e.getResponseBody());
        }

        SshKey sshKey = tempSshKey;

        CreateInstanceCommand createInstanceCommand = new CreateInstanceCommand();
        createInstanceCommand.setInstanceName(vmName);
        createInstanceCommand.setInstancesCount(1);
        createInstanceCommand.setTemplateId(Integer.valueOf(virtualMachineTemplate.imageId()));
        createInstanceCommand.setTypeId(Integer.valueOf(virtualMachineTemplate.hardwareFlavorId()));
        createInstanceCommand.setSubregionId(Integer.valueOf(virtualMachineTemplate.locationId()));
        createInstanceCommand.setDiskClass(48); // id 48 -> Tier 1
        createInstanceCommand.setDiskSize(20); // default disk size
        if (sshKey != null) {
            createInstanceCommand.setSshKeysIds(Collections.singletonList(sshKey.getId()));
            createInstanceCommand.setAuthorizationMethodId(1398);
        } else {
            createInstanceCommand.setAuthorizationMethodId(1399);
        }

        try {
            Ticket ticket = ociApi.instancesPost(createInstanceCommand);
            while ("Running".equals(ticket.getStatus().getLabel())) {

                sleep(5000);

                ticket = ticketsApi.ticketsGet_0(ticket.getId(), null);
                LOGGER.info("Checking VM name: " + ticket.getObjectName() + " - " + ticket.getProgress() + "%");
            }

            if ("Succeed".equals(ticket.getStatus().getLabel())) {

                Instance instance = null;
                boolean isTurnedOn = false;
                while (!isTurnedOn) {
                    ApiCollectionInstance apiCollectionInstance = ociApi.instancesGet(null, null, null, null, null, null, null, null, null);

                    String instanceName = ticket.getObjectName();
                    String names = apiCollectionInstance.getItems().stream().map(Instance::getName).collect(Collectors.joining(",", "[", "]"));

                    LOGGER.info(String.format("Looking for instanceName: %s in %s", instanceName, names));

                    Optional<Instance> first = apiCollectionInstance
                            .getItems()
                            .stream()
                            .filter(i -> i.getName().equalsIgnoreCase(instanceName))
                            .findFirst();

                    if (first.isPresent()) {
                        if ("Powered On".equalsIgnoreCase(first.get().getStatus().getLabel())) {
                            LOGGER.info(String.format("Instance: %s is in %s state", instanceName, first.get().getStatus().getLabel()));
                            instance = first.get();
                            isTurnedOn = true;
                        } else {
                            LOGGER.info(String.format("Instance: %s is in %s state. Waiting for initialization", instanceName, first.get().getStatus().getLabel()));
                            sleep(3000);
                        }
                    } else {
                        LOGGER.info(String.format("Instance: %s not created yet", instanceName));
                    }
                }

                return instanceConverter.apply(new InstanceWithAccessData(instance, getAccessData(instance.getId(), instance.getTemplate().getId(), sshKey != null ? extendedKeyPair.getPrivateKey() : null)));
            }
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
            com.oktawave.api.client.model.AccessData octawaveAccessData = ociApi.instancesGetAccessData(instanceId, null);
            while (octawaveAccessData == null && attemptNumber < maxAttempts) {
                sleep(sleepTime);
                octawaveAccessData = ociApi.instancesGetAccessData(instanceId, null);
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
