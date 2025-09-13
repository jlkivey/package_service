package com.clevelanddx.packageintake.controller;

import com.clevelanddx.packageintake.model.InboundShipmentReference;
import com.clevelanddx.packageintake.service.InboundShipmentReferenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InboundShipmentReferenceControllerTest {

    @Mock
    private InboundShipmentReferenceService service;

    @InjectMocks
    private InboundShipmentReferenceController controller;

    private InboundShipmentReference reference1;
    private InboundShipmentReference reference2;

    @BeforeEach
    void setUp() {
        reference1 = InboundShipmentReference.builder()
                .rowId(1L)
                .type("SHIPPING_TYPE")
                .value("Dry Ice")
                .description(null)
                .build();

        reference2 = InboundShipmentReference.builder()
                .rowId(2L)
                .type("SHIPPING_TYPE")
                .value("LL Box")
                .description(null)
                .build();
    }

    @Test
    void getAllReferences_ShouldReturnAllReferences() {
        // Given
        when(service.getAllReferences()).thenReturn(Arrays.asList(reference1, reference2));

        // When
        ResponseEntity<List<InboundShipmentReference>> response = controller.getAllReferences();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(service).getAllReferences();
    }

    @Test
    void getReferenceById_ShouldReturnReferenceWhenExists() {
        // Given
        Long id = 1L;
        when(service.getReferenceById(id)).thenReturn(Optional.of(reference1));

        // When
        ResponseEntity<InboundShipmentReference> response = controller.getReferenceById(id);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(reference1, response.getBody());
        verify(service).getReferenceById(id);
    }

    @Test
    void getReferenceById_ShouldReturnNotFoundWhenNotExists() {
        // Given
        Long id = 999L;
        when(service.getReferenceById(id)).thenReturn(Optional.empty());

        // When
        ResponseEntity<InboundShipmentReference> response = controller.getReferenceById(id);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(service).getReferenceById(id);
    }

    @Test
    void getReferencesByType_ShouldReturnReferencesWhenTypeExists() {
        // Given
        String type = "SHIPPING_TYPE";
        when(service.getReferencesByType(type)).thenReturn(Arrays.asList(reference1, reference2));

        // When
        ResponseEntity<List<InboundShipmentReference>> response = controller.getReferencesByType(type);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(service).getReferencesByType(type);
    }

    @Test
    void getReferencesByType_ShouldReturnNoContentWhenTypeNotExists() {
        // Given
        String type = "NON_EXISTENT_TYPE";
        when(service.getReferencesByType(type)).thenReturn(Arrays.asList());

        // When
        ResponseEntity<List<InboundShipmentReference>> response = controller.getReferencesByType(type);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).getReferencesByType(type);
    }
}
