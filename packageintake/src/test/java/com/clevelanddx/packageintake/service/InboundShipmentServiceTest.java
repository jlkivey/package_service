package com.clevelanddx.packageintake.service;

import com.clevelanddx.packageintake.dto.InboundShipmentSearchRequest;
import com.clevelanddx.packageintake.dto.InboundShipmentSearchResponse;
import com.clevelanddx.packageintake.model.InboundShipment;
import com.clevelanddx.packageintake.model.Client;
import com.clevelanddx.packageintake.model.InboundShipmentReference;
import com.clevelanddx.packageintake.repository.InboundShipmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InboundShipmentServiceTest {

    @Mock
    private InboundShipmentRepository repository;

    @Mock
    private InboundShipmentReferenceService referenceService;

    @InjectMocks
    private InboundShipmentServiceImpl service;

    private InboundShipment testShipment;
    private InboundShipmentReference testShipmentType;

    @BeforeEach
    void setUp() {
        testShipmentType = InboundShipmentReference.builder()
                .rowId(1L)
                .type("SHIPPING_TYPE")
                .value("Dry Ice")
                .description(null)
                .build();

        testShipment = InboundShipment.builder()
                .rowId(1L)
                .client("Test Client")
                .trackingNumber("TRACK123")
                .scannedNumber("SCAN123")
                .status("Pending")
                .emailId("test@example.com")
                .orderNumber("ORDER123")
                .shipDate(LocalDate.now())
                .lab("Test Lab")
                .weight("10kg")
                .numberOfSamples("5")
                .pickupTime("10:00 AM")
                .pickupTime2("2:00 PM")
                .emailReceiveDatetime(LocalDateTime.now())
                .lastUpdateDatetime(LocalDateTime.now())
                .scanTime(LocalDateTime.now())
                .scanUser("TEST_USER")
                .shipmentType(testShipmentType)
                .build();
    }

    @Test
    void createShipment_ShouldReturnSavedShipment() {
        when(repository.save(any(InboundShipment.class))).thenReturn(testShipment);

        InboundShipment result = service.createShipment(testShipment);

        assertNotNull(result);
        assertEquals(testShipment.getTrackingNumber(), result.getTrackingNumber());
        verify(repository).save(any(InboundShipment.class));
    }

    @Test
    void createShipment_WithShipmentTypeId_ShouldFindExistingReference() {
        when(referenceService.getReferenceById(1L)).thenReturn(Optional.of(testShipmentType));
        when(repository.save(any(InboundShipment.class))).thenReturn(testShipment);

        InboundShipment result = service.createShipment(testShipment);

        assertNotNull(result);
        verify(referenceService).getReferenceById(1L);
        verify(repository).save(any(InboundShipment.class));
    }

    @Test
    void createShipment_WithShipmentTypeIdNotFound_ShouldSetToNull() {
        when(referenceService.getReferenceById(1L)).thenReturn(Optional.empty());
        when(repository.save(any(InboundShipment.class))).thenReturn(testShipment);

        InboundShipment result = service.createShipment(testShipment);

        assertNotNull(result);
        verify(referenceService).getReferenceById(1L);
        verify(repository).save(any(InboundShipment.class));
    }

    @Test
    void createShipment_WithShipmentTypeNoIdButTypeValue_ShouldFindExistingReference() {
        InboundShipmentReference newShipmentType = InboundShipmentReference.builder()
                .type("EXISTING_TYPE")
                .value("EXISTING_VALUE")
                .description("Existing Description")
                .build();
        
        when(referenceService.findByTypeAndValue("EXISTING_TYPE", "EXISTING_VALUE"))
                .thenReturn(Optional.of(newShipmentType));
        when(repository.save(any(InboundShipment.class))).thenReturn(testShipment);

        InboundShipment shipmentToCreate = InboundShipment.builder()
                .client("Test Client")
                .trackingNumber("TRACK123")
                .scannedNumber("SCAN123")
                .status("Pending")
                .emailId("test@example.com")
                .orderNumber("ORDER123")
                .shipDate(LocalDate.now())
                .lab("Test Lab")
                .weight("10kg")
                .numberOfSamples("5")
                .pickupTime("10:00 AM")
                .pickupTime2("2:00 PM")
                .emailReceiveDatetime(LocalDateTime.now())
                .scanTime(LocalDateTime.now())
                .scanUser("TEST_USER")
                .shipmentType(newShipmentType)
                .build();

        InboundShipment result = service.createShipment(shipmentToCreate);

        assertNotNull(result);
        verify(referenceService).findByTypeAndValue("EXISTING_TYPE", "EXISTING_VALUE");
        verify(repository).save(any(InboundShipment.class));
    }

    @Test
    void createShipment_WithShipmentTypeNoIdButTypeValueNotFound_ShouldSetToNull() {
        InboundShipmentReference newShipmentType = InboundShipmentReference.builder()
                .type("NONEXISTENT_TYPE")
                .value("NONEXISTENT_VALUE")
                .description("Non-existent Description")
                .build();
        
        when(referenceService.findByTypeAndValue("NONEXISTENT_TYPE", "NONEXISTENT_VALUE"))
                .thenReturn(Optional.empty());
        when(repository.save(any(InboundShipment.class))).thenReturn(testShipment);

        InboundShipment shipmentToCreate = InboundShipment.builder()
                .client("Test Client")
                .trackingNumber("TRACK123")
                .scannedNumber("SCAN123")
                .status("Pending")
                .emailId("test@example.com")
                .orderNumber("ORDER123")
                .shipDate(LocalDate.now())
                .lab("Test Lab")
                .weight("10kg")
                .numberOfSamples("5")
                .pickupTime("10:00 AM")
                .pickupTime2("2:00 PM")
                .emailReceiveDatetime(LocalDateTime.now())
                .scanTime(LocalDateTime.now())
                .scanUser("TEST_USER")
                .shipmentType(newShipmentType)
                .build();

        InboundShipment result = service.createShipment(shipmentToCreate);

        assertNotNull(result);
        verify(referenceService).findByTypeAndValue("NONEXISTENT_TYPE", "NONEXISTENT_VALUE");
        verify(repository).save(any(InboundShipment.class));
    }

    @Test
    void createShipment_WithShipmentTypeNoIdAndNoTypeValue_ShouldSetToNull() {
        InboundShipmentReference invalidShipmentType = InboundShipmentReference.builder()
                .description("Only Description")
                .build();
        
        when(repository.save(any(InboundShipment.class))).thenReturn(testShipment);

        InboundShipment shipmentToCreate = InboundShipment.builder()
                .client("Test Client")
                .trackingNumber("TRACK123")
                .scannedNumber("SCAN123")
                .status("Pending")
                .emailId("test@example.com")
                .orderNumber("ORDER123")
                .shipDate(LocalDate.now())
                .lab("Test Lab")
                .weight("10kg")
                .numberOfSamples("5")
                .pickupTime("10:00 AM")
                .pickupTime2("2:00 PM")
                .emailReceiveDatetime(LocalDateTime.now())
                .scanTime(LocalDateTime.now())
                .scanUser("TEST_USER")
                .shipmentType(invalidShipmentType)
                .build();

        InboundShipment result = service.createShipment(shipmentToCreate);

        assertNotNull(result);
        verify(repository).save(any(InboundShipment.class));
    }

    @Test
    void createShipment_WithNullShipmentType_ShouldProceedNormally() {
        testShipment.setShipmentType(null);
        when(repository.save(any(InboundShipment.class))).thenReturn(testShipment);

        InboundShipment result = service.createShipment(testShipment);

        assertNotNull(result);
        verify(repository).save(any(InboundShipment.class));
    }

    @Test
    void updateShipment_WhenExists_ShouldReturnUpdatedShipment() {
        when(repository.findById(1L)).thenReturn(Optional.of(testShipment));
        when(repository.save(any(InboundShipment.class))).thenReturn(testShipment);

        InboundShipment result = service.updateShipment(1L, testShipment);

        assertNotNull(result);
        assertEquals(testShipment.getTrackingNumber(), result.getTrackingNumber());
        verify(repository).save(any(InboundShipment.class));
    }

    @Test
    void updateShipment_WithShipmentTypeId_ShouldFindExistingReference() {
        when(repository.findById(1L)).thenReturn(Optional.of(testShipment));
        when(referenceService.getReferenceById(1L)).thenReturn(Optional.of(testShipmentType));
        when(repository.save(any(InboundShipment.class))).thenReturn(testShipment);

        InboundShipment result = service.updateShipment(1L, testShipment);

        assertNotNull(result);
        verify(referenceService).getReferenceById(1L);
        verify(repository).save(any(InboundShipment.class));
    }

    @Test
    void updateShipment_WithShipmentTypeIdNotFound_ShouldSetToNull() {
        when(repository.findById(1L)).thenReturn(Optional.of(testShipment));
        when(referenceService.getReferenceById(1L)).thenReturn(Optional.empty());
        when(repository.save(any(InboundShipment.class))).thenReturn(testShipment);

        InboundShipment result = service.updateShipment(1L, testShipment);

        assertNotNull(result);
        verify(referenceService).getReferenceById(1L);
        verify(repository).save(any(InboundShipment.class));
    }

    @Test
    void updateShipment_WithShipmentTypeNoIdButTypeValue_ShouldFindOrCreateReference() {
        InboundShipmentReference newShipmentType = InboundShipmentReference.builder()
                .type("NEW_TYPE")
                .value("NEW_VALUE")
                .description("New Description")
                .build();
        
        when(repository.findById(1L)).thenReturn(Optional.of(testShipment));
        when(referenceService.findOrCreateReference("NEW_TYPE", "NEW_VALUE", "New Description"))
                .thenReturn(newShipmentType);
        when(repository.save(any(InboundShipment.class))).thenReturn(testShipment);

        InboundShipment shipmentToUpdate = InboundShipment.builder()
                .rowId(1L)
                .client("Test Client")
                .trackingNumber("TRACK123")
                .scannedNumber("SCAN123")
                .status("Pending")
                .emailId("test@example.com")
                .orderNumber("ORDER123")
                .shipDate(LocalDate.now())
                .lab("Test Lab")
                .weight("10kg")
                .numberOfSamples("5")
                .pickupTime("10:00 AM")
                .pickupTime2("2:00 PM")
                .emailReceiveDatetime(LocalDateTime.now())
                .lastUpdateDatetime(LocalDateTime.now())
                .scanTime(LocalDateTime.now())
                .scanUser("TEST_USER")
                .shipmentType(newShipmentType)
                .build();

        InboundShipment result = service.updateShipment(1L, shipmentToUpdate);

        assertNotNull(result);
        verify(referenceService).findOrCreateReference("NEW_TYPE", "NEW_VALUE", "New Description");
        verify(repository).save(any(InboundShipment.class));
    }

    @Test
    void updateShipment_WhenNotExists_ShouldThrowException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.updateShipment(1L, testShipment));
    }

    @Test
    void deleteShipment_WhenExists_ShouldDelete() {
        when(repository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> service.deleteShipment(1L));
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteShipment_WhenNotExists_ShouldThrowException() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> service.deleteShipment(1L));
    }

    @Test
    void getShipmentById_WhenExists_ShouldReturnShipment() {
        when(repository.findById(1L)).thenReturn(Optional.of(testShipment));

        Optional<InboundShipment> result = service.getShipmentById(1L);

        assertTrue(result.isPresent());
        assertEquals(testShipment.getTrackingNumber(), result.get().getTrackingNumber());
    }

    @Test
    void getAllShipments_ShouldReturnList() {
        List<InboundShipment> shipments = Arrays.asList(testShipment);
        when(repository.findAll()).thenReturn(shipments);

        List<InboundShipment> result = service.getAllShipments();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void getShipmentByTrackingNumber_WhenExists_ShouldReturnShipment() {
        when(repository.findByTrackingNumber("TRACK123")).thenReturn(Optional.of(testShipment));

        Optional<InboundShipment> result = service.getShipmentByTrackingNumber("TRACK123");

        assertTrue(result.isPresent());
        assertEquals("TRACK123", result.get().getTrackingNumber());
    }

    @Test
    void getShipmentsByClient_ShouldReturnList() {
        List<InboundShipment> shipments = Arrays.asList(testShipment);
        when(repository.findByClient("Test Client")).thenReturn(shipments);

        List<InboundShipment> result = service.getShipmentsByClient("Test Client");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void getShipmentsByStatus_ShouldReturnList() {
        List<InboundShipment> shipments = Arrays.asList(testShipment);
        when(repository.findByStatus("Pending")).thenReturn(shipments);

        List<InboundShipment> result = service.getShipmentsByStatus("Pending");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void getShipmentByOrderNumber_WhenExists_ShouldReturnShipment() {
        when(repository.findByOrderNumber("ORDER123")).thenReturn(Optional.of(testShipment));

        Optional<InboundShipment> result = service.getShipmentByOrderNumber("ORDER123");

        assertTrue(result.isPresent());
        assertEquals("ORDER123", result.get().getOrderNumber());
    }

    @Test
    void getShipmentByScannedNumber_WhenExists_ShouldReturnShipment() {
        when(repository.findByScannedNumber("SCAN123")).thenReturn(Optional.of(testShipment));

        Optional<InboundShipment> result = service.getShipmentByScannedNumber("SCAN123");

        assertTrue(result.isPresent());
        assertEquals("SCAN123", result.get().getScannedNumber());
    }

    @Test
    void getShipmentByScannedNumber_WhenNotExists_ShouldReturnEmpty() {
        when(repository.findByScannedNumber("NONEXISTENT")).thenReturn(Optional.empty());
        Optional<InboundShipment> result = service.getShipmentByScannedNumber("NONEXISTENT");
        assertTrue(result.isEmpty());
    }

    @Test
    void getShipmentByTrackingNumberInScannedNumber_WhenMultipleExist_ShouldReturnHighestId() {
        InboundShipment olderShipment = InboundShipment.builder()
                .rowId(1L)
                .trackingNumber("123")
                .shipmentType(testShipmentType)
                .build();
        
        InboundShipment newerShipment = InboundShipment.builder()
                .rowId(2L)
                .trackingNumber("123")
                .shipmentType(testShipmentType)
                .build();

        when(repository.findFirstByTrackingNumberInScannedNumber("123456789012345678901234567890"))
                .thenReturn(Optional.of(newerShipment));

        Optional<InboundShipment> result = service.getShipmentByTrackingNumberInScannedNumber("123456789012345678901234567890");
        
        assertTrue(result.isPresent());
        assertEquals(2L, result.get().getRowId());
        assertEquals("123", result.get().getTrackingNumber());
    }

    @Test
    void getShipmentByTrackingNumberInScannedNumber_WhenSingleExists_ShouldReturnShipment() {
        when(repository.findFirstByTrackingNumberInScannedNumber("123456789012345678901234567890"))
                .thenReturn(Optional.of(testShipment));
        
        Optional<InboundShipment> result = service.getShipmentByTrackingNumberInScannedNumber("123456789012345678901234567890");
        
        assertTrue(result.isPresent());
        assertEquals(testShipment, result.get());
    }

    @Test
    void getShipmentByTrackingNumberInScannedNumber_WhenNotExists_ShouldReturnEmpty() {
        when(repository.findFirstByTrackingNumberInScannedNumber("NONEXISTENT"))
                .thenReturn(Optional.empty());
        
        Optional<InboundShipment> result = service.getShipmentByTrackingNumberInScannedNumber("NONEXISTENT");
        
        assertTrue(result.isEmpty());
    }

    @Test
    void updateScanTime_ShouldUpdateShipment() {
        // ... existing code ...
    }

    @Test
    void getAllShipmentsByTrackingNumberInScannedNumber_WhenMultipleExist_ShouldReturnAll() {
        List<InboundShipment> shipments = Arrays.asList(
            InboundShipment.builder().rowId(1L).trackingNumber("123").shipmentType(testShipmentType).build(),
            InboundShipment.builder().rowId(2L).trackingNumber("123").shipmentType(testShipmentType).build()
        );

        when(repository.findAllByTrackingNumberInScannedNumber("123456789012345678901234567890"))
                .thenReturn(shipments);

        List<InboundShipment> result = service.getAllShipmentsByTrackingNumberInScannedNumber("123456789012345678901234567890");
        
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getRowId());
        assertEquals(2L, result.get(1).getRowId());
    }

    @Test
    void getAllShipmentsByTrackingNumberInScannedNumber_WhenNoneExist_ShouldReturnEmpty() {
        when(repository.findAllByTrackingNumberInScannedNumber("NONEXISTENT"))
                .thenReturn(Collections.emptyList());

        List<InboundShipment> result = service.getAllShipmentsByTrackingNumberInScannedNumber("NONEXISTENT");
        
        assertTrue(result.isEmpty());
    }

    @Test
    void getShipmentByScannedNumberAndOrganization_WhenExists_ShouldReturnShipment() {
        // Arrange
        String scannedNumber = "123456";
        Long organizationId = 1L;
        InboundShipment expectedShipment = new InboundShipment();
        expectedShipment.setScannedNumber(scannedNumber);
        Client client = new Client();
        client.setId(organizationId);
        expectedShipment.setClientEntity(client);
        when(repository.findByScannedNumberAndOrganization(scannedNumber, organizationId))
                .thenReturn(Optional.of(expectedShipment));

        // Act
        Optional<InboundShipment> result = service.getShipmentByScannedNumberAndOrganization(scannedNumber, organizationId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedShipment, result.get());
        verify(repository).findByScannedNumberAndOrganization(scannedNumber, organizationId);
    }

    @Test
    void getShipmentByScannedNumberAndOrganization_WhenNotExists_ShouldReturnEmpty() {
        when(repository.findByScannedNumberAndOrganization("NONEXISTENT", 1L))
                .thenReturn(Optional.empty());

        Optional<InboundShipment> result = service.getShipmentByScannedNumberAndOrganization("NONEXISTENT", 1L);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void getShipmentByTrackingNumberAndOrganization_WhenExists_ShouldReturnShipment() {
        // Arrange
        String trackingNumber = "TRK123";
        Long organizationId = 1L;
        InboundShipment expectedShipment = new InboundShipment();
        expectedShipment.setTrackingNumber(trackingNumber);
        Client client = new Client();
        client.setId(organizationId);
        expectedShipment.setClientEntity(client);
        when(repository.findByTrackingNumberAndOrganization(trackingNumber, organizationId))
                .thenReturn(Optional.of(expectedShipment));

        // Act
        Optional<InboundShipment> result = service.getShipmentByTrackingNumberAndOrganization(trackingNumber, organizationId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedShipment, result.get());
        verify(repository).findByTrackingNumberAndOrganization(trackingNumber, organizationId);
    }

    @Test
    void getShipmentByTrackingNumberAndOrganization_WhenNotExists_ShouldReturnEmpty() {
        // Arrange
        String trackingNumber = "TRK123";
        Long organizationId = 1L;
        when(repository.findByTrackingNumberAndOrganization(trackingNumber, organizationId))
                .thenReturn(Optional.empty());

        // Act
        Optional<InboundShipment> result = service.getShipmentByTrackingNumberAndOrganization(trackingNumber, organizationId);

        // Assert
        assertTrue(result.isEmpty());
        verify(repository).findByTrackingNumberAndOrganization(trackingNumber, organizationId);
    }

    @Test
    void updateScanTime_WithScannedNumber_ShouldUpdateShipment() {
        // ... existing code ...
    }

    // Search functionality tests
    @Test
    void searchShipments_WithAllCriteria_ShouldReturnSearchResponse() {
        // Arrange
        InboundShipmentSearchRequest searchRequest = InboundShipmentSearchRequest.builder()
                .trackingNumber("TRACK123")
                .scannedNumber("SCAN123")
                .status("Pending")
                .orderNumber("ORDER123")
                .lab("Test Lab")
                .scanUser("TEST_USER")
                .shipDateFrom(LocalDate.of(2024, 1, 1))
                .shipDateTo(LocalDate.of(2024, 12, 31))
                .scanDateFrom(LocalDate.of(2024, 1, 1))
                .scanDateTo(LocalDate.of(2024, 12, 31))
                .emailReceiveDatetimeFrom(LocalDate.of(2024, 1, 1))
                .emailReceiveDatetimeTo(LocalDate.of(2024, 12, 31))
                .lastUpdateDatetimeFrom(LocalDate.of(2024, 1, 1))
                .lastUpdateDatetimeTo(LocalDate.of(2024, 12, 31))
                .page(0)
                .size(20)
                .build();

        List<InboundShipment> shipments = Arrays.asList(testShipment);
        Page<InboundShipment> page = new PageImpl<>(shipments, PageRequest.of(0, 20), 1);
        
        when(repository.searchShipments(
                eq("TRACK123"), eq("SCAN123"), eq("Pending"), eq("ORDER123"), eq("Test Lab"), eq("TEST_USER"),
                eq(LocalDate.of(2024, 1, 1)), eq(LocalDate.of(2024, 12, 31)),
                eq(LocalDate.of(2024, 1, 1)), eq(LocalDate.of(2024, 12, 31)),
                eq(LocalDate.of(2024, 1, 1)), eq(LocalDate.of(2024, 12, 31)),
                eq(LocalDate.of(2024, 1, 1)), eq(LocalDate.of(2024, 12, 31)),
                any(Pageable.class)
        )).thenReturn(page);

        // Act
        InboundShipmentSearchResponse response = service.searchShipments(searchRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getShipments().size());
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals(0, response.getCurrentPage());
        assertEquals(20, response.getPageSize());
        assertFalse(response.isHasNext());
        assertFalse(response.isHasPrevious());
        assertEquals(testShipment, response.getShipments().get(0));
    }

    @Test
    void searchShipments_WithPartialCriteria_ShouldReturnSearchResponse() {
        // Arrange
        InboundShipmentSearchRequest searchRequest = InboundShipmentSearchRequest.builder()
                .trackingNumber("TRACK")
                .status("Pending")
                .page(0)
                .size(10)
                .build();

        List<InboundShipment> shipments = Arrays.asList(testShipment);
        Page<InboundShipment> page = new PageImpl<>(shipments, PageRequest.of(0, 10), 1);
        
        when(repository.searchShipments(
                eq("TRACK"), isNull(), eq("Pending"), isNull(), isNull(), isNull(),
                isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(),
                any(Pageable.class)
        )).thenReturn(page);

        // Act
        InboundShipmentSearchResponse response = service.searchShipments(searchRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getShipments().size());
        assertEquals(1, response.getTotalElements());
        assertEquals(0, response.getCurrentPage());
        assertEquals(10, response.getPageSize());
    }

    @Test
    void searchShipments_WithDateRangeOnly_ShouldReturnSearchResponse() {
        // Arrange
        InboundShipmentSearchRequest searchRequest = InboundShipmentSearchRequest.builder()
                .shipDateFrom(LocalDate.of(2024, 6, 1))
                .shipDateTo(LocalDate.of(2024, 6, 30))
                .scanDateFrom(LocalDate.of(2024, 6, 1))
                .scanDateTo(LocalDate.of(2024, 6, 30))
                .page(0)
                .size(50)
                .build();

        List<InboundShipment> shipments = Arrays.asList(testShipment);
        Page<InboundShipment> page = new PageImpl<>(shipments, PageRequest.of(0, 50), 1);
        
        when(repository.searchShipments(
                isNull(), isNull(), isNull(), isNull(), isNull(), isNull(),
                eq(LocalDate.of(2024, 6, 1)), eq(LocalDate.of(2024, 6, 30)),
                eq(LocalDate.of(2024, 6, 1)), eq(LocalDate.of(2024, 6, 30)),
                isNull(), isNull(), isNull(), isNull(),
                any(Pageable.class)
        )).thenReturn(page);

        // Act
        InboundShipmentSearchResponse response = service.searchShipments(searchRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getShipments().size());
        assertEquals(1, response.getTotalElements());
        assertEquals(0, response.getCurrentPage());
        assertEquals(50, response.getPageSize());
    }

    @Test
    void searchShipments_WithEmptyResults_ShouldReturnEmptyResponse() {
        // Arrange
        InboundShipmentSearchRequest searchRequest = InboundShipmentSearchRequest.builder()
                .trackingNumber("NONEXISTENT")
                .page(0)
                .size(20)
                .build();

        Page<InboundShipment> page = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 20), 0);
        
        when(repository.searchShipments(
                eq("NONEXISTENT"), isNull(), isNull(), isNull(), isNull(), isNull(),
                isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(),
                any(Pageable.class)
        )).thenReturn(page);

        // Act
        InboundShipmentSearchResponse response = service.searchShipments(searchRequest);

        // Assert
        assertNotNull(response);
        assertTrue(response.getShipments().isEmpty());
        assertEquals(0, response.getTotalElements());
        assertEquals(0, response.getTotalPages());
        assertEquals(0, response.getCurrentPage());
        assertEquals(20, response.getPageSize());
        assertFalse(response.isHasNext());
        assertFalse(response.isHasPrevious());
    }

    @Test
    void searchShipments_WithPagination_ShouldReturnCorrectPageInfo() {
        // Arrange
        InboundShipmentSearchRequest searchRequest = InboundShipmentSearchRequest.builder()
                .trackingNumber("TRACK")
                .page(1)
                .size(5)
                .build();

        List<InboundShipment> shipments = Arrays.asList(testShipment);
        Page<InboundShipment> page = new PageImpl<>(shipments, PageRequest.of(1, 5), 15);
        
        when(repository.searchShipments(
                eq("TRACK"), isNull(), isNull(), isNull(), isNull(), isNull(),
                isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(),
                any(Pageable.class)
        )).thenReturn(page);

        // Act
        InboundShipmentSearchResponse response = service.searchShipments(searchRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getShipments().size());
        assertEquals(15, response.getTotalElements());
        assertEquals(3, response.getTotalPages());
        assertEquals(1, response.getCurrentPage());
        assertEquals(5, response.getPageSize());
        assertTrue(response.isHasNext());
        assertTrue(response.isHasPrevious());
    }

    @Test
    void searchShipments_WithNullSearchRequest_ShouldHandleGracefully() {
        // Arrange
        InboundShipmentSearchRequest searchRequest = InboundShipmentSearchRequest.builder()
                .page(0)
                .size(20)
                .build();

        List<InboundShipment> shipments = Arrays.asList(testShipment);
        Page<InboundShipment> page = new PageImpl<>(shipments, PageRequest.of(0, 20), 1);
        
        when(repository.searchShipments(
                isNull(), isNull(), isNull(), isNull(), isNull(), isNull(),
                isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(),
                any(Pageable.class)
        )).thenReturn(page);

        // Act
        InboundShipmentSearchResponse response = service.searchShipments(searchRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getShipments().size());
        assertEquals(1, response.getTotalElements());
    }
} 