package com.clevelanddx.packageintake.repository;

import com.clevelanddx.packageintake.model.InboundShipment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.time.LocalDate;
import java.util.List;

@Repository
public class InboundShipmentRepositoryImpl {
    
    private static final Logger log = LoggerFactory.getLogger(InboundShipmentRepositoryImpl.class);
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public Page<InboundShipment> searchShipmentsV2WithLogging(
            String trackingNumber,
            String scannedNumber,
            String status,
            String orderNumber,
            String lab,
            String scanUser,
            String clientName,
            LocalDate shipDateFrom,
            LocalDate shipDateTo,
            LocalDate scanDateFrom,
            LocalDate scanDateTo,
            LocalDate emailReceiveDatetimeFrom,
            LocalDate emailReceiveDatetimeTo,
            LocalDate lastUpdateDatetimeFrom,
            LocalDate lastUpdateDatetimeTo,
            Pageable pageable) {
        
        log.debug("=== Repository V2 Search Started ===");
        log.debug("Repository Parameters:");
        log.debug("  - trackingNumber: {}", trackingNumber);
        log.debug("  - scannedNumber: {}", scannedNumber);
        log.debug("  - status: {}", status);
        log.debug("  - orderNumber: {}", orderNumber);
        log.debug("  - lab: {}", lab);
        log.debug("  - scanUser: {}", scanUser);
        log.debug("  - clientName: {}", clientName);
        log.debug("  - shipDateFrom: {}", shipDateFrom);
        log.debug("  - shipDateTo: {}", shipDateTo);
        log.debug("  - scanDateFrom: {}", scanDateFrom);
        log.debug("  - scanDateTo: {}", scanDateTo);
        log.debug("  - emailReceiveDatetimeFrom: {}", emailReceiveDatetimeFrom);
        log.debug("  - emailReceiveDatetimeTo: {}", emailReceiveDatetimeTo);
        log.debug("  - lastUpdateDatetimeFrom: {}", lastUpdateDatetimeFrom);
        log.debug("  - lastUpdateDatetimeTo: {}", lastUpdateDatetimeTo);
        log.debug("  - pageable: {}", pageable);
        
        // Build the query string - simplified to avoid JOIN mapping issues
               String queryString = """
                   SELECT s.Row_ID, s.Client, s.Tracking_Number, s.Scanned_Number, s.Status, 
                          s.Email_ID, s.Order_Number, s.Ship_Date, s.Lab, s.Weight, 
                          s.Number_Of_Samples, s.Pickup_Time, s.Pickup_Time_2, 
                          s.Email_Receive_Datetime, s.Last_Update_Datetime, s.Scan_Time, 
                          s.Scan_User, s.Client_ID, s.Shipment_Type
                   FROM Inbound_Shipments s
            WHERE (:trackingNumber IS NULL OR s.Tracking_Number LIKE CONCAT('%', :trackingNumber, '%'))
            AND (:scannedNumber IS NULL OR s.Scanned_Number LIKE CONCAT('%', :scannedNumber, '%'))
            AND (:status IS NULL OR s.Status LIKE CONCAT('%', :status, '%'))
            AND (:orderNumber IS NULL OR s.Order_Number LIKE CONCAT('%', :orderNumber, '%'))
            AND (:lab IS NULL OR s.Lab LIKE CONCAT('%', :lab, '%'))
            AND (:scanUser IS NULL OR s.Scan_User LIKE CONCAT('%', :scanUser, '%'))
            AND (:clientName IS NULL OR LOWER(s.Client) LIKE LOWER(CONCAT('%', :clientName, '%')))
            AND (:shipDateFrom IS NULL OR s.Ship_Date >= :shipDateFrom)
            AND (:shipDateTo IS NULL OR s.Ship_Date <= :shipDateTo)
            AND (:scanDateFrom IS NULL OR CAST(s.Scan_Time AS DATE) >= :scanDateFrom)
            AND (:scanDateTo IS NULL OR CAST(s.Scan_Time AS DATE) <= :scanDateTo)
            AND (:emailReceiveDatetimeFrom IS NULL OR CAST(s.Email_Receive_Datetime AS DATE) >= :emailReceiveDatetimeFrom)
            AND (:emailReceiveDatetimeTo IS NULL OR CAST(s.Email_Receive_Datetime AS DATE) <= :emailReceiveDatetimeTo)
            AND (:lastUpdateDatetimeFrom IS NULL OR CAST(s.Last_Update_Datetime AS DATE) >= :lastUpdateDatetimeFrom)
            AND (:lastUpdateDatetimeTo IS NULL OR CAST(s.Last_Update_Datetime AS DATE) <= :lastUpdateDatetimeTo)
            ORDER BY s.Row_ID DESC
            """;
        
        log.debug("Executing Query:");
        log.debug("{}", queryString);
        
        try {
            Query query = entityManager.createNativeQuery(queryString, InboundShipment.class);
            
            // Set parameters
            query.setParameter("trackingNumber", trackingNumber);
            query.setParameter("scannedNumber", scannedNumber);
            query.setParameter("status", status);
            query.setParameter("orderNumber", orderNumber);
            query.setParameter("lab", lab);
            query.setParameter("scanUser", scanUser);
            query.setParameter("clientName", clientName);
            query.setParameter("shipDateFrom", shipDateFrom);
            query.setParameter("shipDateTo", shipDateTo);
            query.setParameter("scanDateFrom", scanDateFrom);
            query.setParameter("scanDateTo", scanDateTo);
            query.setParameter("emailReceiveDatetimeFrom", emailReceiveDatetimeFrom);
            query.setParameter("emailReceiveDatetimeTo", emailReceiveDatetimeTo);
            query.setParameter("lastUpdateDatetimeFrom", lastUpdateDatetimeFrom);
            query.setParameter("lastUpdateDatetimeTo", lastUpdateDatetimeTo);
            
            log.debug("Query parameters set successfully");
            
            // Execute query
            List<InboundShipment> results = query.getResultList();
            log.debug("Query executed successfully, found {} results", results.size());
            
            // For now, return a simple page - you might want to implement proper pagination
            return new org.springframework.data.domain.PageImpl<>(results, pageable, results.size());
            
        } catch (Exception e) {
            log.error("Error executing V2 search query", e);
            log.error("Query that failed: {}", queryString);
            throw e;
        } finally {
            log.debug("=== Repository V2 Search Completed ===");
        }
    }
}
