package com.clevelanddx.packageintake.service;

import com.clevelanddx.packageintake.dto.ClientDTO;
import com.clevelanddx.packageintake.model.Client;
import com.clevelanddx.packageintake.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ClientCacheService {
    private final ClientRepository clientRepository;
    private final Map<String, ClientDTO> clientCache = new ConcurrentHashMap<>();
    private final Map<Long, ClientDTO> clientIdCache = new ConcurrentHashMap<>();
    private List<ClientDTO> allClientsCache;

    @Autowired
    public ClientCacheService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<ClientDTO> getAllClients() {
        if (allClientsCache != null) {
            return allClientsCache;
        }
        
        List<Client> clients = clientRepository.findAll();
        allClientsCache = clients.stream()
                .map(this::convertToDTO)
                .peek(dto -> {
                    clientCache.put(dto.getClient(), dto);
                    clientIdCache.put(dto.getId(), dto);
                })
                .collect(Collectors.toList());
        return allClientsCache;
    }

    public Optional<ClientDTO> getClientById(Long id) {
        ClientDTO cached = clientIdCache.get(id);
        if (cached != null) {
            return Optional.of(cached);
        }
        return clientRepository.findById(id)
                .map(this::convertToDTO)
                .map(dto -> {
                    clientCache.put(dto.getClient(), dto);
                    clientIdCache.put(dto.getId(), dto);
                    return dto;
                });
    }

    public Optional<ClientDTO> getClientByName(String clientName) {
        ClientDTO cached = clientCache.get(clientName);
        if (cached != null) {
            return Optional.of(cached);
        }
        return clientRepository.findByClient(clientName)
                .map(this::convertToDTO)
                .map(dto -> {
                    clientCache.put(dto.getClient(), dto);
                    clientIdCache.put(dto.getId(), dto);
                    return dto;
                });
    }

    @CacheEvict(value = "clients", allEntries = true)
    public void clearCache() {
        clientCache.clear();
        clientIdCache.clear();
        allClientsCache = null;
    }

    private ClientDTO convertToDTO(Client client) {
        return ClientDTO.builder()
                .id(client.getId())
                .client(client.getClient())
                .lastUpdateUser(client.getLastUpdateUser())
                .build();
    }
} 