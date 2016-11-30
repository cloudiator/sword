package de.uniulm.omi.cloudiator.sword.drivers.openstack.extensions;

import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.sword.api.domain.KeyPair;
import de.uniulm.omi.cloudiator.sword.api.util.NamingStrategy;
import de.uniulm.omi.cloudiator.sword.drivers.openstack.OpenstackKeyPairClient;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by daniel on 31.07.15.
 */
public class OpenstackKeyPairServiceTest {

    @SuppressWarnings("unchecked")
    private final OneWayConverter<org.jclouds.openstack.nova.v2_0.domain.KeyPair, KeyPair>
        keyPairConverter = mock(OneWayConverter.class);
    private final OpenstackKeyPairClient openstackKeyPairClient =
        mock(OpenstackKeyPairClient.class);
    private final org.jclouds.openstack.nova.v2_0.domain.KeyPair keyPair =
        mock(org.jclouds.openstack.nova.v2_0.domain.KeyPair.class);
    private final NamingStrategy namingStrategy = mock(NamingStrategy.class);

    private OpenstackKeyPairService openstackKeyPairService;

    @Before public void setUp() throws Exception {
        openstackKeyPairService =
            new OpenstackKeyPairService(keyPairConverter, openstackKeyPairClient, namingStrategy);
        when(openstackKeyPairClient.create(any())).thenReturn(keyPair);
        when(openstackKeyPairClient.createWithPublicKey(any(), any())).thenReturn(keyPair);
        when(openstackKeyPairClient.get(any())).thenReturn(keyPair);
        when(namingStrategy.generateUniqueNameInGroup(any())).thenReturn("MockName");
    }

    @Test public void testCreate() throws Exception {
        openstackKeyPairService.create("name");
        verify(openstackKeyPairClient).create(namingStrategy.generateUniqueNameInGroup("name"));
        verify(keyPairConverter).apply(keyPair);
    }

    @Test public void testCreatePublicKey() throws Exception {
        openstackKeyPairService.create("name", "publicKey");
        verify(openstackKeyPairClient)
            .createWithPublicKey(namingStrategy.generateUniqueNameInGroup("name"), "publicKey");
        verify(keyPairConverter).apply(keyPair);
    }

    @Test public void testDelete() throws Exception {
        //todo
    }

    @Test public void testGet() throws Exception {
        //todo
    }
}
