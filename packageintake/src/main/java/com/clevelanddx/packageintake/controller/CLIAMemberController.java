package com.clevelanddx.packageintake.controller;

import com.clevelanddx.packageintake.model.CLIAMember;
import com.clevelanddx.packageintake.service.CLIAMemberService;
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
@RequestMapping("/api/clia-members")
@Tag(name = "CLIA Members", description = "CLIA Member management APIs")
public class CLIAMemberController {

    private final CLIAMemberService service;

    @Autowired
    public CLIAMemberController(CLIAMemberService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create CLIA member")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "CLIA member created"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<CLIAMember> create(@RequestBody CLIAMember member) {
        return new ResponseEntity<>(service.create(member), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update CLIA member")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "CLIA member updated"),
        @ApiResponse(responseCode = "404", description = "CLIA member not found")
    })
    public ResponseEntity<CLIAMember> update(@PathVariable Long id, @RequestBody CLIAMember member) {
        try {
            return ResponseEntity.ok(service.update(id, member));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete CLIA member")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "CLIA member deleted"),
        @ApiResponse(responseCode = "404", description = "CLIA member not found")
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
    @Operation(summary = "Get CLIA member by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "CLIA member found"),
        @ApiResponse(responseCode = "404", description = "CLIA member not found")
    })
    public ResponseEntity<CLIAMember> findById(@PathVariable Long id) {
        Optional<CLIAMember> member = service.findById(id);
        return member.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all CLIA members")
    @ApiResponse(responseCode = "200", description = "List of CLIA members")
    public ResponseEntity<List<CLIAMember>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get CLIA member by user ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "CLIA member found"),
        @ApiResponse(responseCode = "404", description = "CLIA member not found")
    })
    public ResponseEntity<CLIAMember> findByUserId(@PathVariable String userId) {
        Optional<CLIAMember> member = service.findByUserId(userId);
        return member.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    @Operation(summary = "Search CLIA members")
    @ApiResponse(responseCode = "200", description = "List of matching CLIA members")
    public ResponseEntity<List<CLIAMember>> searchMembers(
            @Parameter(description = "Search term for user ID") 
            @RequestParam String searchTerm) {
        return ResponseEntity.ok(service.searchMembers(searchTerm));
    }

    @GetMapping("/exists/{userId}")
    @Operation(summary = "Check if CLIA member exists by user ID")
    @ApiResponse(responseCode = "200", description = "Existence check result")
    public ResponseEntity<Boolean> existsByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(service.existsByUserId(userId));
    }
}
