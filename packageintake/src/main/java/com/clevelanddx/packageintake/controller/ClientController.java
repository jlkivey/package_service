package com.clevelanddx.packageintake.controller;

import com.clevelanddx.packageintake.model.Client;
import com.clevelanddx.packageintake.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@Tag(name = "Clients", description = "Client management APIs")
public class ClientController {

    private final ClientService service;

    @Autowired
    public ClientController(ClientService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create client")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Client created"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<Client> create(@RequestBody Client client) {
        return new ResponseEntity<>(service.create(client), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update client")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Client updated"),
        @ApiResponse(responseCode = "404", description = "Client not found")
    })
    public ResponseEntity<Client> update(@PathVariable Long id, @RequestBody Client client) {
        try {
            return ResponseEntity.ok(service.update(id, client));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete client")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Client deleted"),
        @ApiResponse(responseCode = "404", description = "Client not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id:[0-9]+}")
    @Operation(summary = "Get client by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Client found"),
        @ApiResponse(responseCode = "404", description = "Client not found")
    })
    public ResponseEntity<Client> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all clients")
    public ResponseEntity<List<Client>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/client/{client}")
    @Operation(summary = "Get client by client name")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Client found"),
        @ApiResponse(responseCode = "404", description = "Client not found")
    })
    public ResponseEntity<Client> findByClient(@PathVariable String client) {
        return service.findByClient(client)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{username}")
    @Operation(summary = "Get clients by last update user!")
    public ResponseEntity<List<Client>> findByLastUpdateUser(@PathVariable String username) {
        List<Client> clients = service.findByLastUpdateUser(username);
        return clients.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(clients);
    }

    @GetMapping("/exists/{client}")
    @Operation(summary = "Check if client exists")
    public ResponseEntity<Boolean> existsByClient(@PathVariable String client) {
        return ResponseEntity.ok(service.existsByClient(client));
    }

    @GetMapping("/search")
    @Operation(summary = "Search clients by partial name match -- this changed")
    public ResponseEntity<List<Client>> searchClients(@RequestParam String searchTerm) {
        List<Client> clients = service.searchClients(searchTerm);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/search/partial")
    @Operation(summary = "Search clients by partial name in Inbound Shipments Clients table")
    @ApiResponse(responseCode = "200", description = "List of clients found")
    public ResponseEntity<List<Client>> searchClientByPartial(@RequestParam(required = false) String partialName) {
        List<Client> clients = service.searchClientByPartial(partialName);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/searchByPartialName")
    @Operation(summary = "Search clients by partial name (legacy endpoint)")
    @ApiResponse(responseCode = "200", description = "List of clients found")
    public ResponseEntity<List<Client>> searchByPartialNameLegacy(@RequestParam String partialName) {
        List<Client> clients = service.searchClientByPartial(partialName);
        return ResponseEntity.ok(clients);
    }
} 