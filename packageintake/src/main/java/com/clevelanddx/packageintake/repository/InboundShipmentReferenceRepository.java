package com.clevelanddx.packageintake.repository;

import com.clevelanddx.packageintake.model.InboundShipmentReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InboundShipmentReferenceRepository extends JpaRepository<InboundShipmentReference, Long> {
    List<InboundShipmentReference> findByType(String type);
    Optional<InboundShipmentReference> findByTypeAndValue(String type, String value);
}
