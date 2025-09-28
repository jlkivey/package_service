package com.clevelanddx.packageintake.controller;

import com.clevelanddx.packageintake.dto.InboundShipmentSearchRequest;
import com.clevelanddx.packageintake.dto.InboundShipmentSearchResponse;
import com.clevelanddx.packageintake.model.InboundShipment;
import com.clevelanddx.packageintake.model.InboundShipmentReference;
import com.clevelanddx.packageintake.service.InboundShipmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InboundShipmentControllerTest {

    @Mock
    private InboundShipmentService service;

    @InjectMocks
    private InboundShipmentController controller;

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
    void createShipment_ShouldReturnCreatedShipment() {
        // Given
        when(service.createShipment(any(InboundShipment.class))).thenReturn(testShipment);

        // When
        ResponseEntity<InboundShipment> response = controller.createShipment(testShipment);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testShipment.getTrackingNumber(), response.getBody().getTrackingNumber());
        assertEquals(testShipment.getShipmentType(), response.getBody().getShipmentType());
        verify(service).createShipment(testShipment);
    }

    @Test
    void getShipmentById_ShouldReturnShipmentWhenExists() {
        // Given
        when(service.getShipmentById(1L)).thenReturn(Optional.of(testShipment));

        // When
        ResponseEntity<InboundShipment> response = controller.getShipmentById(1L);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testShipment.getTrackingNumber(), response.getBody().getTrackingNumber());
        assertEquals(testShipment.getShipmentType(), response.getBody().getShipmentType());
        verify(service).getShipmentById(1L);
    }

    @Test
    void getShipmentById_ShouldReturnNotFoundWhenNotExists() {
        // Given
        when(service.getShipmentById(999L)).thenReturn(Optional.empty());

        // When
        ResponseEntity<InboundShipment> response = controller.getShipmentById(999L);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(service).getShipmentById(999L);
    }

    @Test
    void getAllShipments_ShouldReturnAllShipments() {
        // Given
        List<InboundShipment> shipments = Arrays.asList(testShipment);
        when(service.getAllShipments()).thenReturn(shipments);

        // When
        ResponseEntity<List<InboundShipment>> response = controller.getAllShipments();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(testShipment.getShipmentType(), response.getBody().get(0).getShipmentType());
        verify(service).getAllShipments();
    }

    @Test
    void updateShipment_ShouldReturnUpdatedShipment() {
        // Given
        when(service.updateShipment(1L, testShipment)).thenReturn(testShipment);

        // When
        ResponseEntity<InboundShipment> response = controller.updateShipment(1L, testShipment);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testShipment.getShipmentType(), response.getBody().getShipmentType());
        verify(service).updateShipment(1L, testShipment);
    }

    // Search functionality tests
    @Test
    void searchShipments_WithValidRequest_ShouldReturnSearchResponse() {
        // Given
        InboundShipmentSearchRequest searchRequest = InboundShipmentSearchRequest.builder()
                .trackingNumber("TRACK123")
                .status("Pending")
                .page(0)
                .size(20)
                .build();

        InboundShipmentSearchResponse searchResponse = InboundShipmentSearchResponse.builder()
                .shipments(Arrays.asList(testShipment))
                .totalElements(1L)
                .totalPages(1)
                .currentPage(0)
                .pageSize(20)
                .hasNext(false)
                .hasPrevious(false)
                .build();

        when(service.searchShipments(searchRequest)).thenReturn(searchResponse);

        // When
        ResponseEntity<InboundShipmentSearchResponse> response = controller.searchShipments(searchRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getShipments().size());
        assertEquals(1L, response.getBody().getTotalElements());
        assertEquals(0, response.getBody().getCurrentPage());
        assertEquals(20, response.getBody().getPageSize());
        assertFalse(response.getBody().isHasNext());
        assertFalse(response.getBody().isHasPrevious());
        verify(service).searchShipments(searchRequest);
    }

    @Test
    void searchShipments_WithAllCriteria_ShouldReturnSearchResponse() {
        // Given
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

        InboundShipmentSearchResponse searchResponse = InboundShipmentSearchResponse.builder()
                .shipments(Arrays.asList(testShipment))
                .totalElements(1L)
                .totalPages(1)
                .currentPage(0)
                .pageSize(20)
                .hasNext(false)
                .hasPrevious(false)
                .build();

        when(service.searchShipments(searchRequest)).thenReturn(searchResponse);

        // When
        ResponseEntity<InboundShipmentSearchResponse> response = controller.searchShipments(searchRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getShipments().size());
        verify(service).searchShipments(searchRequest);
    }

    @Test
    void searchShipments_WithEmptyResults_ShouldReturnEmptyResponse() {
        // Given
        InboundShipmentSearchRequest searchRequest = InboundShipmentSearchRequest.builder()
                .trackingNumber("NONEXISTENT")
                .page(0)
                .size(20)
                .build();

        InboundShipmentSearchResponse searchResponse = InboundShipmentSearchResponse.builder()
                .shipments(Collections.emptyList())
                .totalElements(0L)
                .totalPages(0)
                .currentPage(0)
                .pageSize(20)
                .hasNext(false)
                .hasPrevious(false)
                .build();

        when(service.searchShipments(searchRequest)).thenReturn(searchResponse);

        // When
        ResponseEntity<InboundShipmentSearchResponse> response = controller.searchShipments(searchRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getShipments().isEmpty());
        assertEquals(0L, response.getBody().getTotalElements());
        verify(service).searchShipments(searchRequest);
    }

    @Test
    void searchShipments_WithPagination_ShouldReturnCorrectPageInfo() {
        // Given
        InboundShipmentSearchRequest searchRequest = InboundShipmentSearchRequest.builder()
                .trackingNumber("TRACK")
                .page(1)
                .size(5)
                .build();

        InboundShipmentSearchResponse searchResponse = InboundShipmentSearchResponse.builder()
                .shipments(Arrays.asList(testShipment))
                .totalElements(15L)
                .totalPages(3)
                .currentPage(1)
                .pageSize(5)
                .hasNext(true)
                .hasPrevious(true)
                .build();

        when(service.searchShipments(searchRequest)).thenReturn(searchResponse);

        // When
        ResponseEntity<InboundShipmentSearchResponse> response = controller.searchShipments(searchRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getShipments().size());
        assertEquals(15L, response.getBody().getTotalElements());
        assertEquals(3, response.getBody().getTotalPages());
        assertEquals(1, response.getBody().getCurrentPage());
        assertEquals(5, response.getBody().getPageSize());
        assertTrue(response.getBody().isHasNext());
        assertTrue(response.getBody().isHasPrevious());
        verify(service).searchShipments(searchRequest);
    }

    @Test
    void searchShipments_WithInvalidPageNumber_ShouldCorrectToZero() {
        // Given
        InboundShipmentSearchRequest searchRequest = InboundShipmentSearchRequest.builder()
                .trackingNumber("TRACK")
                .page(-1) // Invalid page number
                .size(20)
                .build();

        InboundShipmentSearchResponse searchResponse = InboundShipmentSearchResponse.builder()
                .shipments(Arrays.asList(testShipment))
                .totalElements(1L)
                .totalPages(1)
                .currentPage(0) // Should be corrected to 0
                .pageSize(20)
                .hasNext(false)
                .hasPrevious(false)
                .build();

        when(service.searchShipments(any(InboundShipmentSearchRequest.class))).thenReturn(searchResponse);

        // When
        ResponseEntity<InboundShipmentSearchResponse> response = controller.searchShipments(searchRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(service).searchShipments(any(InboundShipmentSearchRequest.class));
    }

    @Test
    void searchShipments_WithInvalidPageSize_ShouldCorrectToDefault() {
        // Given
        InboundShipmentSearchRequest searchRequest = InboundShipmentSearchRequest.builder()
                .trackingNumber("TRACK")
                .page(0)
                .size(2000) // Invalid page size (too large)
                .build();

        InboundShipmentSearchResponse searchResponse = InboundShipmentSearchResponse.builder()
                .shipments(Arrays.asList(testShipment))
                .totalElements(1L)
                .totalPages(1)
                .currentPage(0)
                .pageSize(20) // Should be corrected to 20
                .hasNext(false)
                .hasPrevious(false)
                .build();

        when(service.searchShipments(any(InboundShipmentSearchRequest.class))).thenReturn(searchResponse);

        // When
        ResponseEntity<InboundShipmentSearchResponse> response = controller.searchShipments(searchRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(service).searchShipments(any(InboundShipmentSearchRequest.class));
    }

    @Test
    void searchShipments_WithServiceException_ShouldReturnBadRequest() {
        // Given
        InboundShipmentSearchRequest searchRequest = InboundShipmentSearchRequest.builder()
                .trackingNumber("TRACK")
                .page(0)
                .size(20)
                .build();

        when(service.searchShipments(searchRequest)).thenThrow(new RuntimeException("Database error"));

        // When
        ResponseEntity<InboundShipmentSearchResponse> response = controller.searchShipments(searchRequest);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(service).searchShipments(searchRequest);
    }
}
