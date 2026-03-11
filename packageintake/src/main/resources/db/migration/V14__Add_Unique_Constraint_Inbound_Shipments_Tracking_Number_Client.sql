-- Enforce uniqueness on (Tracking_Number, Client) for Inbound_Shipments.
-- Remove existing duplicates first (e.g. maintenance/remove-inbound-shipments-duplicates.sql) or this migration will fail.
-- Index key columns cannot be (N)VARCHAR(MAX); alter to bounded length. Drop dependent indexes first, then recreate. Safe to run again.

-- Drop indexes that depend on Tracking_Number (block ALTER COLUMN)
IF EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Inbound_Shipments_Tracking_Number' AND object_id = OBJECT_ID('Inbound_Shipments'))
    DROP INDEX IX_Inbound_Shipments_Tracking_Number ON Inbound_Shipments;

IF EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Inbound_Shipments_Client_ID_Tracking_Number' AND object_id = OBJECT_ID('Inbound_Shipments'))
    DROP INDEX IX_Inbound_Shipments_Client_ID_Tracking_Number ON Inbound_Shipments;

-- Alter columns to bounded length so they can be used in unique index
ALTER TABLE Inbound_Shipments
ALTER COLUMN Client NVARCHAR(255) NULL;

ALTER TABLE Inbound_Shipments
ALTER COLUMN Tracking_Number NVARCHAR(255) NULL;

-- Recreate dropped indexes
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Inbound_Shipments_Tracking_Number' AND object_id = OBJECT_ID('Inbound_Shipments'))
BEGIN
    CREATE NONCLUSTERED INDEX IX_Inbound_Shipments_Tracking_Number
    ON Inbound_Shipments (Tracking_Number)
    WHERE Tracking_Number IS NOT NULL;
END

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Inbound_Shipments_Client_ID_Tracking_Number' AND object_id = OBJECT_ID('Inbound_Shipments'))
BEGIN
    CREATE NONCLUSTERED INDEX IX_Inbound_Shipments_Client_ID_Tracking_Number
    ON Inbound_Shipments (Client_ID, Tracking_Number)
    WHERE Client_ID IS NOT NULL AND Tracking_Number IS NOT NULL AND Tracking_Number <> '';
END

IF NOT EXISTS (
    SELECT 1 FROM sys.key_constraints
    WHERE name = 'UK_Inbound_Shipments_Tracking_Number_Client'
      AND parent_object_id = OBJECT_ID('Inbound_Shipments')
)
BEGIN
    ALTER TABLE Inbound_Shipments
    ADD CONSTRAINT UK_Inbound_Shipments_Tracking_Number_Client UNIQUE (Tracking_Number, Client);
END
