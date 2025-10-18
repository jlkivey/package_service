package com.clevelanddx.packageintake.service;

import com.clevelanddx.packageintake.dto.InboundShipmentSearchRequest;
import com.clevelanddx.packageintake.dto.InboundShipmentSearchRequestV2;
import com.clevelanddx.packageintake.dto.InboundShipmentSearchResponse;
import com.clevelanddx.packageintake.model.InboundShipment;
import com.clevelanddx.packageintake.model.InboundShipmentReference;
import com.clevelanddx.packageintake.repository.InboundShipmentRepository;
import com.clevelanddx.packageintake.repository.InboundShipmentRepositoryImpl;
import com.clevelanddx.packageintake.service.InboundShipmentReferenceService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InboundShipmentServiceImpl implements InboundShipmentService {

    private static final Logger log = LoggerFactory.getLogger(InboundShipmentServiceImpl.class);
    
    private final InboundShipmentRepository repository;
    private final InboundShipmentRepositoryImpl repositoryImpl;
    private final InboundShipmentReferenceService referenceService;

    @Autowired
    public InboundShipmentServiceImpl(InboundShipmentRepository repository, InboundShipmentRepositoryImpl repositoryImpl, InboundShipmentReferenceService referenceService) {
        this.repository = repository;
        this.repositoryImpl = repositoryImpl;
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
    
    @Override
    public InboundShipmentSearchResponse searchShipments(InboundShipmentSearchRequest searchRequest) {
        // Create pageable object
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize());
        
        // Call repository search method
        Page<InboundShipment> page = repository.searchShipments(
            searchRequest.getTrackingNumber(),
            searchRequest.getScannedNumber(),
            searchRequest.getStatus(),
            searchRequest.getOrderNumber(),
            searchRequest.getLab(),
            searchRequest.getScanUser(),
            searchRequest.getShipDateFrom(),
            searchRequest.getShipDateTo(),
            searchRequest.getScanDateFrom(),
            searchRequest.getScanDateTo(),
            searchRequest.getEmailReceiveDatetimeFrom(),
            searchRequest.getEmailReceiveDatetimeTo(),
            searchRequest.getLastUpdateDatetimeFrom(),
            searchRequest.getLastUpdateDatetimeTo(),
            pageable
        );
        
        // Build response
        return InboundShipmentSearchResponse.builder()
            .shipments(page.getContent())
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .currentPage(page.getNumber())
            .pageSize(page.getSize())
            .hasNext(page.hasNext())
            .hasPrevious(page.hasPrevious())
            .build();
    }
    
    @Override
    public InboundShipmentSearchResponse searchShipmentsV2(InboundShipmentSearchRequestV2 searchRequest) {
        log.debug("=== V2 Search Started ===");
        log.debug("Search Request Parameters:");
        log.debug("  - trackingNumber: {}", searchRequest.getTrackingNumber());
        log.debug("  - scannedNumber: {}", searchRequest.getScannedNumber());
        log.debug("  - status: {}", searchRequest.getStatus());
        log.debug("  - orderNumber: {}", searchRequest.getOrderNumber());
        log.debug("  - lab: {}", searchRequest.getLab());
        log.debug("  - scanUser: {}", searchRequest.getScanUser());
        log.debug("  - clientName: {}", searchRequest.getClientName());
        log.debug("  - shipDateFrom: {}", searchRequest.getShipDateFrom());
        log.debug("  - shipDateTo: {}", searchRequest.getShipDateTo());
        log.debug("  - scanDateFrom: {}", searchRequest.getScanDateFrom());
        log.debug("  - scanDateTo: {}", searchRequest.getScanDateTo());
        log.debug("  - emailReceiveDatetimeFrom: {}", searchRequest.getEmailReceiveDatetimeFrom());
        log.debug("  - emailReceiveDatetimeTo: {}", searchRequest.getEmailReceiveDatetimeTo());
        log.debug("  - lastUpdateDatetimeFrom: {}", searchRequest.getLastUpdateDatetimeFrom());
        log.debug("  - lastUpdateDatetimeTo: {}", searchRequest.getLastUpdateDatetimeTo());
        log.debug("  - page: {}", searchRequest.getPage());
        log.debug("  - size: {}", searchRequest.getSize());
        
        // Create pageable object
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize());
        log.debug("Created Pageable: page={}, size={}", searchRequest.getPage(), searchRequest.getSize());
        
        log.debug("Calling repositoryImpl.searchShipmentsV2WithLogging()...");
        // Call repository V2 search method with logging
        Page<InboundShipment> page = repositoryImpl.searchShipmentsV2WithLogging(
            searchRequest.getTrackingNumber(),
            searchRequest.getScannedNumber(),
            searchRequest.getStatus(),
            searchRequest.getOrderNumber(),
            searchRequest.getLab(),
            searchRequest.getScanUser(),
            searchRequest.getClientName(),
            searchRequest.getShipDateFrom(),
            searchRequest.getShipDateTo(),
            searchRequest.getScanDateFrom(),
            searchRequest.getScanDateTo(),
            searchRequest.getEmailReceiveDatetimeFrom(),
            searchRequest.getEmailReceiveDatetimeTo(),
            searchRequest.getLastUpdateDatetimeFrom(),
            searchRequest.getLastUpdateDatetimeTo(),
            pageable
        );
        
        log.debug("Repository returned: {} results, total elements: {}, total pages: {}", 
                 page.getContent().size(), page.getTotalElements(), page.getTotalPages());
        
        // Build response
        InboundShipmentSearchResponse response = InboundShipmentSearchResponse.builder()
            .shipments(page.getContent())
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .currentPage(page.getNumber())
            .pageSize(page.getSize())
            .hasNext(page.hasNext())
            .hasPrevious(page.hasPrevious())
            .build();
            
        log.debug("=== V2 Search Completed ===");
        return response;
    }
    
    @Override
    @Cacheable(value = "scanUsers", key = "'all'")
    public List<String> getDistinctScanUsers() {
        return repository.findDistinctScanUsers();
    }

    @Override
    @Cacheable(value = "statuses", key = "'all'")
    public List<String> getDistinctStatuses() {
        return repository.findDistinctStatuses();
    }
    
    @CacheEvict(value = {"scanUsers", "statuses"}, allEntries = true)
    public void evictDistinctListsCache() {
        // This method will clear the cache for both scan users and statuses
        // The @CacheEvict annotation handles the actual cache clearing
    }
} 