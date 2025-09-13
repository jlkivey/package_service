package com.clevelanddx.packageintake.controller;

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
}
