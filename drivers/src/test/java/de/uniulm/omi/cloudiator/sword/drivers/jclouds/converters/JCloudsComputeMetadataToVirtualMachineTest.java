package de.uniulm.omi.cloudiator.sword.drivers.jclouds.converters;

import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.sword.api.domain.LoginCredential;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.domain.LoginCredentials;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by daniel on 31.07.15.
 */
public class JCloudsComputeMetadataToVirtualMachineTest {

    @SuppressWarnings("unchecked") private OneWayConverter<LoginCredentials, LoginCredential>
        loginCredentialsConverter = mock(OneWayConverter.class);
    private JCloudsComputeMetadataToVirtualMachine jCloudsComputeMetadataToVirtualMachine;
    private NodeMetadata nodeMetadata = mock(NodeMetadata.class);
    private LoginCredential loginCredential = mock(LoginCredential.class);
    private LoginCredentials loginCredentials = mock(LoginCredentials.class);

    @Before public void setUp() throws Exception {
        jCloudsComputeMetadataToVirtualMachine =
            new JCloudsComputeMetadataToVirtualMachine(loginCredentialsConverter);
    }

    @Test public void testApply() throws Exception {

        when(nodeMetadata.getId()).thenReturn("id");
        when(nodeMetadata.getProviderId()).thenReturn("providerId");
        when(nodeMetadata.getName()).thenReturn("name");
        when(nodeMetadata.getPublicAddresses())
            .thenReturn(new HashSet<>(Collections.singletonList("93.184.216.34")));
        when(nodeMetadata.getPrivateAddresses())
            .thenReturn(new HashSet<>(Collections.singletonList("192.168.0.1")));
        when(nodeMetadata.getCredentials()).thenReturn(loginCredentials);
        when(loginCredentialsConverter.apply(loginCredentials)).thenReturn(loginCredential);

        jCloudsComputeMetadataToVirtualMachine.apply(nodeMetadata);

        verify(nodeMetadata, atLeast(1)).getCredentials();
        verify(nodeMetadata).getPrivateAddresses();
        verify(nodeMetadata).getPublicAddresses();
        verify(nodeMetadata).getId();
        verify(nodeMetadata).getProviderId();
        verify(nodeMetadata).getName();
        verify(loginCredentialsConverter).apply(any());


    }
}
