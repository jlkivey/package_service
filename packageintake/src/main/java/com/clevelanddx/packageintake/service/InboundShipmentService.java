package com.clevelanddx.packageintake.service;

import com.clevelanddx.packageintake.dto.InboundShipmentSearchRequest;
import com.clevelanddx.packageintake.dto.InboundShipmentSearchResponse;
import com.clevelanddx.packageintake.model.InboundShipment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InboundShipmentService {
    InboundShipment createShipment(InboundShipment shipment);
    InboundShipment updateShipment(Long id, InboundShipment shipment);
    void deleteShipment(Long id);
    Optional<InboundShipment> getShipmentById(Long id);
    List<InboundShipment> getAllShipments();
    Optional<InboundShipment> getShipmentByTrackingNumber(String trackingNumber);
    List<InboundShipment> getShipmentsByClient(String client);
    List<InboundShipment> getShipmentsByStatus(String status);
    Optional<InboundShipment> getShipmentByOrderNumber(String orderNumber);
    Optional<InboundShipment> getShipmentByScannedNumber(String scannedNumber);
    Optional<InboundShipment> getShipmentByTrackingNumberInScannedNumber(String scannedNumber);
    List<InboundShipment> getAllShipmentsByTrackingNumberInScannedNumber(String scannedNumber);
    Optional<InboundShipment> getShipmentByScannedNumberAndOrganization(String scannedNumber, Long organizationId);
    Optional<InboundShipment> getShipmentByTrackingNumberAndOrganization(String trackingNumber, Long organizationId);
    InboundShipment updateScanTime(Long id, String scannedNumber);
    
    // New date-based methods
    List<InboundShipment> getTodayShipments();
    List<InboundShipment> getShipmentsByDate(LocalDate date);
    List<InboundShipment> getShipmentsByDateRange(LocalDate fromDate, LocalDate toDate);
    
    // Recent shipments method
    List<InboundShipment> getRecentShipments(int limit);
    
    // Search method
    InboundShipmentSearchResponse searchShipments(InboundShipmentSearchRequest searchRequest);
    
    // Distinct list methods with caching
    List<String> getDistinctScanUsers();
    List<String> getDistinctStatuses();
} 