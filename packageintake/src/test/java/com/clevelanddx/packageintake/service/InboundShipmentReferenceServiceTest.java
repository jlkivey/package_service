package com.clevelanddx.packageintake.service;

import com.clevelanddx.packageintake.model.InboundShipmentReference;
import com.clevelanddx.packageintake.repository.InboundShipmentReferenceRepository;
import com.clevelanddx.packageintake.service.impl.InboundShipmentReferenceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InboundShipmentReferenceServiceTest {

    @Mock
    private InboundShipmentReferenceRepository repository;

    @InjectMocks
    private InboundShipmentReferenceServiceImpl service;

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
        when(repository.findAll()).thenReturn(Arrays.asList(reference1, reference2));

        // When
        List<InboundShipmentReference> result = service.getAllReferences();

        // Then
        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    void getReferencesByType_ShouldReturnReferencesByType() {
        // Given
        String type = "SHIPPING_TYPE";
        when(repository.findByType(type)).thenReturn(Arrays.asList(reference1, reference2));

        // When
        List<InboundShipmentReference> result = service.getReferencesByType(type);

        // Then
        assertEquals(2, result.size());
        verify(repository).findByType(type);
    }

    @Test
    void getReferenceById_ShouldReturnReferenceWhenExists() {
        // Given
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.of(reference1));

        // When
        Optional<InboundShipmentReference> result = service.getReferenceById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(reference1, result.get());
        verify(repository).findById(id);
    }

    @Test
    void getReferenceById_ShouldReturnEmptyWhenNotExists() {
        // Given
        Long id = 999L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<InboundShipmentReference> result = service.getReferenceById(id);

        // Then
        assertFalse(result.isPresent());
        verify(repository).findById(id);
    }
}
