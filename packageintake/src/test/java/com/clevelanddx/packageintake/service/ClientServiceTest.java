package com.clevelanddx.packageintake.service;

import com.clevelanddx.packageintake.dto.ClientDTO;
import com.clevelanddx.packageintake.model.Client;
import com.clevelanddx.packageintake.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository repository;

    @Mock
    private ClientCacheService cacheService;

    @InjectMocks
    private ClientServiceImpl service;

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
    void create_ShouldClearCacheAndReturnSavedClient() {
        when(repository.save(any(Client.class))).thenReturn(testClient);

        Client result = service.create(testClient);

        assertNotNull(result);
        assertEquals(testClient.getClient(), result.getClient());
        verify(cacheService).clearCache();
    }

    @Test
    void update_WhenExists_ShouldClearCacheAndReturnUpdatedClient() {
        when(repository.findById(1L)).thenReturn(Optional.of(testClient));
        when(repository.save(any(Client.class))).thenReturn(testClient);

        Client result = service.update(1L, testClient);

        assertNotNull(result);
        assertEquals(testClient.getClient(), result.getClient());
        verify(cacheService).clearCache();
    }

    @Test
    void update_WhenNotExists_ShouldThrowException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(1L, testClient));
        verify(cacheService, never()).clearCache();
    }

    @Test
    void delete_WhenExists_ShouldClearCache() {
        when(repository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> service.delete(1L));
        verify(repository).deleteById(1L);
        verify(cacheService).clearCache();
    }

    @Test
    void delete_WhenNotExists_ShouldThrowException() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> service.delete(1L));
        verify(cacheService, never()).clearCache();
    }

    @Test
    void findById_ShouldUseCache() {
        when(cacheService.getClientById(1L)).thenReturn(Optional.of(testClientDTO));

        Optional<Client> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(testClient.getClient(), result.get().getClient());
        verify(cacheService).getClientById(1L);
    }

    @Test
    void findAll_ShouldUseCache() {
        when(cacheService.getAllClients()).thenReturn(Arrays.asList(testClientDTO));

        List<Client> result = service.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(testClient.getClient(), result.get(0).getClient());
        verify(cacheService).getAllClients();
    }

    @Test
    void findByClient_ShouldUseCache() {
        when(cacheService.getClientByName("Test Client")).thenReturn(Optional.of(testClientDTO));

        Optional<Client> result = service.findByClient("Test Client");

        assertTrue(result.isPresent());
        assertEquals(testClient.getClient(), result.get().getClient());
        verify(cacheService).getClientByName("Test Client");
    }

    @Test
    void existsByClient_ShouldUseCache() {
        when(cacheService.getClientByName("Test Client")).thenReturn(Optional.of(testClientDTO));

        boolean result = service.existsByClient("Test Client");

        assertTrue(result);
        verify(cacheService).getClientByName("Test Client");
    }
} 