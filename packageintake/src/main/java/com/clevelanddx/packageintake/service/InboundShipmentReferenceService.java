package com.clevelanddx.packageintake.service;

import com.clevelanddx.packageintake.model.InboundShipmentReference;

import java.util.List;
import java.util.Optional;

public interface InboundShipmentReferenceService {
    List<InboundShipmentReference> getAllReferences();
    List<InboundShipmentReference> getReferencesByType(String type);
    Optional<InboundShipmentReference> getReferenceById(Long id);
    InboundShipmentReference createReference(InboundShipmentReference reference);
    InboundShipmentReference updateReference(Long id, InboundShipmentReference reference);
    void deleteReference(Long id);
    InboundShipmentReference findOrCreateReference(String type, String value, String description);
    Optional<InboundShipmentReference> findByTypeAndValue(String type, String value);
}
