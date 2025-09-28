package com.clevelanddx.packageintake.service.impl;

import com.clevelanddx.packageintake.model.InboundShipmentReference;
import com.clevelanddx.packageintake.repository.InboundShipmentReferenceRepository;
import com.clevelanddx.packageintake.service.InboundShipmentReferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InboundShipmentReferenceServiceImpl implements InboundShipmentReferenceService {

    private final InboundShipmentReferenceRepository repository;

    @Autowired
    public InboundShipmentReferenceServiceImpl(InboundShipmentReferenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<InboundShipmentReference> getAllReferences() {
        return repository.findAll();
    }

    @Override
    public List<InboundShipmentReference> getReferencesByType(String type) {
        return repository.findByType(type);
    }

    @Override
    public Optional<InboundShipmentReference> getReferenceById(Long id) {
        return repository.findById(id);
    }

    @Override
    public InboundShipmentReference createReference(InboundShipmentReference reference) {
        return repository.save(reference);
    }

    @Override
    public InboundShipmentReference updateReference(Long id, InboundShipmentReference reference) {
        if (!repository.existsById(id)) {
            throw new jakarta.persistence.EntityNotFoundException("Reference not found with id: " + id);
        }
        reference.setRowId(id);
        return repository.save(reference);
    }

    @Override
    public void deleteReference(Long id) {
        if (!repository.existsById(id)) {
            throw new jakarta.persistence.EntityNotFoundException("Reference not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public InboundShipmentReference findOrCreateReference(String type, String value, String description) {
        // First try to find by type and value
        Optional<InboundShipmentReference> existingReference = repository.findByTypeAndValue(type, value);
        if (existingReference.isPresent()) {
            return existingReference.get();
        }
        
        // If not found, create a new one
        InboundShipmentReference newReference = InboundShipmentReference.builder()
                .type(type)
                .value(value)
                .description(description != null ? description : "")
                .build();
        
        return repository.save(newReference);
    }

    @Override
    public Optional<InboundShipmentReference> findByTypeAndValue(String type, String value) {
        return repository.findByTypeAndValue(type, value);
    }
}
