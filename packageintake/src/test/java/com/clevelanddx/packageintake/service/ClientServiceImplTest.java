package com.clevelanddx.packageintake.service;

import com.clevelanddx.packageintake.model.Client;
import com.clevelanddx.packageintake.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientCacheService clientCacheService;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client createTestClient(Long id, String clientName) {
        Client client = new Client();
        client.setId(id);
        client.setClient(clientName);
        return client;
    }

    @Test
    void searchClients_ShouldReturnMatchingClients() {
        // Given
        String searchTerm = "test";
        List<Client> expectedClients = Arrays.asList(
            createTestClient(1L, "test1"),
            createTestClient(2L, "test2")
        );
        when(clientRepository.searchByClient(searchTerm)).thenReturn(expectedClients);

        // When
        List<Client> result = clientService.searchClients(searchTerm);

        // Then
        assertEquals(expectedClients, result);
        verify(clientRepository).searchByClient(searchTerm);
    }

    @Test
    void searchClientByPartial_ShouldReturnMatchingClients() {
        // Given
        String searchTerm = "test";
        List<Client> expectedClients = Arrays.asList(
            createTestClient(1L, "test1"),
            createTestClient(2L, "test2")
        );
        when(clientRepository.searchClientByPartial(searchTerm)).thenReturn(expectedClients);

        // When
        List<Client> result = clientService.searchClientByPartial(searchTerm);

        // Then
        assertEquals(expectedClients, result);
        verify(clientRepository).searchClientByPartial(searchTerm);
    }
} 