package com.clevelanddx.packageintake.service;

import com.clevelanddx.packageintake.model.InboundShipment;
import com.clevelanddx.packageintake.model.InboundShipmentReference;
import com.clevelanddx.packageintake.repository.InboundShipmentRepository;
import com.clevelanddx.packageintake.service.InboundShipmentReferenceService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InboundShipmentServiceImpl implements InboundShipmentService {

    private final InboundShipmentRepository repository;
    private final InboundShipmentReferenceService referenceService;

    @Autowired
    public InboundShipmentServiceImpl(InboundShipmentRepository repository, InboundShipmentReferenceService referenceService) {
        this.repository = repository;
        this.referenceService = referenceService;
    }

    @Override
    public InboundShipment createShipment(InboundShipment shipment) {
        // Handle shipmentType reference properly - only establish references to existing ones
        if (shipment.getShipmentType() != null) {
            // If shipmentType has an ID, verify it exists
            if (shipment.getShipmentType().getRowId() != null) {
                Optional<InboundShipmentReference> existingReference = 
                    referenceService.getReferenceById(shipment.getShipmentType().getRowId());
                if (existingReference.isPresent()) {
                    shipment.setShipmentType(existingReference.get());
                } else {
                    // If reference doesn't exist, set to null to avoid error
                    shipment.setShipmentType(null);
                }
            } else {
                // If shipmentType has no ID but has type/value, try to find existing reference
                if (shipment.getShipmentType().getType() != null && shipment.getShipmentType().getValue() != null) {
                    Optional<InboundShipmentReference> existingReference = 
                        referenceService.findByTypeAndValue(shipment.getShipmentType().getType(), shipment.getShipmentType().getValue());
                    if (existingReference.isPresent()) {
                        shipment.setShipmentType(existingReference.get());
                    } else {
                        // If no existing reference found, set to null - don't create new ones during shipment creation
                        shipment.setShipmentType(null);
                    }
                } else {
                    // If no identifying information, set to null
                    shipment.setShipmentType(null);
                }
            }
        }
        
        shipment.setLastUpdateDatetime(LocalDateTime.now());
        if (shipment.getScanTime() == null) {
            shipment.setScanTime(LocalDateTime.now());
        }
        return repository.save(shipment);
    }

    @Override
    public InboundShipment updateShipment(Long id, InboundShipment shipment) {
        return repository.findById(id)
                .map(existingShipment -> {
                    // Handle shipmentType reference properly
                    if (shipment.getShipmentType() != null) {
                        // If shipmentType has an ID, try to find the existing reference
                        if (shipment.getShipmentType().getRowId() != null) {
                            Optional<InboundShipmentReference> existingReference = 
                                referenceService.getReferenceById(shipment.getShipmentType().getRowId());
                            if (existingReference.isPresent()) {
                                shipment.setShipmentType(existingReference.get());
                            } else {
                                // If reference doesn't exist, set to null to avoid error
                                shipment.setShipmentType(null);
                            }
                        } else {
                            // If shipmentType has no ID but has type/value, try to find existing or create new
                            if (shipment.getShipmentType().getType() != null && shipment.getShipmentType().getValue() != null) {
                                InboundShipmentReference foundOrCreated = referenceService.findOrCreateReference(
                                    shipment.getShipmentType().getType(), 
                                    shipment.getShipmentType().getValue(),
                                    shipment.getShipmentType().getDescription()
                                );
                                shipment.setShipmentType(foundOrCreated);
                            } else {
                                // If no identifying information, set to null
                                shipment.setShipmentType(null);
                            }
                        }
                    }
                    
                    shipment.setRowId(id);
                    shipment.setLastUpdateDatetime(LocalDateTime.now());
                    if (shipment.getScanTime() == null) {
                        shipment.setScanTime(existingShipment.getScanTime());
                    }
                    return repository.save(shipment);
                })
                .orElseThrow(() -> new EntityNotFoundException("Shipment not found with id: " + id));
    }

    @Override
    public void deleteShipment(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Shipment not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public Optional<InboundShipment> getShipmentById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<InboundShipment> getAllShipments() {
        return repository.findAll();
    }

    @Override
    public Optional<InboundShipment> getShipmentByTrackingNumber(String trackingNumber) {
        return repository.findByTrackingNumber(trackingNumber);
    }

    @Override
    public List<InboundShipment> getShipmentsByClient(String client) {
        return repository.findByClient(client);
    }

    @Override
    public List<InboundShipment> getShipmentsByStatus(String status) {
        return repository.findByStatus(status);
    }

    @Override
    public Optional<InboundShipment> getShipmentByOrderNumber(String orderNumber) {
        return repository.findByOrderNumber(orderNumber);
    }

    @Override
    public Optional<InboundShipment> getShipmentByScannedNumber(String scannedNumber) {
        return repository.findByScannedNumber(scannedNumber);
    }

    @Override
    public Optional<InboundShipment> getShipmentByTrackingNumberInScannedNumber(String scannedNumber) {
        return repository.findFirstByTrackingNumberInScannedNumber(scannedNumber);
    }

    @Override
    public List<InboundShipment> getAllShipmentsByTrackingNumberInScannedNumber(String scannedNumber) {
        return repository.findAllByTrackingNumberInScannedNumber(scannedNumber);
    }

    @Override
    public Optional<InboundShipment> getShipmentByScannedNumberAndOrganization(String scannedNumber, Long organizationId) {
        return repository.findByScannedNumberAndOrganization(scannedNumber, organizationId);
    }

    @Override
    public Optional<InboundShipment> getShipmentByTrackingNumberAndOrganization(String trackingNumber, Long organizationId) {
        return repository.findByTrackingNumberAndOrganization(trackingNumber, organizationId);
    }

    @Override
    public InboundShipment updateScanTime(Long id, String scannedNumber) {
        return repository.findById(id)
                .map(existingShipment -> {
                    existingShipment.setScanTime(LocalDateTime.now());
                    existingShipment.setScannedNumber(scannedNumber);
                    return repository.save(existingShipment);
                })
                .orElseThrow(() -> new EntityNotFoundException("Shipment not found with id: " + id));
    }
    
    @Override
    public List<InboundShipment> getTodayShipments() {
        return repository.findTodayShipments();
    }
    
    @Override
    public List<InboundShipment> getShipmentsByDate(LocalDate date) {
        return repository.findByScanDate(date);
    }
    
    @Override
    public List<InboundShipment> getShipmentsByDateRange(LocalDate fromDate, LocalDate toDate) {
        return repository.findByScanDateRange(fromDate, toDate);
    }
    
    @Override
    public List<InboundShipment> getRecentShipments(int limit) {
        return repository.findRecentShipments(limit);
    }
} 