IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Inbound_Shipments_Tracking_Number' AND object_id = OBJECT_ID('Inbound_Shipments'))
BEGIN
    -- Inbound_Shipments table
    ALTER TABLE Inbound_Shipments
    ALTER COLUMN Tracking_Number NVARCHAR(255) NULL;

    ALTER TABLE Inbound_Shipments
    ALTER COLUMN Status NVARCHAR(255) NULL;

    ALTER TABLE Inbound_Shipments
    ALTER COLUMN Order_Number NVARCHAR(255) NULL;

    ALTER TABLE Inbound_Shipments
    ALTER COLUMN Scan_User NVARCHAR(255) NULL;

    -- Inbound_Shipments_Clients table
    ALTER TABLE Inbound_Shipments_Clients
    ALTER COLUMN Client NVARCHAR(255) NULL;
END
-- Index for Tracking_Number lookups (exact matches and LIKE searches)
-- Used in: findByTrackingNumber, findAllByTrackingNumber, search queries
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Inbound_Shipments_Tracking_Number' AND object_id = OBJECT_ID('Inbound_Shipments'))
BEGIN
    CREATE NONCLUSTERED INDEX IX_Inbound_Shipments_Tracking_Number
    ON Inbound_Shipments (Tracking_Number)
    WHERE Tracking_Number IS NOT NULL;
END

-- Index for Scanned_Number lookups (exact matches and LIKE searches)
-- Used in: findByScannedNumber, search queries
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Inbound_Shipments_Scanned_Number' AND object_id = OBJECT_ID('Inbound_Shipments'))
BEGIN
    CREATE NONCLUSTERED INDEX IX_Inbound_Shipments_Scanned_Number
    ON Inbound_Shipments (Scanned_Number)
    WHERE Scanned_Number IS NOT NULL;
END

-- Index for Client_ID foreign key lookups
-- Used in: findByScannedNumberAndOrganization, findByTrackingNumberAndOrganization
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Inbound_Shipments_Client_ID' AND object_id = OBJECT_ID('Inbound_Shipments'))
BEGIN
    CREATE NONCLUSTERED INDEX IX_Inbound_Shipments_Client_ID
    ON Inbound_Shipments (Client_ID)
    WHERE Client_ID IS NOT NULL;
END

-- Index for Scan_Time date-based queries
-- Used in: findTodayShipments, findByScanDate, findByScanDateRange, search queries
-- Note: CAST(Scan_Time AS DATE) queries will benefit from this index
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Inbound_Shipments_Scan_Time' AND object_id = OBJECT_ID('Inbound_Shipments'))
BEGIN
    CREATE NONCLUSTERED INDEX IX_Inbound_Shipments_Scan_Time
    ON Inbound_Shipments (Scan_Time)
    WHERE Scan_Time IS NOT NULL;
END

-- Index for Ship_Date date range queries
-- Used in: search queries with shipDateFrom/shipDateTo filters
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Inbound_Shipments_Ship_Date' AND object_id = OBJECT_ID('Inbound_Shipments'))
BEGIN
    CREATE NONCLUSTERED INDEX IX_Inbound_Shipments_Ship_Date
    ON Inbound_Shipments (Ship_Date)
    WHERE Ship_Date IS NOT NULL;
END

-- Index for Email_Receive_Datetime date range queries
-- Used in: search queries with emailReceiveDatetimeFrom/To filters
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Inbound_Shipments_Email_Receive_Datetime' AND object_id = OBJECT_ID('Inbound_Shipments'))
BEGIN
    CREATE NONCLUSTERED INDEX IX_Inbound_Shipments_Email_Receive_Datetime
    ON Inbound_Shipments (Email_Receive_Datetime)
    WHERE Email_Receive_Datetime IS NOT NULL;
END

-- Index for Last_Update_Datetime date range queries
-- Used in: search queries with lastUpdateDatetimeFrom/To filters
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Inbound_Shipments_Last_Update_Datetime' AND object_id = OBJECT_ID('Inbound_Shipments'))
BEGIN
    CREATE NONCLUSTERED INDEX IX_Inbound_Shipments_Last_Update_Datetime
    ON Inbound_Shipments (Last_Update_Datetime)
    WHERE Last_Update_Datetime IS NOT NULL;
END

-- Composite index for Client_ID + Tracking_Number lookups
-- Used in: findByTrackingNumberAndOrganization
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Inbound_Shipments_Client_ID_Tracking_Number' AND object_id = OBJECT_ID('Inbound_Shipments'))
BEGIN
    CREATE NONCLUSTERED INDEX IX_Inbound_Shipments_Client_ID_Tracking_Number
    ON Inbound_Shipments (Client_ID, Tracking_Number)
    WHERE Client_ID IS NOT NULL AND Tracking_Number IS NOT NULL AND Tracking_Number <> '';
END

-- Composite index for Client_ID + Scanned_Number lookups
-- Used in: findByScannedNumberAndOrganization
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Inbound_Shipments_Client_ID_Scanned_Number' AND object_id = OBJECT_ID('Inbound_Shipments'))
BEGIN
    CREATE NONCLUSTERED INDEX IX_Inbound_Shipments_Client_ID_Scanned_Number
    ON Inbound_Shipments (Client_ID, Scanned_Number)
    WHERE Client_ID IS NOT NULL AND Scanned_Number IS NOT NULL AND Scanned_Number <> '';
END

