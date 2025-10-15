package com.clevelanddx.packageintake.repository;

import com.clevelanddx.packageintake.model.InboundShipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    
    // Search method with dynamic criteria
    @Query(value = """
        SELECT * FROM Inbound_Shipments 
        WHERE (:trackingNumber IS NULL OR Tracking_Number LIKE CONCAT('%', :trackingNumber, '%'))
        AND (:scannedNumber IS NULL OR Scanned_Number LIKE CONCAT('%', :scannedNumber, '%'))
        AND (:status IS NULL OR Status LIKE CONCAT('%', :status, '%'))
        AND (:orderNumber IS NULL OR Order_Number LIKE CONCAT('%', :orderNumber, '%'))
        AND (:lab IS NULL OR Lab LIKE CONCAT('%', :lab, '%'))
        AND (:scanUser IS NULL OR Scan_User LIKE CONCAT('%', :scanUser, '%'))
        AND (:shipDateFrom IS NULL OR Ship_Date >= :shipDateFrom)
        AND (:shipDateTo IS NULL OR Ship_Date <= :shipDateTo)
        AND (:scanDateFrom IS NULL OR CAST(Scan_Time AS DATE) >= :scanDateFrom)
        AND (:scanDateTo IS NULL OR CAST(Scan_Time AS DATE) <= :scanDateTo)
        AND (:emailReceiveDatetimeFrom IS NULL OR CAST(Email_Receive_Datetime AS DATE) >= :emailReceiveDatetimeFrom)
        AND (:emailReceiveDatetimeTo IS NULL OR CAST(Email_Receive_Datetime AS DATE) <= :emailReceiveDatetimeTo)
        AND (:lastUpdateDatetimeFrom IS NULL OR CAST(Last_Update_Datetime AS DATE) >= :lastUpdateDatetimeFrom)
        AND (:lastUpdateDatetimeTo IS NULL OR CAST(Last_Update_Datetime AS DATE) <= :lastUpdateDatetimeTo)
        ORDER BY Row_ID DESC
        """, nativeQuery = true)
    Page<InboundShipment> searchShipments(
        @Param("trackingNumber") String trackingNumber,
        @Param("scannedNumber") String scannedNumber,
        @Param("status") String status,
        @Param("orderNumber") String orderNumber,
        @Param("lab") String lab,
        @Param("scanUser") String scanUser,
        @Param("shipDateFrom") LocalDate shipDateFrom,
        @Param("shipDateTo") LocalDate shipDateTo,
        @Param("scanDateFrom") LocalDate scanDateFrom,
        @Param("scanDateTo") LocalDate scanDateTo,
        @Param("emailReceiveDatetimeFrom") LocalDate emailReceiveDatetimeFrom,
        @Param("emailReceiveDatetimeTo") LocalDate emailReceiveDatetimeTo,
        @Param("lastUpdateDatetimeFrom") LocalDate lastUpdateDatetimeFrom,
        @Param("lastUpdateDatetimeTo") LocalDate lastUpdateDatetimeTo,
        Pageable pageable
    );
    
    // Distinct query methods for caching
    @Query(value = """
        SELECT DISTINCT Scan_User 
        FROM Inbound_Shipments 
        WHERE Scan_User IS NOT NULL 
        AND Scan_User <> ''
        ORDER BY Scan_User
        """, nativeQuery = true)
    List<String> findDistinctScanUsers();
    
    @Query(value = """
        SELECT DISTINCT Status 
        FROM Inbound_Shipments 
        WHERE Status IS NOT NULL 
        AND Status <> ''
        ORDER BY Status
        """, nativeQuery = true)
    List<String> findDistinctStatuses();
    
    // V2 Search method with client name support
    @Query(value = """
        SELECT s.Row_ID, s.Tracking_Number, s.Scanned_Number, s.Status, s.Order_Number, 
               s.Lab, s.Scan_User, s.Ship_Date, s.Scan_Time, s.Email_Receive_Datetime, 
               s.Last_Update_Datetime, s.Client_ID, s.Shipment_Type
        FROM Inbound_Shipments s
        LEFT JOIN Inbound_Shipments_Clients c ON s.Client_ID = c.Row_ID
        WHERE (:trackingNumber IS NULL OR s.Tracking_Number LIKE CONCAT('%', :trackingNumber, '%'))
        AND (:scannedNumber IS NULL OR s.Scanned_Number LIKE CONCAT('%', :scannedNumber, '%'))
        AND (:status IS NULL OR s.Status LIKE CONCAT('%', :status, '%'))
        AND (:orderNumber IS NULL OR s.Order_Number LIKE CONCAT('%', :orderNumber, '%'))
        AND (:lab IS NULL OR s.Lab LIKE CONCAT('%', :lab, '%'))
        AND (:scanUser IS NULL OR s.Scan_User LIKE CONCAT('%', :scanUser, '%'))
        AND (:clientName IS NULL OR c.Client LIKE CONCAT('%', :clientName, '%'))
        AND (:shipDateFrom IS NULL OR s.Ship_Date >= :shipDateFrom)
        AND (:shipDateTo IS NULL OR s.Ship_Date <= :shipDateTo)
        AND (:scanDateFrom IS NULL OR CAST(s.Scan_Time AS DATE) >= :scanDateFrom)
        AND (:scanDateTo IS NULL OR CAST(s.Scan_Time AS DATE) <= :scanDateTo)
        AND (:emailReceiveDatetimeFrom IS NULL OR CAST(s.Email_Receive_Datetime AS DATE) >= :emailReceiveDatetimeFrom)
        AND (:emailReceiveDatetimeTo IS NULL OR CAST(s.Email_Receive_Datetime AS DATE) <= :emailReceiveDatetimeTo)
        AND (:lastUpdateDatetimeFrom IS NULL OR CAST(s.Last_Update_Datetime AS DATE) >= :lastUpdateDatetimeFrom)
        AND (:lastUpdateDatetimeTo IS NULL OR CAST(s.Last_Update_Datetime AS DATE) <= :lastUpdateDatetimeTo)
        ORDER BY s.Row_ID DESC
        """, nativeQuery = true)
    Page<InboundShipment> searchShipmentsV2(
        @Param("trackingNumber") String trackingNumber,
        @Param("scannedNumber") String scannedNumber,
        @Param("status") String status,
        @Param("orderNumber") String orderNumber,
        @Param("lab") String lab,
        @Param("scanUser") String scanUser,
        @Param("clientName") String clientName,
        @Param("shipDateFrom") LocalDate shipDateFrom,
        @Param("shipDateTo") LocalDate shipDateTo,
        @Param("scanDateFrom") LocalDate scanDateFrom,
        @Param("scanDateTo") LocalDate scanDateTo,
        @Param("emailReceiveDatetimeFrom") LocalDate emailReceiveDatetimeFrom,
        @Param("emailReceiveDatetimeTo") LocalDate emailReceiveDatetimeTo,
        @Param("lastUpdateDatetimeFrom") LocalDate lastUpdateDatetimeFrom,
        @Param("lastUpdateDatetimeTo") LocalDate lastUpdateDatetimeTo,
        Pageable pageable
    );
} 