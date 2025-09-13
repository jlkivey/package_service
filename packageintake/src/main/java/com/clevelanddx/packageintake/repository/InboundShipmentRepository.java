package com.clevelanddx.packageintake.repository;

import com.clevelanddx.packageintake.model.InboundShipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InboundShipmentRepository extends JpaRepository<InboundShipment, Long> {
    Optional<InboundShipment> findByTrackingNumber(String trackingNumber);
    List<InboundShipment> findByClient(String client);
    List<InboundShipment> findByStatus(String status);
    Optional<InboundShipment> findByOrderNumber(String orderNumber);
    Optional<InboundShipment> findByScannedNumber(String scannedNumber);
    
    // New date-based query methods
    @Query(value = """
        SELECT * FROM Inbound_Shipments 
        WHERE CAST(Scan_Time AS DATE) = CAST(DATEADD(hour, -5, GETUTCDATE()) AS DATE)
        ORDER BY Row_ID DESC
        """, nativeQuery = true)
    List<InboundShipment> findTodayShipments();
    
    @Query(value = """
        SELECT TOP(:limit) * FROM Inbound_Shipments 
        ORDER BY Row_ID DESC
        """, nativeQuery = true)
    List<InboundShipment> findRecentShipments(@Param("limit") int limit);
    
    @Query(value = """
        SELECT * FROM Inbound_Shipments 
        WHERE CAST(Scan_Time AS DATE) = :date
        ORDER BY Row_ID DESC
        """, nativeQuery = true)
    List<InboundShipment> findByScanDate(@Param("date") LocalDate date);
    
    @Query(value = """
        SELECT * FROM Inbound_Shipments 
        WHERE CAST(Scan_Time AS DATE) BETWEEN :fromDate AND :toDate
        ORDER BY Row_ID DESC
        """, nativeQuery = true)
    List<InboundShipment> findByScanDateRange(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
    
    @Query(value = """
        SELECT TOP 1 *
        FROM Inbound_Shipments
        WHERE (
            (
                :scannedNumber IS NOT NULL AND
                :scannedNumber <> '' AND
                scanned_number IS NOT NULL AND
                scanned_number <> '' AND
                scanned_number = :scannedNumber
            )
            OR
            (
                NOT EXISTS (
                    SELECT 1
                    FROM Inbound_Shipments
                    WHERE :scannedNumber IS NOT NULL AND
                          :scannedNumber <> '' AND
                          scanned_number IS NOT NULL AND
                          scanned_number <> '' AND
                          scanned_number = :scannedNumber
                )
                AND :scannedNumber LIKE CONCAT('%', Tracking_Number, '%')
                AND Tracking_Number IS NOT NULL
                AND Tracking_Number <> ''
                AND LEN(:scannedNumber) > 9
            )
        )
        ORDER BY Row_ID DESC
        """, nativeQuery = true)
    Optional<InboundShipment> findFirstByTrackingNumberInScannedNumber(@Param("scannedNumber") String scannedNumber);
    
    @Query(value = """
        SELECT *
        FROM Inbound_Shipments
        WHERE (
            (
                :scannedNumber IS NOT NULL AND
                :scannedNumber <> '' AND
                scanned_number IS NOT NULL AND
                scanned_number <> '' AND
                scanned_number = :scannedNumber
            )
            OR
            (
                NOT EXISTS (
                    SELECT 1
                    FROM Inbound_Shipments
                    WHERE :scannedNumber IS NOT NULL AND
                          :scannedNumber <> '' AND
                          scanned_number IS NOT NULL AND
                          scanned_number <> '' AND
                          scanned_number = :scannedNumber
                )
                AND :scannedNumber LIKE CONCAT('%', Tracking_Number, '%')
                AND Tracking_Number IS NOT NULL
                AND Tracking_Number <> ''
                AND LEN(:scannedNumber) > 9
            )
        )
        ORDER BY Row_ID DESC
        """, nativeQuery = true)
    List<InboundShipment> findAllByTrackingNumberInScannedNumber(@Param("scannedNumber") String scannedNumber);
    
    @Query(value = "SELECT TOP 1 * FROM Inbound_Shipments WHERE :scannedNumber LIKE CONCAT('%', Tracking_Number, '%') AND Client_ID = :organizationId  and Tracking_Number is not null and tracking_number <> '' and len(:scannedNumber) > 9 ORDER BY Row_ID DESC", nativeQuery = true)
    Optional<InboundShipment> findByScannedNumberAndOrganization(@Param("scannedNumber") String scannedNumber, @Param("organizationId") Long organizationId);

    @Query(value = "SELECT TOP 1 * FROM Inbound_Shipments WHERE Tracking_Number = :trackingNumber AND Client_ID = :organizationId AND Tracking_Number is not null and tracking_number <> '' ORDER BY Row_ID DESC", nativeQuery = true)
    Optional<InboundShipment> findByTrackingNumberAndOrganization(@Param("trackingNumber") String trackingNumber, @Param("organizationId") Long organizationId);
} 