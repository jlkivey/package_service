package com.clevelanddx.packageintake.controller;

import com.clevelanddx.packageintake.model.InboundShipmentReference;
import com.clevelanddx.packageintake.service.InboundShipmentReferenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inbound-shipment-references")
@Tag(name = "Inbound Shipment References", description = "APIs for managing inbound shipment reference data")
public class InboundShipmentReferenceController {

    private final InboundShipmentReferenceService service;

    @Autowired
    public InboundShipmentReferenceController(InboundShipmentReferenceService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get all inbound shipment references")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all references")
    public ResponseEntity<List<InboundShipmentReference>> getAllReferences() {
        return ResponseEntity.ok(service.getAllReferences());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an inbound shipment reference by ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the reference")
    @ApiResponse(responseCode = "404", description = "Reference not found")
    public ResponseEntity<InboundShipmentReference> getReferenceById(
            @Parameter(description = "ID of the reference to retrieve") @PathVariable Long id) {
        return service.getReferenceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get all inbound shipment references by type")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved references by type")
    @ApiResponse(responseCode = "204", description = "No references found for the specified type")
    public ResponseEntity<List<InboundShipmentReference>> getReferencesByType(
            @Parameter(description = "Type of references to retrieve") @PathVariable String type) {
        List<InboundShipmentReference> references = service.getReferencesByType(type);
        return references.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(references);
    }

    @PostMapping
    @Operation(summary = "Create a new inbound shipment reference")
    @ApiResponse(responseCode = "201", description = "Successfully created the reference")
    public ResponseEntity<InboundShipmentReference> createReference(@RequestBody InboundShipmentReference reference) {
        InboundShipmentReference createdReference = service.createReference(reference);
        return ResponseEntity.status(201).body(createdReference);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing inbound shipment reference")
    @ApiResponse(responseCode = "200", description = "Successfully updated the reference")
    @ApiResponse(responseCode = "404", description = "Reference not found")
    public ResponseEntity<InboundShipmentReference> updateReference(
            @Parameter(description = "ID of the reference to update") @PathVariable Long id,
            @RequestBody InboundShipmentReference reference) {
        try {
            InboundShipmentReference updatedReference = service.updateReference(id, reference);
            return ResponseEntity.ok(updatedReference);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an inbound shipment reference")
    @ApiResponse(responseCode = "204", description = "Successfully deleted the reference")
    @ApiResponse(responseCode = "404", description = "Reference not found")
    public ResponseEntity<Void> deleteReference(
            @Parameter(description = "ID of the reference to delete") @PathVariable Long id) {
        try {
            service.deleteReference(id);
            return ResponseEntity.noContent().build();
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/find-or-create")
    @Operation(summary = "Find an existing reference or create a new one")
    @ApiResponse(responseCode = "200", description = "Successfully found or created the reference")
    public ResponseEntity<InboundShipmentReference> findOrCreateReference(
            @RequestParam String type,
            @RequestParam String value,
            @RequestParam(required = false) String description) {
        InboundShipmentReference reference = service.findOrCreateReference(type, value, description);
        return ResponseEntity.ok(reference);
    }

    @GetMapping("/type/{type}/value/{value}")
    @Operation(summary = "Get an inbound shipment reference by type and value")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the reference")
    @ApiResponse(responseCode = "404", description = "Reference not found")
    public ResponseEntity<InboundShipmentReference> getReferenceByTypeAndValue(
            @Parameter(description = "Type of the reference") @PathVariable String type,
            @Parameter(description = "Value of the reference") @PathVariable String value) {
        return service.findByTypeAndValue(type, value)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
