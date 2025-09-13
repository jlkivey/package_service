package com.clevelanddx.packageintake.service;

import com.clevelanddx.packageintake.model.Client;
import java.util.List;
import java.util.Optional;

public interface ClientService {
    Client create(Client client);
    Client update(Long id, Client client);
    void delete(Long id);
    Optional<Client> findById(Long id);
    List<Client> findAll();
    Optional<Client> findByClient(String client);
    List<Client> findByLastUpdateUser(String lastUpdateUser);
    boolean existsByClient(String client);
    List<Client> searchClients(String searchTerm);
    List<Client> searchClientByPartial(String searchTerm);
} 