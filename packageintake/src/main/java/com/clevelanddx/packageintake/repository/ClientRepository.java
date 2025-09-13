package com.clevelanddx.packageintake.repository;

import com.clevelanddx.packageintake.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByClient(String client);
    List<Client> findByLastUpdateUser(String lastUpdateUser);
    boolean existsByClient(String client);
    
    @Query("SELECT c FROM Client c WHERE LOWER(c.client) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY c.client")
    List<Client> searchByClient(@Param("searchTerm") String searchTerm);

    @Query(value = "SELECT * FROM Inbound_Shipments_Clients WHERE LOWER(Client) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY Client", nativeQuery = true)
    List<Client> searchClientByPartial(@Param("searchTerm") String searchTerm);
} 