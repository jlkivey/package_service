package com.clevelanddx.packageintake.controller;

import com.clevelanddx.packageintake.model.CLIAAdmin;
import com.clevelanddx.packageintake.service.CLIAAdminService;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/clia-admins")
@Tag(name = "CLIA Admins", description = "CLIA Admin management APIs")
public class CLIAAdminController {

    private final CLIAAdminService service;

    @Autowired
    public CLIAAdminController(CLIAAdminService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create CLIA admin")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "CLIA admin created"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<CLIAAdmin> create(@RequestBody CLIAAdmin admin) {
        return new ResponseEntity<>(service.create(admin), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update CLIA admin")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "CLIA admin updated"),
        @ApiResponse(responseCode = "404", description = "CLIA admin not found")
    })
    public ResponseEntity<CLIAAdmin> update(@PathVariable Long id, @RequestBody CLIAAdmin admin) {
        try {
            return ResponseEntity.ok(service.update(id, admin));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete CLIA admin")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "CLIA admin deleted"),
        @ApiResponse(responseCode = "404", description = "CLIA admin not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get CLIA admin by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "CLIA admin found"),
        @ApiResponse(responseCode = "404", description = "CLIA admin not found")
    })
    public ResponseEntity<CLIAAdmin> findById(@PathVariable Long id) {
        Optional<CLIAAdmin> admin = service.findById(id);
        return admin.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all CLIA admins")
    @ApiResponse(responseCode = "200", description = "List of CLIA admins")
    public ResponseEntity<List<CLIAAdmin>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get CLIA admin by user ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "CLIA admin found"),
        @ApiResponse(responseCode = "404", description = "CLIA admin not found")
    })
    public ResponseEntity<CLIAAdmin> findByUserId(@PathVariable String userId) {
        Optional<CLIAAdmin> admin = service.findByUserId(userId);
        return admin.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    @Operation(summary = "Search CLIA admins")
    @ApiResponse(responseCode = "200", description = "List of matching CLIA admins")
    public ResponseEntity<List<CLIAAdmin>> searchAdmins(
            @Parameter(description = "Search term for user ID") 
            @RequestParam String searchTerm) {
        return ResponseEntity.ok(service.searchAdmins(searchTerm));
    }

    @GetMapping("/exists/{userId}")
    @Operation(summary = "Check if CLIA admin exists by user ID")
    @ApiResponse(responseCode = "200", description = "Existence check result")
    public ResponseEntity<Boolean> existsByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(service.existsByUserId(userId));
    }

    @GetMapping("/is-admin/{userId}")
    @Operation(summary = "Check if user is a CLIA admin")
    @ApiResponse(responseCode = "200", description = "True if user is admin, false otherwise")
    public ResponseEntity<Boolean> isAdmin(@PathVariable String userId) {
        return ResponseEntity.ok(service.existsByUserId(userId));
    }
}