-- Index for Status lookups
-- Used in: findByStatus, search queries, findDistinctStatuses
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Inbound_Shipments_Status' AND object_id = OBJECT_ID('Inbound_Shipments'))
BEGIN
    CREATE NONCLUSTERED INDEX IX_Inbound_Shipments_Status
    ON Inbound_Shipments (Status)
    WHERE Status IS NOT NULL AND Status <> '';
END

-- Index for Order_Number lookups
-- Used in: findByOrderNumber, search queries
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Inbound_Shipments_Order_Number' AND object_id = OBJECT_ID('Inbound_Shipments'))
BEGIN
    CREATE NONCLUSTERED INDEX IX_Inbound_Shipments_Order_Number
    ON Inbound_Shipments (Order_Number)
    WHERE Order_Number IS NOT NULL;
END

-- Index for Scan_User lookups
-- Used in: search queries, findDistinctScanUsers
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Inbound_Shipments_Scan_User' AND object_id = OBJECT_ID('Inbound_Shipments'))
BEGIN
    CREATE NONCLUSTERED INDEX IX_Inbound_Shipments_Scan_User
    ON Inbound_Shipments (Scan_User)
    WHERE Scan_User IS NOT NULL AND Scan_User <> '';
END

-- Composite index for Scan_Time + Row_ID (for date queries with ORDER BY Row_ID DESC)
-- Used in: findTodayShipments, findByScanDate, findByScanDateRange
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Inbound_Shipments_Scan_Time_Row_ID' AND object_id = OBJECT_ID('Inbound_Shipments'))
BEGIN
    CREATE NONCLUSTERED INDEX IX_Inbound_Shipments_Scan_Time_Row_ID
    ON Inbound_Shipments (Scan_Time, Row_ID DESC)
    WHERE Scan_Time IS NOT NULL;
END

-- ============================================================================
-- Inbound_Shipments_Clients Table Indexes
-- ============================================================================

-- Index for Client lookups (exact matches)
-- Used in: findByClient, existsByClient
-- Note: LIKE searches with LOWER() may not use this index efficiently
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Inbound_Shipments_Clients_Client' AND object_id = OBJECT_ID('Inbound_Shipments_Clients'))
BEGIN
    CREATE NONCLUSTERED INDEX IX_Inbound_Shipments_Clients_Client
    ON Inbound_Shipments_Clients (Client)
    WHERE Client IS NOT NULL;
END

-- Index for Last_Update_User lookups
-- Used in: findByLastUpdateUser
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Inbound_Shipments_Clients_Last_Update_User' AND object_id = OBJECT_ID('Inbound_Shipments_Clients'))
BEGIN
    CREATE NONCLUSTERED INDEX IX_Inbound_Shipments_Clients_Last_Update_User
    ON Inbound_Shipments_Clients (Last_Update_User)
    WHERE Last_Update_User IS NOT NULL;
END

-- ============================================================================
-- CLIA_Member Table Indexes
-- ============================================================================

-- Note: User_ID already has a unique constraint (unique index exists)
-- Index for Last_Update_User lookups
-- Used in: findByLastUpdateUser
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_CLIA_Member_Last_Update_User' AND object_id = OBJECT_ID('CLIA_Member'))
BEGIN
    CREATE NONCLUSTERED INDEX IX_CLIA_Member_Last_Update_User
    ON CLIA_Member (Last_Update_User)
    WHERE Last_Update_User IS NOT NULL;
END

-- ============================================================================
-- CLIA_Admin Table Indexes
-- ============================================================================

-- Note: User_ID already has a unique constraint (unique index exists)
-- Index for Last_Update_User lookups
-- Used in: findByLastUpdateUser
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_CLIA_Admin_Last_Update_User' AND object_id = OBJECT_ID('CLIA_Admin'))
BEGIN
    CREATE NONCLUSTERED INDEX IX_CLIA_Admin_Last_Update_User
    ON CLIA_Admin (Last_Update_User)
    WHERE Last_Update_User IS NOT NULL;
END

-- ============================================================================
-- Inbound_Shipments_Reference Table Indexes
-- ============================================================================

-- Composite index for Type + Value lookups
-- Used in: findByTypeAndValue queries
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Inbound_Shipments_Reference_Type_Value' AND object_id = OBJECT_ID('Inbound_Shipments_Reference'))
BEGIN
    CREATE NONCLUSTERED INDEX IX_Inbound_Shipments_Reference_Type_Value
    ON Inbound_Shipments_Reference (Type, Value);
END

-- ============================================================================
-- Notes on Index Design:
-- ============================================================================
-- 1. Filtered indexes (WHERE clauses) are used to reduce index size and
--    improve performance by excluding NULL values where appropriate.
--
-- 2. Composite indexes are created for queries that filter on multiple
--    columns together (e.g., Client_ID + Tracking_Number).
--
-- 3. Date column indexes support range queries and date-based filtering.
--
-- 4. LIKE '%value%' queries (leading wildcard) will not efficiently use
--    indexes, but exact matches and prefix searches will benefit.
--
-- 5. The Row_ID column already has a clustered primary key index, which
--    is optimal for ORDER BY Row_ID DESC queries.
--
-- 6. Foreign key columns are indexed to improve JOIN performance and
--    constraint checking.
--
-- 7. All indexes are created conditionally using IF NOT EXISTS to allow
--    safe re-execution of this script.
-- ============================================================================




