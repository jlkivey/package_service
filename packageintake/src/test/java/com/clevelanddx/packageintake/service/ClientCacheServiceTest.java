package com.clevelanddx.packageintake.service;

import com.clevelanddx.packageintake.dto.ClientDTO;
import com.clevelanddx.packageintake.model.Client;
import com.clevelanddx.packageintake.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientCacheServiceTest {

    @Mock
    private ClientRepository repository;

    @InjectMocks
    private ClientCacheService cacheService;

    private Client testClient;
    private ClientDTO testClientDTO;

    @BeforeEach
    void setUp() {
        testClient = new Client();
        testClient.setId(1L);
        testClient.setClient("Test Client");
        testClient.setLastUpdateUser("test.user");
        testClient.setLastUpdateTime(LocalDateTime.now());

        testClientDTO = ClientDTO.builder()
                .id(1L)
                .client("Test Client")
                .lastUpdateUser("test.user")
                .build();
    }

    @Test
    void getAllClients_ShouldReturnCachedClients() {
        when(repository.findAll()).thenReturn(Arrays.asList(testClient));

        List<ClientDTO> firstCall = cacheService.getAllClients();
        List<ClientDTO> secondCall = cacheService.getAllClients();

        assertEquals(1, firstCall.size());
        assertEquals(testClientDTO.getClient(), firstCall.get(0).getClient());
        assertEquals(firstCall, secondCall);
        verify(repository, times(1)).findAll();
    }

    @Test
    void getClientById_ShouldReturnCachedClient() {
        when(repository.findById(1L)).thenReturn(Optional.of(testClient));

        Optional<ClientDTO> firstCall = cacheService.getClientById(1L);
        Optional<ClientDTO> secondCall = cacheService.getClientById(1L);

        assertTrue(firstCall.isPresent());
        assertEquals(testClientDTO.getClient(), firstCall.get().getClient());
        assertEquals(firstCall, secondCall);
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void getClientByName_ShouldReturnCachedClient() {
        when(repository.findByClient("Test Client")).thenReturn(Optional.of(testClient));

        Optional<ClientDTO> firstCall = cacheService.getClientByName("Test Client");
        Optional<ClientDTO> secondCall = cacheService.getClientByName("Test Client");

        assertTrue(firstCall.isPresent());
        assertEquals(testClientDTO.getClient(), firstCall.get().getClient());
        assertEquals(firstCall, secondCall);
        verify(repository, times(1)).findByClient("Test Client");
    }

    @Test
    void clearCache_ShouldClearAllCaches() {
        when(repository.findAll()).thenReturn(Arrays.asList(testClient));

        // Populate cache
        cacheService.getAllClients();
        cacheService.clearCache();

        // Should hit repository again after clear
        cacheService.getAllClients();

        verify(repository, times(2)).findAll();
    }
} 