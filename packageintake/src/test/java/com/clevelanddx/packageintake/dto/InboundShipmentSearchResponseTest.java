package com.clevelanddx.packageintake.dto;

import com.clevelanddx.packageintake.model.InboundShipment;
import com.clevelanddx.packageintake.model.InboundShipmentReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InboundShipmentSearchResponseTest {

    private InboundShipment testShipment;

    @BeforeEach
    void setUp() {
        InboundShipmentReference testShipmentType = InboundShipmentReference.builder()
                .rowId(1L)
                .type("SHIPPING_TYPE")
                .value("Dry Ice")
                .description("Test Description")
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
    void builder_ShouldCreateResponseWithAllFields() {
        // Given
        List<InboundShipment> shipments = Arrays.asList(testShipment);

        // When
        InboundShipmentSearchResponse response = InboundShipmentSearchResponse.builder()
                .shipments(shipments)
                .totalElements(1L)
                .totalPages(1)
                .currentPage(0)
                .pageSize(20)
                .hasNext(false)
                .hasPrevious(false)
                .build();

        // Then
        assertEquals(shipments, response.getShipments());
        assertEquals(1L, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals(0, response.getCurrentPage());
        assertEquals(20, response.getPageSize());
        assertFalse(response.isHasNext());
        assertFalse(response.isHasPrevious());
    }

    @Test
    void builder_WithEmptyResults_ShouldCreateEmptyResponse() {
        // When
        InboundShipmentSearchResponse response = InboundShipmentSearchResponse.builder()
                .shipments(Collections.emptyList())
                .totalElements(0L)
                .totalPages(0)
                .currentPage(0)
                .pageSize(20)
                .hasNext(false)
                .hasPrevious(false)
                .build();

        // Then
        assertTrue(response.getShipments().isEmpty());
        assertEquals(0L, response.getTotalElements());
        assertEquals(0, response.getTotalPages());
        assertEquals(0, response.getCurrentPage());
        assertEquals(20, response.getPageSize());
        assertFalse(response.isHasNext());
        assertFalse(response.isHasPrevious());
    }

    @Test
    void builder_WithPagination_ShouldCreateResponseWithPaginationInfo() {
        // Given
        List<InboundShipment> shipments = Arrays.asList(testShipment);

        // When
        InboundShipmentSearchResponse response = InboundShipmentSearchResponse.builder()
                .shipments(shipments)
                .totalElements(15L)
                .totalPages(3)
                .currentPage(1)
                .pageSize(5)
                .hasNext(true)
                .hasPrevious(true)
                .build();

        // Then
        assertEquals(shipments, response.getShipments());
        assertEquals(15L, response.getTotalElements());
        assertEquals(3, response.getTotalPages());
        assertEquals(1, response.getCurrentPage());
        assertEquals(5, response.getPageSize());
        assertTrue(response.isHasNext());
        assertTrue(response.isHasPrevious());
    }

    @Test
    void builder_WithMultipleShipments_ShouldCreateResponseWithMultipleShipments() {
        // Given
        InboundShipment shipment2 = InboundShipment.builder()
                .rowId(2L)
                .client("Test Client 2")
                .trackingNumber("TRACK456")
                .scannedNumber("SCAN456")
                .status("Delivered")
                .build();

        List<InboundShipment> shipments = Arrays.asList(testShipment, shipment2);

        // When
        InboundShipmentSearchResponse response = InboundShipmentSearchResponse.builder()
                .shipments(shipments)
                .totalElements(2L)
                .totalPages(1)
                .currentPage(0)
                .pageSize(20)
                .hasNext(false)
                .hasPrevious(false)
                .build();

        // Then
        assertEquals(2, response.getShipments().size());
        assertEquals(testShipment, response.getShipments().get(0));
        assertEquals(shipment2, response.getShipments().get(1));
        assertEquals(2L, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals(0, response.getCurrentPage());
        assertEquals(20, response.getPageSize());
        assertFalse(response.isHasNext());
        assertFalse(response.isHasPrevious());
    }

    @Test
    void builder_WithNullShipments_ShouldAcceptNullShipments() {
        // When
        InboundShipmentSearchResponse response = InboundShipmentSearchResponse.builder()
                .shipments(null)
                .totalElements(0L)
                .totalPages(0)
                .currentPage(0)
                .pageSize(20)
                .hasNext(false)
                .hasPrevious(false)
                .build();

        // Then
        assertNull(response.getShipments());
        assertEquals(0L, response.getTotalElements());
        assertEquals(0, response.getTotalPages());
        assertEquals(0, response.getCurrentPage());
        assertEquals(20, response.getPageSize());
        assertFalse(response.isHasNext());
        assertFalse(response.isHasPrevious());
    }

    @Test
    void builder_WithFirstPage_ShouldSetCorrectPaginationFlags() {
        // Given
        List<InboundShipment> shipments = Arrays.asList(testShipment);

        // When
        InboundShipmentSearchResponse response = InboundShipmentSearchResponse.builder()
                .shipments(shipments)
                .totalElements(50L)
                .totalPages(5)
                .currentPage(0) // First page
                .pageSize(10)
                .hasNext(true)
                .hasPrevious(false) // No previous page
                .build();

        // Then
        assertEquals(0, response.getCurrentPage());
        assertTrue(response.isHasNext());
        assertFalse(response.isHasPrevious());
    }

    @Test
    void builder_WithLastPage_ShouldSetCorrectPaginationFlags() {
        // Given
        List<InboundShipment> shipments = Arrays.asList(testShipment);

        // When
        InboundShipmentSearchResponse response = InboundShipmentSearchResponse.builder()
                .shipments(shipments)
                .totalElements(50L)
                .totalPages(5)
                .currentPage(4) // Last page (0-indexed)
                .pageSize(10)
                .hasNext(false) // No next page
                .hasPrevious(true)
                .build();

        // Then
        assertEquals(4, response.getCurrentPage());
        assertFalse(response.isHasNext());
        assertTrue(response.isHasPrevious());
    }

    @Test
    void builder_WithMiddlePage_ShouldSetCorrectPaginationFlags() {
        // Given
        List<InboundShipment> shipments = Arrays.asList(testShipment);

        // When
        InboundShipmentSearchResponse response = InboundShipmentSearchResponse.builder()
                .shipments(shipments)
                .totalElements(50L)
                .totalPages(5)
                .currentPage(2) // Middle page
                .pageSize(10)
                .hasNext(true)
                .hasPrevious(true)
                .build();

        // Then
        assertEquals(2, response.getCurrentPage());
        assertTrue(response.isHasNext());
        assertTrue(response.isHasPrevious());
    }
}
