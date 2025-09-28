package com.clevelanddx.packageintake.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InboundShipmentSearchRequestTest {

    @Test
    void builder_ShouldCreateRequestWithAllFields() {
        // Given
        LocalDate testDate = LocalDate.of(2024, 6, 15);

        // When
        InboundShipmentSearchRequest request = InboundShipmentSearchRequest.builder()
                .trackingNumber("TRACK123")
                .scannedNumber("SCAN123")
                .status("Pending")
                .orderNumber("ORDER123")
                .lab("Test Lab")
                .scanUser("TEST_USER")
                .shipDateFrom(testDate)
                .shipDateTo(testDate.plusDays(30))
                .scanDateFrom(testDate)
                .scanDateTo(testDate.plusDays(30))
                .emailReceiveDatetimeFrom(testDate)
                .emailReceiveDatetimeTo(testDate.plusDays(30))
                .lastUpdateDatetimeFrom(testDate)
                .lastUpdateDatetimeTo(testDate.plusDays(30))
                .page(1)
                .size(50)
                .build();

        // Then
        assertEquals("TRACK123", request.getTrackingNumber());
        assertEquals("SCAN123", request.getScannedNumber());
        assertEquals("Pending", request.getStatus());
        assertEquals("ORDER123", request.getOrderNumber());
        assertEquals("Test Lab", request.getLab());
        assertEquals("TEST_USER", request.getScanUser());
        assertEquals(testDate, request.getShipDateFrom());
        assertEquals(testDate.plusDays(30), request.getShipDateTo());
        assertEquals(testDate, request.getScanDateFrom());
        assertEquals(testDate.plusDays(30), request.getScanDateTo());
        assertEquals(testDate, request.getEmailReceiveDatetimeFrom());
        assertEquals(testDate.plusDays(30), request.getEmailReceiveDatetimeTo());
        assertEquals(testDate, request.getLastUpdateDatetimeFrom());
        assertEquals(testDate.plusDays(30), request.getLastUpdateDatetimeTo());
        assertEquals(1, request.getPage());
        assertEquals(50, request.getSize());
    }

    @Test
    void builder_ShouldUseDefaultValues() {
        // When
        InboundShipmentSearchRequest request = InboundShipmentSearchRequest.builder().build();

        // Then
        assertNull(request.getTrackingNumber());
        assertNull(request.getScannedNumber());
        assertNull(request.getStatus());
        assertNull(request.getOrderNumber());
        assertNull(request.getLab());
        assertNull(request.getScanUser());
        assertNull(request.getShipDateFrom());
        assertNull(request.getShipDateTo());
        assertNull(request.getScanDateFrom());
        assertNull(request.getScanDateTo());
        assertNull(request.getEmailReceiveDatetimeFrom());
        assertNull(request.getEmailReceiveDatetimeTo());
        assertNull(request.getLastUpdateDatetimeFrom());
        assertNull(request.getLastUpdateDatetimeTo());
        assertEquals(0, request.getPage()); // @Builder.Default
        assertEquals(20, request.getSize()); // @Builder.Default
    }

    @Test
    void builder_WithPartialFields_ShouldCreateRequestWithOnlySpecifiedFields() {
        // When
        InboundShipmentSearchRequest request = InboundShipmentSearchRequest.builder()
                .trackingNumber("TRACK123")
                .status("Pending")
                .page(2)
                .size(10)
                .build();

        // Then
        assertEquals("TRACK123", request.getTrackingNumber());
        assertNull(request.getScannedNumber());
        assertEquals("Pending", request.getStatus());
        assertNull(request.getOrderNumber());
        assertNull(request.getLab());
        assertNull(request.getScanUser());
        assertNull(request.getShipDateFrom());
        assertNull(request.getShipDateTo());
        assertNull(request.getScanDateFrom());
        assertNull(request.getScanDateTo());
        assertNull(request.getEmailReceiveDatetimeFrom());
        assertNull(request.getEmailReceiveDatetimeTo());
        assertNull(request.getLastUpdateDatetimeFrom());
        assertNull(request.getLastUpdateDatetimeTo());
        assertEquals(2, request.getPage());
        assertEquals(10, request.getSize());
    }

    @Test
    void builder_WithDateRanges_ShouldSetBothFromAndToDates() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 1, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);

        // When
        InboundShipmentSearchRequest request = InboundShipmentSearchRequest.builder()
                .shipDateFrom(fromDate)
                .shipDateTo(toDate)
                .scanDateFrom(fromDate)
                .scanDateTo(toDate)
                .build();

        // Then
        assertEquals(fromDate, request.getShipDateFrom());
        assertEquals(toDate, request.getShipDateTo());
        assertEquals(fromDate, request.getScanDateFrom());
        assertEquals(toDate, request.getScanDateTo());
    }

    @Test
    void builder_WithNullValues_ShouldAcceptNullValues() {
        // When
        InboundShipmentSearchRequest request = InboundShipmentSearchRequest.builder()
                .trackingNumber(null)
                .scannedNumber(null)
                .status(null)
                .orderNumber(null)
                .lab(null)
                .scanUser(null)
                .shipDateFrom(null)
                .shipDateTo(null)
                .scanDateFrom(null)
                .scanDateTo(null)
                .emailReceiveDatetimeFrom(null)
                .emailReceiveDatetimeTo(null)
                .lastUpdateDatetimeFrom(null)
                .lastUpdateDatetimeTo(null)
                .build();

        // Then
        assertNull(request.getTrackingNumber());
        assertNull(request.getScannedNumber());
        assertNull(request.getStatus());
        assertNull(request.getOrderNumber());
        assertNull(request.getLab());
        assertNull(request.getScanUser());
        assertNull(request.getShipDateFrom());
        assertNull(request.getShipDateTo());
        assertNull(request.getScanDateFrom());
        assertNull(request.getScanDateTo());
        assertNull(request.getEmailReceiveDatetimeFrom());
        assertNull(request.getEmailReceiveDatetimeTo());
        assertNull(request.getLastUpdateDatetimeFrom());
        assertNull(request.getLastUpdateDatetimeTo());
        assertEquals(0, request.getPage());
        assertEquals(20, request.getSize());
    }
}
