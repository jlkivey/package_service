package com.clevelanddx.packageintake.service.impl;

import com.clevelanddx.packageintake.service.TrackingNumberGeneratorService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementation of TrackingNumberGeneratorService that uses a SQL Server sequence
 * to generate unique incrementing tracking numbers.
 */
@Service
public class TrackingNumberGeneratorServiceImpl implements TrackingNumberGeneratorService {
    
    private static final Logger log = LoggerFactory.getLogger(TrackingNumberGeneratorServiceImpl.class);
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public String generateNextTrackingNumber() {
        try {
            // Use SQL Server NEXT VALUE FOR to get the next sequence value
            String queryString = "SELECT NEXT VALUE FOR SEQ_Tracking_Number AS next_value";
            Query query = entityManager.createNativeQuery(queryString);
            Object result = query.getSingleResult();
            
            Long nextValue = ((Number) result).longValue();
            String trackingNumber = String.valueOf(nextValue);
            
            log.debug("Generated tracking number: {}", trackingNumber);
            return trackingNumber;
            
        } catch (Exception e) {
            log.error("Error generating tracking number from sequence", e);
            throw new RuntimeException("Failed to generate tracking number", e);
        }
    }
}




