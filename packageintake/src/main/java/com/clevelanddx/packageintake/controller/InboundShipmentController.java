package com.clevelanddx.packageintake.controller;

import com.clevelanddx.packageintake.dto.InboundShipmentSearchRequest;
import com.clevelanddx.packageintake.dto.InboundShipmentSearchResponse;
import com.clevelanddx.packageintake.model.InboundShipment;
import com.clevelanddx.packageintake.service.InboundShipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/inbound-shipments")
@Tag(name = "Inbound Shipments", description = "APIs for managing inbound shipments")
public class InboundShipmentController {

    private final InboundShipmentService service;

    @Autowired
    public InboundShipmentController(InboundShipmentService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create a new inbound shipment")
    public ResponseEntity<InboundShipment> createShipment(@RequestBody InboundShipment shipment) {
        return new ResponseEntity<>(service.createShipment(shipment), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing inbound shipment")
    public ResponseEntity<InboundShipment> updateShipment(@PathVariable Long id, @RequestBody InboundShipment shipment) {
        try {
            return ResponseEntity.ok(service.updateShipment(id, shipment));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an inbound shipment")
    public ResponseEntity<Void> deleteShipment(@PathVariable Long id) {
        try {
            service.deleteShipment(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an inbound shipment by ID")
    public ResponseEntity<InboundShipment> getShipmentById(@PathVariable Long id) {
        return service.getShipmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all inbound shipments")
    public ResponseEntity<List<InboundShipment>> getAllShipments() {
        return ResponseEntity.ok(service.getAllShipments());
    }

    @GetMapping("/tracking/{trackingNumber}")
    @Operation(summary = "Get an inbound shipment by tracking number")
    public ResponseEntity<InboundShipment> getShipmentByTrackingNumber(@PathVariable String trackingNumber) {
        return service.getShipmentByTrackingNumber(trackingNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/client/{client}")
    @Operation(summary = "Get all inbound shipments for a client")
    public ResponseEntity<List<InboundShipment>> getShipmentsByClient(@PathVariable String client) {
        List<InboundShipment> shipments = service.getShipmentsByClient(client);
        return shipments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(shipments);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get all inbound shipments by status")
    public ResponseEntity<List<InboundShipment>> getShipmentsByStatus(@PathVariable String status) {
        List<InboundShipment> shipments = service.getShipmentsByStatus(status);
        return shipments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(shipments);
    }

    @GetMapping("/order/{orderNumber}")
    @Operation(summary = "Get an inbound shipment by order number")
    public ResponseEntity<InboundShipment> getShipmentByOrderNumber(@PathVariable String orderNumber) {
        return service.getShipmentByOrderNumber(orderNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/scan")
    @Operation(summary = "Update scan time and scanned number for an inbound shipment")
    public ResponseEntity<InboundShipment> updateScanTime(
            @PathVariable Long id,
            @RequestParam(required = false) LocalDateTime scanTime,
            @RequestParam(required = false) String scanUser,
            @RequestParam(required = false) String scannedNumber) {
        try {
            InboundShipment shipment = service.getShipmentById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Shipment not found with id: " + id));
            
            shipment.setScanTime(scanTime != null ? scanTime : LocalDateTime.now());
            shipment.setScanUser(scanUser);
            if (scannedNumber != null) {
                shipment.setScannedNumber(scannedNumber);
            }
            return ResponseEntity.ok(service.updateShipment(id, shipment));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/scanned")
    @Operation(summary = "Get all scanned inbound shipments")
    public ResponseEntity<List<InboundShipment>> getScannedShipments() {
        List<InboundShipment> shipments = service.getAllShipments().stream()
                .filter(shipment -> shipment.getScanTime() != null)
                .toList();
        return shipments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(shipments);
    }

    @GetMapping("/unscanned")
    @Operation(summary = "Get all unscanned inbound shipments")
    public ResponseEntity<List<InboundShipment>> getUnscannedShipments() {
        List<InboundShipment> shipments = service.getAllShipments().stream()
                .filter(shipment -> shipment.getScanTime() == null)
                .toList();
        return shipments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(shipments);
    }

    @GetMapping("/scanned-number/{scannedNumber}")
    @Operation(summary = "Get inbound shipment by scanned number")
    public ResponseEntity<InboundShipment> getShipmentByScannedNumber(
            @Parameter(description = "Scanned number to search for") @PathVariable String scannedNumber) {
        return service.getShipmentByScannedNumber(scannedNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tracking-in-scanned/{scannedNumber}")
    @Operation(summary = "Get inbound shipment by tracking number contained in scanned number")
    public ResponseEntity<InboundShipment> getShipmentByTrackingNumberInScannedNumber(
            @Parameter(description = "Scanned number containing tracking number") @PathVariable String scannedNumber) {
        return service.getShipmentByTrackingNumberInScannedNumber(scannedNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tracking-in-scanned/all/{scannedNumber}")
    @Operation(summary = "Get all inbound shipments where tracking number is contained in scanned number")
    public ResponseEntity<List<InboundShipment>> getAllShipmentsByTrackingNumberInScannedNumber(
            @Parameter(description = "Scanned number containing tracking number") @PathVariable String scannedNumber) {
        List<InboundShipment> shipments = service.getAllShipmentsByTrackingNumberInScannedNumber(scannedNumber);
        return shipments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(shipments);
    }

    @Operation(summary = "Get shipment by scanned number and organization", description = "Retrieves a shipment by its scanned number and organization ID")
    @ApiResponse(responseCode = "200", description = "Shipment found")
    @ApiResponse(responseCode = "404", description = "Shipment not found")
    @GetMapping("/scanned/{scannedNumber}/organization/{organizationId}")
    public ResponseEntity<InboundShipment> getShipmentByScannedNumberAndOrganization(
            @Parameter(description = "Scanned number to search for") @PathVariable String scannedNumber,
            @Parameter(description = "Organization ID") @PathVariable Long organizationId) {
        return service.getShipmentByScannedNumberAndOrganization(scannedNumber, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get shipment by tracking number and organization", description = "Retrieves a shipment by its tracking number and organization ID")
    @ApiResponse(responseCode = "200", description = "Shipment found")
    @ApiResponse(responseCode = "404", description = "Shipment not found")
    @GetMapping("/tracking/{trackingNumber}/organization/{organizationId}")
    public ResponseEntity<InboundShipment> getShipmentByTrackingNumberAndOrganization(
            @Parameter(description = "Tracking number to search for") @PathVariable String trackingNumber,
            @Parameter(description = "Organization ID") @PathVariable Long organizationId) {
        return service.getShipmentByTrackingNumberAndOrganization(trackingNumber, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/today")
    @Operation(summary = "Get all inbound shipments scanned today")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved today's shipments")
    @ApiResponse(responseCode = "204", description = "No shipments found for today")
    public ResponseEntity<List<InboundShipment>> getTodayShipments() {
        List<InboundShipment> shipments = service.getTodayShipments();
        return shipments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(shipments);
    }
    
    @GetMapping("/date/{date}")
    @Operation(summary = "Get all inbound shipments scanned on a specific date")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved shipments for the specified date")
    @ApiResponse(responseCode = "204", description = "No shipments found for the specified date")
    public ResponseEntity<List<InboundShipment>> getShipmentsByDate(
            @Parameter(description = "Date to search for (format: yyyy-MM-dd)") @PathVariable String date) {
        try {
            LocalDate searchDate = LocalDate.parse(date);
            List<InboundShipment> shipments = service.getShipmentsByDate(searchDate);
            return shipments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(shipments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/date-range")
    @Operation(summary = "Get all inbound shipments scanned within a date range")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved shipments for the date range")
    @ApiResponse(responseCode = "204", description = "No shipments found for the date range")
    @ApiResponse(responseCode = "400", description = "Invalid date format or range")
    public ResponseEntity<List<InboundShipment>> getShipmentsByDateRange(
            @Parameter(description = "Start date (format: yyyy-MM-dd)") @RequestParam String fromDate,
            @Parameter(description = "End date (format: yyyy-MM-dd)") @RequestParam String toDate) {
        try {
            LocalDate from = LocalDate.parse(fromDate);
            LocalDate to = LocalDate.parse(toDate);
            
            if (from.isAfter(to)) {
                return ResponseEntity.badRequest().build();
            }
            
            List<InboundShipment> shipments = service.getShipmentsByDateRange(from, to);
            return shipments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(shipments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/recent")
    @Operation(summary = "Get the most recent inbound shipments")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved recent shipments")
    @ApiResponse(responseCode = "204", description = "No shipments found")
    @ApiResponse(responseCode = "400", description = "Invalid limit parameter")
    public ResponseEntity<List<InboundShipment>> getRecentShipments(
            @Parameter(description = "Number of recent shipments to retrieve (default: 10, max: 20000)") 
            @RequestParam(defaultValue = "10") int limit) {
        try {
            // Validate limit parameter
            if (limit <= 0 || limit > 20000) {
                return ResponseEntity.badRequest().build();
            }
            
            List<InboundShipment> shipments = service.getRecentShipments(limit);
            return shipments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(shipments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/search")
    @Operation(summary = "Search inbound shipments with multiple criteria", 
               description = "Search shipments by tracking number, scanned number, status, order number, lab, scan user, and date ranges (ship date, scan date, email receive datetime, last update datetime)")
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    @ApiResponse(responseCode = "400", description = "Invalid search parameters")
    public ResponseEntity<InboundShipmentSearchResponse> searchShipments(
            @Parameter(description = "Search criteria including string fields for partial matching and date ranges") 
            @RequestBody InboundShipmentSearchRequest searchRequest) {
        try {
            // Validate pagination parameters
            if (searchRequest.getPage() < 0) {
                searchRequest.setPage(0);
            }
            if (searchRequest.getSize() <= 0 || searchRequest.getSize() > 1000) {
                searchRequest.setSize(20);
            }
            
            InboundShipmentSearchResponse response = service.searchShipments(searchRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 