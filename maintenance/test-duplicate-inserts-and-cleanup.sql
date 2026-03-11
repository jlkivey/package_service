-- =============================================================================
-- Test data for duplicate (Tracking_Number, Client) handling and cleanup
-- =============================================================================
-- Run the INSERT block to add test rows, then run duplicate detection/removal
-- and unique-constraint migration as needed. Run the DELETE block to remove
-- all test data when done.
-- Uses Client = 'TEST_DUPLICATE_CLEANUP' and Tracking_Number = 'TEST_TN_99999'
-- so cleanup is safe and targeted.
-- =============================================================================

SET NOCOUNT ON;

-- -----------------------------------------------------------------------------
-- INSERTS: 3 rows with same (Tracking_Number, Client) to test duplicate logic
-- Row 1: no scan info (should be removed by duplicate-removal script)
-- Row 2: no scan info (should be removed)
-- Row 3: has Scan_Time and Scan_User (should be KEPT by duplicate-removal script)
-- Trigger will set Client_ID from Client.
-- -----------------------------------------------------------------------------
INSERT INTO dbo.Inbound_Shipments (
    Client,
    Tracking_Number,
    Status,
    Email_ID,
    Order_Number,
    Ship_Date,
    Lab,
    Weight,
    Number_Of_Samples,
    Pickup_Time,
    Pickup_Time_2,
    Email_Receive_Datetime,
    Last_Update_Datetime,
    Scan_Time,
    Scan_User,
    Scanned_Number,
    Shipment_Type
)
VALUES
    (
        'TEST_DUPLICATE_CLEANUP',
        'TEST_TN_99999',
        'Pending',
        NULL,
        NULL,
        CAST(GETDATE() AS DATE),
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        GETDATE(),
        GETDATE(),
        NULL,   -- no scan
        NULL,   -- no scan user
        NULL,
        NULL
    ),
    (
        'TEST_DUPLICATE_CLEANUP',
        'TEST_TN_99999',
        'Pending',
        NULL,
        NULL,
        CAST(GETDATE() AS DATE),
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        GETDATE(),
        GETDATE(),
        NULL,
        NULL,
        NULL,
        NULL
    ),
    (
        'TEST_DUPLICATE_CLEANUP',
        'TEST_TN_99999',
        'Scanned',
        NULL,
        NULL,
        CAST(GETDATE() AS DATE),
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        GETDATE(),
        GETDATE(),
        GETDATE(),              -- has scan time
        'TEST_SCAN_USER',       -- has scan user (this row should be kept)
        NULL,
        NULL
    );

SELECT @@ROWCOUNT AS TestRowsInserted;

-- -----------------------------------------------------------------------------
-- CLEANUP: Remove test rows and the test client (run when done testing)
-- -----------------------------------------------------------------------------
/*
DELETE FROM dbo.Inbound_Shipments
WHERE Client = 'TEST_DUPLICATE_CLEANUP'
   OR Tracking_Number = 'TEST_TN_99999';

DELETE FROM dbo.Inbound_Shipments_Clients
WHERE Client = 'TEST_DUPLICATE_CLEANUP';

SELECT @@ROWCOUNT AS RowsDeleted;
*/
