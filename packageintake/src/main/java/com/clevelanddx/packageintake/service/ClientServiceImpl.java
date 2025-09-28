package com.clevelanddx.packageintake.service;

import com.clevelanddx.packageintake.model.Client;
import com.clevelanddx.packageintake.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {
    
    private final ClientRepository repository;
    private final ClientCacheService cacheService;
    
    @Autowired
    public ClientServiceImpl(ClientRepository repository, ClientCacheService cacheService) {
        this.repository = repository;
        this.cacheService = cacheService;
    }
    
    @Override
    public Client create(Client client) {
        Client savedClient = repository.save(client);
        cacheService.clearCache();
        return savedClient;
    }

    @Override
    public Client update(Long id, Client client) {
        Optional<Client> existingClient = repository.findById(id);
        if (existingClient.isPresent()) {
            Client updatedClient = existingClient.get();
            updatedClient.setClient(client.getClient());
            updatedClient.setLastUpdateUser(client.getLastUpdateUser());
            Client savedClient = repository.save(updatedClient);
            cacheService.clearCache();
            return savedClient;
        }
        throw new EntityNotFoundException("Client not found with id: " + id);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Client not found with id: " + id);
        }
        repository.deleteById(id);
        cacheService.clearCache();
    }

    @Override
    public Optional<Client> findById(Long id) {
        return cacheService.getClientById(id)
                .map(this::convertToEntity);
    }

    @Override
    public List<Client> findAll() {
        return cacheService.getAllClients().stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Client> findByClient(String client) {
        return cacheService.getClientByName(client)
                .map(this::convertToEntity);
    }

    @Override
    public List<Client> findByLastUpdateUser(String lastUpdateUser) {
        return repository.findByLastUpdateUser(lastUpdateUser);
    }

    @Override
    public boolean existsByClient(String client) {
        return cacheService.getClientByName(client).isPresent();
    }

    @Override
    public List<Client> searchClients(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findAll();
        }
        return repository.searchByClient(searchTerm.trim());
    }

    @Override
    public List<Client> searchClientByPartial(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findAll();
        }
        return repository.searchClientByPartial(searchTerm.trim());
    }

    private Client convertToEntity(com.clevelanddx.packageintake.dto.ClientDTO dto) {
        Client client = new Client();
        client.setId(dto.getId());
        client.setClient(dto.getClient());
        client.setLastUpdateUser(dto.getLastUpdateUser());
        return client;
    }
} 